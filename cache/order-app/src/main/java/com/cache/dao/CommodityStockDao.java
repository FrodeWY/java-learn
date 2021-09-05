package com.cache.dao;

import com.cache.entity.CommodityStock;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存表(CommodityStock)表数据库访问层
 *
 * @author makejava
 * @since 2021-09-03 23:36:07
 */
@Mapper
public interface CommodityStockDao {

  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  CommodityStock queryById(Long id);

  /**
   * 根据skuCode 获取库存
   */
  CommodityStock queryBySkuCode(String skuCode);

  /**
   * 查询指定行数据
   *
   * @param offset 查询起始位置
   * @param limit 查询条数
   * @return 对象列表
   */
  List<CommodityStock> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


  /**
   * 通过实体作为筛选条件查询
   *
   * @param commodityStock 实例对象
   * @return 对象列表
   */
  List<CommodityStock> queryAll(CommodityStock commodityStock);

  /**
   * 新增数据
   *
   * @param commodityStock 实例对象
   * @return 影响行数
   */
  int insert(CommodityStock commodityStock);

  /**
   * 修改数据
   *
   * @param commodityStock 实例对象
   * @return 影响行数
   */
  int update(CommodityStock commodityStock);

  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 影响行数
   */
  int deleteById(Long id);

  /**
   * 扣减库存
   *
   * @param commodityStock 实例对象
   * @return 影响行数
   */
  int updateStock(CommodityStock commodityStock);
}