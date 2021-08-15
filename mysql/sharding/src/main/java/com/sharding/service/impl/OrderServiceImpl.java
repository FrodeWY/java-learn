package com.sharding.service.impl;

import com.sharding.dao.OrderDao;
import com.sharding.entity.Order;
import com.sharding.service.OrderService;
import java.util.List;
import javax.annotation.Resource;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 订单表(正向)(Order)表服务实现类
 *
 * @author makejava
 * @since 2021-08-06 12:49:51
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

  @Resource
  private OrderDao orderDao;

  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  @Override
  public Order queryById(Long id) {
    return this.orderDao.queryById(id);
  }

  /**
   * 查询多条数据
   *
   * @param offset 查询起始位置
   * @param limit 查询条数
   * @return 对象列表
   */
  @Override
  public List<Order> queryAllByLimit(int offset, int limit) {
    return this.orderDao.queryAllByLimit(offset, limit);
  }

  @Override
  @Transactional(rollbackFor = Throwable.class)
//  @ShardingTransactionType(value = TransactionType.LOCAL)
//  @ShardingTransactionType(value = TransactionType.BASE)
  @ShardingTransactionType(value = TransactionType.XA)
  public void insertBatch(List<Order> list) {
//    orderDao.insertBatch(list);

    if (CollectionUtils.isEmpty(list)) {
      return;
    }
    for (int i = 0; i < list.size(); i++) {
      if (i == (list.size() - 1)) {
        throw new RuntimeException("test rollback");
      }
      Order order = list.get(i);
      orderDao.insert(order);
    }
  }

  /**
   * 新增数据
   *
   * @param order 实例对象
   * @return 实例对象
   */
  @Override
  @ShardingTransactionType(value = TransactionType.XA)
  public Order insert(Order order) {
    this.orderDao.insert(order);
    if ((order.getId() & 1) == 1) {
      throw new RuntimeException("test rollback");
    }
    return order;
  }

  /**
   * 修改数据
   *
   * @param order 实例对象
   * @return 实例对象
   */
  @Override
  public Order update(Order order) {
    this.orderDao.update(order);
    return this.queryById(order.getId());
  }

  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 是否成功
   */
  @Override
  public boolean deleteById(Long id) {
    return this.orderDao.deleteById(id) > 0;
  }
}