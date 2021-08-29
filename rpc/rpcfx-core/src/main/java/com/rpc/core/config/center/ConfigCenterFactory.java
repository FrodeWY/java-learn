package com.rpc.core.config.center;

/**
 * @author wangyang
 * @ClassName ConfigCenterFactory
 * @Description TODO
 * @Date 2021/8/29 下午4:57
 * @Version 1.0
 */
public class ConfigCenterFactory {

  public static ConfigCenter getConfigCenter(String type) {
    ConfigCenter configCenter = null;
    if (ZookeeperConfigCenter.NAME.equals(type)) {
      configCenter = new ZookeeperConfigCenter();
    } else {
      throw new IllegalArgumentException("not found class type is :" + type + " cluster");
    }
    return configCenter;
  }
}
