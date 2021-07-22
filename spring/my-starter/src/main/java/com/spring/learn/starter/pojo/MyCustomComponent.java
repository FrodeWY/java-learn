package com.spring.learn.starter.pojo;

import com.spring.learn.starter.annotation.MyComponent;

@MyComponent//自定义注解继承@component的方式装配Bean
public class MyCustomComponent {

    public void print(){
        System.out.println("hello world");
    }
}
