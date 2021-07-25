package com.learn.starter.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 自定义注解继承@component ,同样能实现Bean的装配
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MyComponent {
}
