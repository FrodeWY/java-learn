package com.spring.aop.interceptor;

import com.spring.aop.advice.Advice;
import com.spring.aop.interceptor.invocation.MethodInvocation;
import java.lang.reflect.InvocationTargetException;


public class MyMethodAfterAdviceInterceptor extends AbstractMyMethodInterceptor {

  public MyMethodAfterAdviceInterceptor(Advice advice) {
    super(advice);
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws InvocationTargetException, IllegalAccessException {
    try {
      return invocation.process();
    } finally {
      advice.invoke(invocation.currentJoinPoint());
    }
  }
}
