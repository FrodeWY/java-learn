package com.cache.dao;

import com.cache.entity.OrderDetail;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单详情表(OrderDetail)表数据库访问层
 *
 * @author makejava
 * @since 2021-09-03 23:36:07
 */
@Mapper
public interface OrderDetailDao {

  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  OrderDetail queryById(Long id);

  /**
   * 查询指定行数据
   *
   * @param offset 查询起始位置
   * @param limit 查询条数
   * @return 对象列表
   */
  List<OrderDetail> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


  /**
   * 通过实体作为筛选条件查询
   *
   * @param orderDetail 实例对象
   * @return 对象列表
   */
  List<OrderDetail> queryAll(OrderDetail orderDetail);

  /**
   * 新增数据
   *
   * @param orderDetail 实例对象
   * @return 影响行数
   */
  int insert(OrderDetail orderDetail);

  int insertBatch(List<OrderDetail> list);

  /**
   * 修改数据
   *
   * @param orderDetail 实例对象
   * @return 影响行数
   */
  int update(OrderDetail orderDetail);

  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 影响行数
   */
  int deleteById(Long id);

}