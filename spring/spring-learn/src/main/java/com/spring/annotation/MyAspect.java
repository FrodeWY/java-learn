package com.spring.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MyAspect {
    /**
     * 切点
     */
    String pointcut() default "";

    /**
     * 前置通知
     */
    String before();

    /**
     * 后置通知
     */
    String after();
}
