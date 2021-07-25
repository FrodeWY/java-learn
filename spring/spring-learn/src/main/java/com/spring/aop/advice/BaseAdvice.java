package com.spring.aop.advice;

import com.spring.aop.joinpoint.MyJoinPoint;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 基础通知
 */
public class BaseAdvice implements Advice {

  /**
   * 通知方法
   */
  private Method method;
  /**
   * 切面对象实例
   */
  private Object instance;
  /**
   * 连接点匹配
   */
  private Pattern methodPattern;
  /**
   * 类匹配器
   */
  private Pattern classPattern;
  /**
   * 执行优先级
   */
  private int order;

  public BaseAdvice(Method method, Object instance, Pattern methodPattern, Pattern classPattern, int order) {
    this.method = method;
    this.instance = instance;
    this.methodPattern = methodPattern;
    this.order = order;
    this.classPattern = classPattern;
  }

  @Override
  public void invoke(MyJoinPoint myJoinPoint) throws InvocationTargetException, IllegalAccessException {
    method.invoke(instance, myJoinPoint);
  }

  @Override
  public boolean methodMatch(Method method) {
    return methodPattern.matcher(method.getName()).matches();
  }

  @Override
  public boolean classMatch(Class<?> target) {
    return classPattern.matcher(target.getName()).matches();
  }

  @Override
  public boolean isBefore() {
    return false;
  }

  @Override
  public boolean isAfter() {
    return false;
  }

  @Override
  public boolean isAround() {
    return false;
  }


  @Override
  public int getOrder() {
    return order;
  }
}
