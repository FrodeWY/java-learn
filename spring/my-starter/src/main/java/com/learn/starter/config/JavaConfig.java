package com.learn.starter.config;

import com.learn.starter.pojo.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(value = "classpath:applicationContext.xml")//xml 方式 装配Bean
public class JavaConfig {

    //java config方式 装配Bean
    @Bean(name = "studentOne")
    public Student student() {
        return new Student(1, "one");
    }
}
