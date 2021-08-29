package com.rpc.core.api;

import java.util.Properties;

/**
 * 配置中心监听器
 */
public interface ConfigCenterListener {

  /**
   * 当监听的配置中心相应的配置变动,会触发监听
   *
   * @param properties 变动后的配置
   */
  void notify(Properties properties);

  ;
}
