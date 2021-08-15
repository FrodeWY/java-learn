package com.sharding.controller;

import com.sharding.entity.Order;
import com.sharding.service.OrderService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单表(正向)(Order)表控制层
 *
 * @author makejava
 * @since 2021-08-06 12:49:52
 */
@RestController
@RequestMapping("order")
public class OrderController {

  /**
   * 服务对象
   */
  @Resource
  private OrderService orderService;

  /**
   * 通过主键查询单条数据
   *
   * @param id 主键
   * @return 单条数据
   */
  @GetMapping("selectOne/{id}")
  public Order selectOne(@PathVariable(value = "id") Long id) {
    return this.orderService.queryById(id);
  }


  @PostMapping("insertOne")
  public Order insertOne(@RequestBody Order order) {
    return this.orderService.insert(order);
  }

  @PostMapping("insertBatch")
  public void insertBatch(@RequestBody List<Order> list) {
    this.orderService.insertBatch(list);
  }
}