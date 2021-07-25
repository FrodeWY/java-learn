package com.spring.aop.interceptor;

import com.spring.aop.advice.Advice;
import com.spring.aop.interceptor.invocation.MethodInvocation;
import com.spring.aop.joinpoint.MyJoinPoint;
import java.lang.reflect.InvocationTargetException;

public class MyMethodAroundAdviceInterceptor extends AbstractMyMethodInterceptor {

  public MyMethodAroundAdviceInterceptor(Advice advice) {
    super(advice);
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws InvocationTargetException, IllegalAccessException {
    MyJoinPoint myJoinPoint = invocation.currentJoinPoint();
    advice.invoke(myJoinPoint);
    try {
      return invocation.process();
    } finally {
      advice.invoke(myJoinPoint);
    }
  }


}


