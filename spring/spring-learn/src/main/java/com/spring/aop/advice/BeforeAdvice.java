package com.spring.aop.advice;

import java.lang.reflect.Method;

public class BeforeAdvice implements Advice{

    private Method method;
    private Object instance;

    public BeforeAdvice(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
    }

    @Override
    public void invoke(Object[] args) throws Exception {
        method.invoke(instance,args);
    }
}
