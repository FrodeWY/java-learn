package com.spring.aop.interceptor.invocation;

import com.spring.aop.joinpoint.MyJoinPoint;
import java.lang.reflect.InvocationTargetException;

/**
 * @author wangyang
 * @ClassName MethodInvocation
 * @Description TODO
 * @Date 2021/7/24 下午12:16
 * @Version 1.0
 */
public interface MethodInvocation {

  ThreadLocal<MyJoinPoint> THREAD_LOCAL = new InheritableThreadLocal<>();

  Object process() throws InvocationTargetException, IllegalAccessException;

  MyJoinPoint currentJoinPoint();

  void initJoinPoint(MyJoinPoint myJoinPoint);
}
