package com.spring.aop.proxy;

import com.spring.aop.advice.Advice;
import com.spring.aop.interceptor.MyMethodInterceptor;
import com.spring.aop.interceptor.invocation.AopMethodInvocation;
import com.spring.aop.joinpoint.MyJoinPoint;
import com.spring.aop.joinpoint.MyJoinPointImpl;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.List;

public class ByteBuddyAopProxy implements AopProxyFactory {
    private Object target;
    private List<MyMethodInterceptor> myMethodInterceptorList;

    public ByteBuddyAopProxy(Object target, List<Advice> adviceList) {
        this.target = target;
        myMethodInterceptorList = convertMethodInterceptorList(adviceList);
    }

    @Override
    public Object getProxy() throws IllegalAccessException, InstantiationException {
        final Class<?> dynamicProxyClass = new ByteBuddy().subclass(target.getClass())
                .method(ElementMatchers.any())
                .intercept(MethodDelegation.to(this))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded();

        return dynamicProxyClass.newInstance();
    }

    @RuntimeType
    public Object process(@Origin Method method,  @AllArguments Object[] args) throws Exception {
        MyJoinPoint myJoinPoint = new MyJoinPointImpl(method, args, method.getParameterTypes());
        AopMethodInvocation aopMethodInvocation = new AopMethodInvocation(myMethodInterceptorList, target, args, method);
        aopMethodInvocation.initJoinPoint(myJoinPoint);
        return aopMethodInvocation.process();
    }
}
