package com.rpc.core.proxy;

import com.rpc.core.api.GenericService;
import com.rpc.core.api.Invoker;
import com.rpc.core.api.ProxyFactory;
import com.rpc.core.api.RpcfxRequest;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @ClassName JdkProxyFactory
 * @Description TODO
 * @Date 2021/8/18 下午9:56
 * @Version 1.0
 */
public class JdkProxy implements InvocationHandler, ProxyFactory {

  public final static String NAME = "jdk";
  private Class<?> serviceClass;
  private Invoker invoker;

  public JdkProxy(Class<?> serviceClass, Invoker invoker) {
    this.serviceClass = serviceClass;
    this.invoker = invoker;
  }

  public <T> T proxy() {
    return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), serviceClass.getInterfaces(), this);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    RpcfxRequest rpcfxRequest = new RpcfxRequest();
    //泛型调用
    if (GenericService.class.isAssignableFrom(serviceClass)) {
      rpcfxRequest.setGeneric(true);
      rpcfxRequest.setServiceClass((String) args[0]);
      rpcfxRequest.setMethod((String) args[1]);
      rpcfxRequest.setParameterTypes((String[]) args[2]);
      rpcfxRequest.setParams((Object[]) args[3]);
    } else {
      rpcfxRequest.setServiceClass(serviceClass.getName());
      rpcfxRequest.setMethod(method.getName());
      rpcfxRequest.setGeneric(false);
      rpcfxRequest.setParams(args);
      Class<?>[] parameterTypes = method.getParameterTypes();
      String[] parameterArray = (String[]) Arrays.stream(parameterTypes).map(Class::getName).toArray();
      rpcfxRequest.setParameterTypes(parameterArray);
    }

    return invoker.invoke(rpcfxRequest);
  }
}
