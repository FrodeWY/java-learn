package com.learn.starter.pojo;

import com.learn.starter.annotation.CustomComponent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

@CustomComponent  //通过扫描自定义注解装配Bean
public class School {
    @Autowired
    Klass class1;

    @Resource(name = "studentOne")
    Student student100;


    public Klass getClass1() {
        return class1;
    }

    public void setClass1(Klass class1) {
        this.class1 = class1;
    }

    public Student getStudent100() {
        return student100;
    }

    public void setStudent100(Student student100) {
        this.student100 = student100;
    }
}
