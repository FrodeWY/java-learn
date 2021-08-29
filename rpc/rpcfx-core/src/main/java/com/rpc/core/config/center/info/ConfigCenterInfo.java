package com.rpc.core.config.center.info;

/**
 * 配置中心配置信息
 */
public interface ConfigCenterInfo {

  /**
   * 校验配置中心配置信息是否合法
   *
   * @return true:合法,false:配置不合法
   */
  boolean checkValid();

  /**
   * 获取配置中心配置信息的唯一标识;
   */
  String centerUniqueKey();
}
