package com.rpc.core.filter;

import com.alibaba.fastjson.JSON;
import com.rpc.core.api.Filter;
import com.rpc.core.common.RpcfxRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 泛型调用过滤器
 */
@Slf4j
public class GenericInBoundFilter implements Filter {

    @Override
    public void filter(RpcfxRequest request) {
        if (!request.getGeneric()) {
            return;
        }

        final String[] parameterTypes = request.getParameterTypes();
        final Object[] params = request.getParams();
        if (parameterTypes.length != params.length) {
            throw new IllegalArgumentException("parameter type length must equal parameter length");
        }
        log.info("generic invoke:{}", request.getServiceClass());
//        for (int i = 0; i < params.length; i++) {
//
//            final String parameterType = parameterTypes[i];
//            params[i]=arg(parameterType,params[i]);
//        }
    }

    @Override
    public boolean isProvider() {
        return true;
    }

    private static Object arg(String parameterType, Object parameter) {
        final Class<?> parameterTypeClass;
        try {
            parameterTypeClass = Class.forName(parameterType);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("parameter type:" + parameterType + " not found", e);
        }
        if (parameterTypeClass.isPrimitive()) {
            return parameter;
        }

        return JSON.parseObject((String) parameter, parameterTypeClass);
    }
}
