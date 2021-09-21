package com.kafka.learn;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @author wangyang
 * @ClassName KafkaApplication
 * @Description TODO
 * @Date 2021/9/16 下午10:22
 * @Version 1.0
 */
@EnableKafka
@SpringBootApplication
public class KafkaApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = new SpringApplicationBuilder(KafkaApplication.class).web(WebApplicationType.NONE).run(args);

  }
}
