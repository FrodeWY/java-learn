package com.rpc.rpcfx.demo.provider;

import com.rpc.core.api.RpcfxResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

public class SpringIOCResolver implements RpcfxResolver, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object resolve(String serviceClass) {
        try {
            final Class<?> aClass = Class.forName(serviceClass);
            return this.applicationContext.getBean(aClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
