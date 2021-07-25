package com.spring.aop.joinpoint;

import java.lang.reflect.Method;

/**
 * @author wangyang
 * @ClassName Joinpoint
 * @Description TODO
 * @Date 2021/7/24 上午11:55
 * @Version 1.0
 */
public interface MyJoinPoint {

  /**
   * 唯一标识
   */
  String uniqueKey();

  /**
   * 连接点方法
   */
  Method invokeMethod();

  /**
   * 返回值
   */
  Object returnValue();

  /**
   * 返回类型
   */
  Class returnType();

  /**
   * 参数数组
   */
  Object[] args();

  /**
   * 参数类型数组
   */
  Class[] argsTypes();

  /**
   * 设置返回值
   */
  void setReturnValue(Object returnValue);

  /**
   * 连接点方法是否已经执行完毕
   */
  boolean isProcessed();

  /**
   * 连接点方法执行完毕后调用
   */
  void end();
}
