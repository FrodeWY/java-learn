package com.db;

public interface BaseDao<T> {

  /**
   * 根据主键删除
   */
  void deleteByPrimaryKey(Long id) throws Exception;

  /**
   * 创建
   */
  void create(T t) throws Exception;

  /**
   * 根据主键查询
   */
  T getByPrimaryKey(Long id) throws Exception;

  /**
   * 更新
   */
  void update(T t) throws Exception;
}
