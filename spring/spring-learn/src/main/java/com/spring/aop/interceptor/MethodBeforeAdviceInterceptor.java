package com.spring.aop.interceptor;

import com.spring.aop.advice.Advice;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MethodBeforeAdviceInterceptor implements MethodInterceptor {
    private Advice advice;
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        advice.invoke(objects);
        return method.invoke(o,objects);
    }
}
