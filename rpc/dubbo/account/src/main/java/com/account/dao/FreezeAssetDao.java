package com.account.dao;

import com.account.entity.FreezeAsset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 资产冻结表(FreezeAsset)表数据库访问层
 *
 * @author makejava
 * @since 2021-08-21 22:02:41
 */
@Mapper
public interface FreezeAssetDao {

  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  FreezeAsset queryById(Long id);

  /**
   * 根据流水号和冻结记录状态查询冻结记录
   *
   * @param transactionNo 流水号
   * @param freezeStatus 冻结状态(-1:失效,0:冻结,1:解冻)
   * @return 冻结记录
   */
  FreezeAsset queryByTransactionNoAndFreezeStatus(@Param("transactionNo") String transactionNo, @Param("freezeStatus") int freezeStatus);

  /**
   * 查询指定行数据
   *
   * @param offset 查询起始位置
   * @param limit 查询条数
   * @return 对象列表
   */
  List<FreezeAsset> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


  /**
   * 通过实体作为筛选条件查询
   *
   * @param freezeAsset 实例对象
   * @return 对象列表
   */
  List<FreezeAsset> queryAll(FreezeAsset freezeAsset);

  /**
   * 新增数据
   *
   * @param freezeAsset 实例对象
   * @return 影响行数
   */
  int insert(FreezeAsset freezeAsset);

  /**
   * 修改数据
   *
   * @param freezeAsset 实例对象
   * @return 影响行数
   */
  int update(FreezeAsset freezeAsset);


  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 影响行数
   */
  int deleteById(Long id);

}