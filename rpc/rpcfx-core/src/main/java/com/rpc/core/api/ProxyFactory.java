package com.rpc.core.api;

import com.rpc.core.protocol.RegistryProtocol;

/**
 * @author wangyang
 * @ClassName ProxyFactory
 * @Description TODO
 * @Date 2021/8/18 下午9:55
 * @Version 1.0
 */
public interface ProxyFactory {

   <T> T proxy(Class<T> serviceClass, Invoker invoker);

   GenericService genericServiceProxy(RegistryProtocol protocol, String group, String version);
}
