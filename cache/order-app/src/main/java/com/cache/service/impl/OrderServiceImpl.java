package com.cache.service.impl;

import com.alibaba.fastjson.JSON;
import com.cache.dao.CommodityStockDao;
import com.cache.dao.OrderDao;
import com.cache.dao.OrderDetailDao;
import com.cache.dto.request.CreateOrderRequestDTO;
import com.cache.entity.CommodityStock;
import com.cache.entity.Order;
import com.cache.entity.OrderDetail;
import com.cache.service.OrderService;
import com.cache.utils.DistributionIdGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 订单表(正向)(Order1)表服务实现类
 *
 * @author makejava
 * @since 2021-09-01 22:12:49
 */
@Service("order1Service")
public class OrderServiceImpl implements OrderService, InitializingBean {

  private final OrderDao orderDao;
  private final CommodityStockDao commodityStockDao;
  private final OrderDetailDao orderDetailDao;
  private final StringRedisTemplate redisTemplate;
  private final ThreadPoolExecutor threadPoolExecutor;

  public OrderServiceImpl(OrderDao orderDao, CommodityStockDao commodityStockDao,
      OrderDetailDao orderDetailDao, StringRedisTemplate redisTemplate, ThreadPoolExecutor threadPoolExecutor) {
    this.orderDao = orderDao;
    this.commodityStockDao = commodityStockDao;
    this.orderDetailDao = orderDetailDao;
    this.redisTemplate = redisTemplate;
    this.threadPoolExecutor = threadPoolExecutor;
  }


  /**
   * 新增数据
   *
   * @param requestDTO 创建请求体
   * @return 实例对象
   */
  @Override
  public void insert(CreateOrderRequestDTO requestDTO) {
    List<CreateOrderRequestDTO.OrderDetail> orderDetailList = requestDTO.getOrderDetailList();
    if (CollectionUtils.isEmpty(orderDetailList)) {
      throw new RuntimeException("订单明细不可为空");
    }
    //同步扣减库存
    List<Future<?>> futureList = new ArrayList<>(orderDetailList.size());
    for (CreateOrderRequestDTO.OrderDetail orderDetail : orderDetailList) {
      Future<?> submit = threadPoolExecutor.submit(new DecrStockTask(orderDetail.getSkuCode(), orderDetail.getQuantity()));
      futureList.add(submit);
    }
    for (Future<?> future : futureList) {
      try {
        future.get();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    //如果商品库存都充足,将当前请求放入redis队列里等待消费
    ListOperations<String, String> opsForList = redisTemplate.opsForList();
    opsForList.leftPush("createOrderQueue", JSON.toJSONString(requestDTO));
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    CreateOrderQueueConsumer createOrderQueueConsumer = new CreateOrderQueueConsumer();
    threadPoolExecutor.execute(createOrderQueueConsumer);
  }


  /**
   * 订单创建队列消费者
   */
  private class CreateOrderQueueConsumer implements Runnable {

    @Override
    public void run() {
      ListOperations<String, String> opsForList = redisTemplate.opsForList();
      while (!Thread.interrupted()) {
        String message = opsForList.rightPop("createOrderQueue", 30, TimeUnit.SECONDS);
        if (message == null) {
          continue;
        }
        CreateOrderRequestDTO requestDTO = JSON.parseObject(message, CreateOrderRequestDTO.class);
        CreateOrderRequestDTO.Order order = requestDTO.getOrder();
        List<CreateOrderRequestDTO.OrderDetail> orderDetailList = requestDTO.getOrderDetailList();
        Order orderPo = convertOrder(order);
        List<OrderDetail> orderDetailPos = convertOrderDetailList(orderDetailList, orderPo.getOrderId());
        orderDao.insert(orderPo);
        orderDetailDao.insertBatch(orderDetailPos);
      }
    }

    private List<OrderDetail> convertOrderDetailList(List<CreateOrderRequestDTO.OrderDetail> orderDetailList, Long orderId) {
      return orderDetailList.stream().map(orderDetail -> {
        OrderDetail detail = new OrderDetail();
        detail.setId(DistributionIdGenerator.generateSingleId());
        detail.setOrderId(orderId);
        detail.setSkuCode(orderDetail.getSkuCode());
        detail.setQuantity(orderDetail.getQuantity());
        detail.setUnitPrice(orderDetail.getUnitPrice());
        detail.setTaxAmount(orderDetail.getTaxAmount());
        detail.setActualAmount(orderDetail.getActualAmount());
        detail.setEnabled(1);
        long currentTimeMillis = System.currentTimeMillis();
        detail.setCreateTime(currentTimeMillis);
        detail.setUpdateTime(currentTimeMillis);
        detail.setUnitExchangeScore(orderDetail.getUnitExchangeScore());
        detail.setCurrency(orderDetail.getCurrency());
        return detail;
      }).collect(Collectors.toList());
    }

    private Order convertOrder(CreateOrderRequestDTO.Order order) {
      Order orderPo = new Order();
      orderPo.setId(DistributionIdGenerator.generateSingleId());
      orderPo.setOrderId(DistributionIdGenerator.generateSingleId());
      orderPo.setBuyerMemberId(order.getBuyerMemberId());
      orderPo.setParentOrderId(order.getParentOrderId());
      orderPo.setOrderAmount(order.getOrderAmount());
      orderPo.setPayAmount(order.getPayAmount());
      orderPo.setDiscountAmount(order.getDiscountAmount());
      orderPo.setTaxAmount(order.getTaxAmount());
      orderPo.setScoreDeduction(order.getScoreDeduction());
      orderPo.setScore(order.getScore());
      orderPo.setGrowthValue(order.getGrowthValue());
      orderPo.setStatus(0);
      orderPo.setReceiveAddress(order.getReceiveAddress());
      orderPo.setReceiverName(order.getReceiverName());
      orderPo.setReceiverPhone(order.getReceiverPhone());
      long currentTimeMillis = System.currentTimeMillis();
      orderPo.setCreateTime(currentTimeMillis);
      orderPo.setUpdateTime(currentTimeMillis);
      orderPo.setEnabled(1);
      orderPo.setCurrency(order.getCurrency());
      return orderPo;
    }
  }

  /**
   * 扣减库存任务
   */
  private class DecrStockTask implements Runnable {

    private String skuCode;
    private Integer quantity;

    public DecrStockTask(String skuCode, Integer quantity) {
      this.skuCode = skuCode;
      this.quantity = quantity;
    }

    @Override
    public void run() {
      while (!Thread.interrupted()) {
        CommodityStock commodityStock = commodityStockDao.queryBySkuCode(skuCode);
        if (commodityStock == null) {
          throw new RuntimeException(skuCode + " 商品库存数据不存在");
        }
        CommodityStock update = commodityStock;
        long updateStockAmount = commodityStock.getStockAmount() - quantity;

        if (updateStockAmount >= 0) {
          update.setStockAmount(updateStockAmount);
        } else {
          throw new RuntimeException(skuCode + " 商品库存不足");
        }
        update.setUpdateTime(System.currentTimeMillis());
        int i = commodityStockDao.updateStock(update);
        if (i > 0) {
          break;
        }
      }
    }
  }


}