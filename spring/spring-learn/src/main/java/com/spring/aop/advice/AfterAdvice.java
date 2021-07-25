package com.spring.aop.advice;

import com.sun.xml.internal.rngom.parse.host.Base;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author wangyang
 * @ClassName AfterAdvice
 * @Description 后置通知
 * @Date 2021/7/24 上午11:42
 * @Version 1.0
 */
public class AfterAdvice extends BaseAdvice {

  public AfterAdvice(Method method, Object instance, Pattern methodPattern, Pattern classPattern, int order) {
    super(method, instance, methodPattern, classPattern, order);
  }

  @Override
  public boolean isAfter() {
    return true;
  }
}
