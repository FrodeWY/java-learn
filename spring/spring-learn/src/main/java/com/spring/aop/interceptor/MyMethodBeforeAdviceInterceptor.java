package com.spring.aop.interceptor;

import com.spring.aop.advice.Advice;
import com.spring.aop.interceptor.invocation.MethodInvocation;
import java.lang.reflect.InvocationTargetException;

public class MyMethodBeforeAdviceInterceptor extends AbstractMyMethodInterceptor {

  public MyMethodBeforeAdviceInterceptor(Advice advice) {
    super(advice);
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws InvocationTargetException, IllegalAccessException {
    advice.invoke(invocation.currentJoinPoint());
    return invocation.process();
  }


}


