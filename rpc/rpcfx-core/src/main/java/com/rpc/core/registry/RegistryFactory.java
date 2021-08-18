package com.rpc.core.registry;

import com.rpc.core.api.Registry;

/**
 * @author wangyang
 * @ClassName RegistryFactory
 * @Description TODO
 * @Date 2021/8/18 下午8:02
 * @Version 1.0
 */
public class RegistryFactory {


  public static Registry getRegistry(String type, String address) {
    Registry registry;
    if (ZookeeperRegistry.NAME.equals(type)) {
      registry = new ZookeeperRegistry(address);
    } else {
      throw new IllegalArgumentException("not found class type is :" + type + " registry");
    }
    return registry;
  }
}
