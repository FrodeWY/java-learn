package com.spring.aop.interceptor;

import com.spring.aop.advice.Advice;
import com.spring.aop.interceptor.invocation.MethodInvocation;
import com.spring.aop.joinpoint.MyJoinPoint;
import java.lang.reflect.Method;

/**
 * @author wangyang
 * @ClassName AbstractMethodInterceptor
 * @Description TODO
 * @Date 2021/7/24 下午1:25
 * @Version 1.0
 */
public abstract class AbstractMyMethodInterceptor implements MyMethodInterceptor {

  protected Advice advice;

  public AbstractMyMethodInterceptor(Advice advice) {
    this.advice = advice;
  }

  @Override
  public boolean isMatch(MethodInvocation invocation) {
    MyJoinPoint myJoinPoint = invocation.currentJoinPoint();
    Method method = myJoinPoint.invokeMethod();
    return advice.methodMatch(method);
  }
}
