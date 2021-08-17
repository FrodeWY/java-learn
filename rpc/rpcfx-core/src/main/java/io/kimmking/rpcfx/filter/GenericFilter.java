package io.kimmking.rpcfx.filter;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.Filter;
import io.kimmking.rpcfx.api.RpcfxRequest;
import org.springframework.stereotype.Component;

/**
 * 泛型调用过滤器,这里默认参数的序列化是通过fastJson进行的,所以反序列化也通过fastJson
 */
@Component
public class GenericFilter implements Filter {
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
        for (int i = 0; i < params.length; i++) {
            final String parameterType = parameterTypes[i];
            params[i]=arg(parameterType,params[i]);
        }
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
