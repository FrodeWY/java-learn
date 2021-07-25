package com.spring.aop.annotation;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Order
public @interface MyAspect {

  /**
   * 匹配类的正则表达式
   */
  String classRegex() default ".*";

  /**
   * 匹配方法的正则表达式
   */
  String methodRegex() default ".*";

  /**
   * 是否是前置通知
   */
  boolean isBefore() default false;

  /**
   * 是否是后置通知
   */
  boolean isAfter() default false;

  @AliasFor(annotation = Order.class, attribute = "value")
  int order() default Ordered.LOWEST_PRECEDENCE;
}
