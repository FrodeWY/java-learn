package com.spring.aop.interceptor;

import com.spring.aop.advice.Advice;
import com.spring.aop.interceptor.invocation.MethodInvocation;
import java.lang.reflect.InvocationTargetException;

/**
 * @author wangyang
 * @ClassName MethodInterceptor
 * @Description TODO
 * @Date 2021/7/24 下午12:15
 * @Version 1.0
 */
public interface MyMethodInterceptor {

  Object invoke(MethodInvocation invocation) throws InvocationTargetException, IllegalAccessException;

  boolean isMatch(MethodInvocation invocation);
}
