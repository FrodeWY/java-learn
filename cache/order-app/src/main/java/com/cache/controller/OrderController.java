package com.cache.controller;

import com.cache.dto.request.CreateOrderRequestDTO;
import com.cache.service.OrderService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单表(正向)(Order)控制层
 *
 * @author makejava
 * @since 2021-09-01 22:12:49
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
   * 创建订单
   */
  @PostMapping("/create")
  public void create(@RequestBody CreateOrderRequestDTO requestDTO) {
    orderService.insert(requestDTO);
  }

}