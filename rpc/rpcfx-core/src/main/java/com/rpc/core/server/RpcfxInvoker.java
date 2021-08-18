package com.rpc.core.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rpc.core.api.RpcfxRequest;
import com.rpc.core.api.RpcfxResolver;
import com.rpc.core.api.RpcfxResponse;
import com.rpc.core.api.Filter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class RpcfxInvoker {

    private RpcfxResolver resolver;

    private List<Filter> filters;

    public RpcfxInvoker(RpcfxResolver resolver, List<Filter> filters) {
        this.resolver = resolver;
        this.filters = filters;
    }

    public RpcfxResponse invoke(RpcfxRequest request) {

        if (filters != null) {
            filters.stream().filter(Filter::isProvider).forEach(filter -> filter.filter(request));
        }
        RpcfxResponse response = new RpcfxResponse();
        String serviceClass = request.getServiceClass();

        // 作业1：改成泛型和反射
        Object service = resolver.resolve(serviceClass);//this.applicationContext.getBean(serviceClass);

        try {
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            // 两次json序列化能否合并成一个
            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            return response;
        } catch (IllegalAccessException | InvocationTargetException e) {

            // 3.Xstream

            // 2.封装一个统一的RpcfxException
            // 客户端也需要判断异常
            e.printStackTrace();
            response.setException(e);
            response.setStatus(false);
            return response;
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

}
