package com.spring;

import com.spring.learn.starter.annotation.EnableCustomComponent;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
@EnableCustomComponent
public class Bootstrap {
    public static void main(String[] args) throws IOException {
        final ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(Bootstrap.class).web(WebApplicationType.NONE).run(args);
        System.in.read();
        applicationContext.close();
    }
}
