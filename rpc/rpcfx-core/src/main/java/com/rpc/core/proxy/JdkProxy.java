package com.rpc.core.proxy;

import com.rpc.core.api.*;

import com.rpc.core.common.RpcfxRequest;
import com.rpc.core.common.RpcfxResponse;
import com.rpc.core.protocol.RegistryProtocol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private RegistryProtocol protocol;
    private static Map<String, Invoker> invokerMap = new ConcurrentHashMap<>();

    public JdkProxy() {

    }

    public <T> T proxy(Class<T> serviceClass, Invoker invoker) {
        this.serviceClass = serviceClass;
        this.invoker = invoker;
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, this);
    }

    public GenericService genericServiceProxy(RegistryProtocol protocol) {
        this.protocol = protocol;
        this.serviceClass = GenericService.class;
        return (GenericService) Proxy.newProxyInstance(GenericService.class.getClassLoader(), new Class[]{GenericService.class}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcfxRequest rpcfxRequest = new RpcfxRequest();
        Class<?> returnType = method.getReturnType();
        rpcfxRequest.setReturnType(returnType);
        //泛型调用
        if (GenericService.class.isAssignableFrom(serviceClass)) {
            rpcfxRequest.setGeneric(true);
            String serviceClass = (String) args[0];
            rpcfxRequest.setServiceClass(serviceClass);
            rpcfxRequest.setMethod((String) args[1]);
            rpcfxRequest.setParameterTypes((String[]) args[2]);
            rpcfxRequest.setParams((Object[]) args[3]);
            invoker = invokerMap.computeIfAbsent(serviceClass, k -> protocol.getInvoker(serviceClass));
        } else {
            rpcfxRequest.setServiceClass(serviceClass.getName());
            rpcfxRequest.setMethod(method.getName());
            rpcfxRequest.setGeneric(false);
            rpcfxRequest.setParams(args);
            Class<?>[] parameterTypes = method.getParameterTypes();
            String[] parameterArray = new String[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                parameterArray[i] = arg(parameterType);
            }
            rpcfxRequest.setParameterTypes(parameterArray);
        }

        RpcfxResponse response = invoker.invoke(rpcfxRequest);
        if (response.isStatus()) {
            return response.getResult();
        }
        throw response.getException();
    }

    private String arg(Class<?> cl) {
        if (cl.isPrimitive()) {
            if (cl == Boolean.TYPE) {
                return Boolean.class.getName();
            }
            if (cl == Byte.TYPE) {
                return Byte.class.getName();
            }
            if (cl == Character.TYPE) {
                return Character.class.getName();
            }
            if (cl == Double.TYPE) {
                return Double.class.getName();
            }
            if (cl == Float.TYPE) {
                return Float.class.getName();
            }
            if (cl == Integer.TYPE) {
                return Integer.class.getName();
            }
            if (cl == Long.TYPE) {
                return Long.class.getName();
            }
            if (cl == Short.TYPE) {
                return Short.class.getName();
            }
            throw new RuntimeException("Unknown primitive type: " + cl.getName());
        }
        return cl.getName();
    }
}
