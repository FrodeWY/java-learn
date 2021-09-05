package com.cache.service;

import com.cache.dto.request.CreateOrderRequestDTO;

/**
 * 订单表(正向)(Order1)表服务接口
 *
 * @author makejava
 * @since 2021-09-01 22:12:49
 */
public interface OrderService {


  /**
   * 新增数据
   *
   * @param requestDTO 创建订单请求体
   * @return 实例对象
   */
  void insert(CreateOrderRequestDTO requestDTO);

}