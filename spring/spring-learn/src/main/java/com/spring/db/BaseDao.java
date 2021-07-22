package com.spring.db;

import java.sql.SQLException;

public interface BaseDao<T> {

  /**
   * 根据主键删除
   */
  void deleteByPrimaryKey(Long id);

  /**
   * 创建
   */
  void create(T t);

  /**
   * 根据主键查询
   */
  T getByPrimaryKey(Long id) throws SQLException;

  /**
   * 更新
   */
  void update(T t);
}
