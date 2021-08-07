package com.readwrite3.dao;

import com.readwrite3.entity.Order;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单表(正向)(Order)表数据库访问层
 *
 * @author makejava
 * @since 2021-08-06 12:49:48
 */
@Mapper
public interface OrderDao {

  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  Order queryById(Long id);

  /**
   * 查询指定行数据
   *
   * @param offset 查询起始位置
   * @param limit 查询条数
   * @return 对象列表
   */
  List<Order> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


  /**
   * 通过实体作为筛选条件查询
   *
   * @param order 实例对象
   * @return 对象列表
   */
  List<Order> queryAll(Order order);

  /**
   * 新增数据
   *
   * @param order 实例对象
   * @return 影响行数
   */
  int insert(Order order);

  /**
   * 修改数据
   *
   * @param order 实例对象
   * @return 影响行数
   */
  int update(Order order);

  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 影响行数
   */
  int deleteById(Long id);

}