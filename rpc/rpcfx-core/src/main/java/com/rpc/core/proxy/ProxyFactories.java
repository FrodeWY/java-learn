package com.rpc.core.proxy;

import com.rpc.core.api.Invoker;

/**
 * @author wangyang
 * @ClassName ProxyFactorys
 * @Description TODO
 * @Date 2021/8/18 下午10:17
 * @Version 1.0
 */
public class ProxyFactories {

  public static <T> T proxy(String proxyType, Class<T> serviceClass, Invoker invoker) {
    T proxy;
    if (JdkProxy.NAME.equals(proxyType)) {
      proxy = new JdkProxy(serviceClass, invoker).proxy();
    } else {
      throw new IllegalArgumentException("not found class type is :" + proxyType + " proxy");
    }
    return proxy;
  }
}
