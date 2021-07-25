package com.spring;

import com.learn.starter.annotation.EnableCustomComponent;
import com.learn.starter.pojo.MyCustomComponent;
import com.learn.starter.pojo.Student;
import com.spring.service.ReceiveService;
import com.spring.service.SendService;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableCustomComponent
public class Bootstrap {
    public static void main(String[] args) throws IOException {
        final ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(Bootstrap.class).web(WebApplicationType.NONE)
            .run(args);
        SendService sendService = applicationContext.getBean(SendService.class);
        sendService.send("林生");
        sendService.send2("莉莉");

        ReceiveService receiveService = applicationContext.getBean(ReceiveService.class);
        receiveService.receive("啦啦");
        receiveService.ack();
        System.out.println("************************");
        MyCustomComponent myCustomComponent = applicationContext.getBean("myCustomComponent", MyCustomComponent.class);
        myCustomComponent.print();
        System.in.read();
        applicationContext.close();
    }
}
