package com.spring.learn.starter.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 将@CustomComponent 声明的类注册成bean
 * Enable模块装配
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CustomComponentRegistrar.class)
public @interface EnableCustomComponent {

    /**
     * 扫描类所在的 package
     *
     * @return
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * 扫描的包名
     *
     * @return
     */
    String[] basePackages() default {};
}
