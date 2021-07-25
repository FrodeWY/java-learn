package com.spring.aop.advice;

import com.spring.aop.joinpoint.MyJoinPoint;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.core.Ordered;

public interface Advice extends Ordered {

  void invoke(MyJoinPoint myJoinPoint) throws InvocationTargetException, IllegalAccessException;

  boolean methodMatch(Method method);

  boolean classMatch(Class<?> target);

  boolean isBefore();

  boolean isAfter();

  boolean isAround();
}
