package com.learn.starter.pojo;

import com.learn.starter.annotation.MyComponent;

@MyComponent//自定义注解继承@component的方式装配Bean
public class MyCustomComponent {

    public void print(){
        System.out.println("hello world");
    }
}
