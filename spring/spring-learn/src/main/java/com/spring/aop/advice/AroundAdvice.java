package com.spring.aop.advice;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author wangyang
 * @ClassName BeforeAdvice
 * @Description 前置通知
 * @Date 2021/7/24 上午11:41
 * @Version 1.0
 */
public class AroundAdvice extends BaseAdvice {

  public AroundAdvice(Method method, Object instance, Pattern methodPattern, Pattern classPattern, int order) {
    super(method, instance, methodPattern, classPattern, order);
  }

  @Override
  public boolean isAround() {
    return true;
  }
}
