package com.readwrite.loadbalance;

import java.util.List;

/**
 * @author wangyang
 * @ClassName Loadbalance
 * @Description TODO
 * @Date 2021/8/7 下午1:03
 * @Version 1.0
 */
public interface LoadBalance {

  /**
   * 选择一个数据源
   *
   * @param dataSourceKeyList 数据源对应的名称集合
   */
  Object select(List<Object> dataSourceKeyList);

}
