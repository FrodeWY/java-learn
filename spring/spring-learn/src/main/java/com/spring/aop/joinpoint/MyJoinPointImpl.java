package com.spring.aop.joinpoint;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author wangyang
 * @ClassName JoinPointImpl
 * @Description 连接点实现
 * @Date 2021/7/24 上午11:57
 * @Version 1.0
 */
public class MyJoinPointImpl implements MyJoinPoint {

  private Method method;
  private Object[] args;
  private Class[] argsTypes;
  private Object returnValue;
  private String uniqueKey;
  private boolean isProcessed;

  @Override
  public String uniqueKey() {
    return uniqueKey;
  }

  public MyJoinPointImpl(Method method, Object[] args, Class[] argsTypes) {
    this.method = method;
    this.args = args;
    this.argsTypes = argsTypes;
    this.uniqueKey = UUID.randomUUID().toString();
  }

  @Override
  public boolean isProcessed() {
    return isProcessed;
  }

  @Override
  public void end() {
    isProcessed = true;
  }

  @Override
  public void setReturnValue(Object returnValue) {
    this.returnValue = returnValue;
  }


  @Override
  public Method invokeMethod() {
    return method;
  }

  @Override
  public Object returnValue() {
    return returnValue;
  }

  @Override
  public Class returnType() {
    return method.getReturnType();
  }

  @Override
  public Object[] args() {
    return args;
  }

  @Override
  public Class[] argsTypes() {
    return argsTypes;
  }
}
