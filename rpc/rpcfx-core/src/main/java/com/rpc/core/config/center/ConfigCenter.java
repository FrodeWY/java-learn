package com.rpc.core.config.center;

import com.rpc.core.api.ConfigCenterListener;
import com.rpc.core.config.center.info.ConfigCenterInfo;
import java.util.Properties;

/**
 * 配置中心
 */
public interface ConfigCenter {

  /**
   * 从配置中心获取配置信息
   *
   * @param info 配置中心配置信息
   * @return 配置中心中的配置
   */
  Properties getProperties(ConfigCenterInfo info);

  /**
   * 监听配置信息
   *
   * @param info 配置中心配置信息
   */
  Properties getPropertiesAsync(ConfigCenterInfo info, ConfigCenterListener listener);
}
