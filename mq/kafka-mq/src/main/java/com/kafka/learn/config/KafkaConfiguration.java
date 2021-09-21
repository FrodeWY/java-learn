package com.kafka.learn.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author wangyang
 * @ClassName KafkaConfiguration
 * @Description TODO
 * @Date 2021/9/16 下午10:17
 * @Version 1.0
 */
@Configuration
public class KafkaConfiguration {

  @Bean
  public NewTopic topic() {
    return TopicBuilder.name("topic1")
        .partitions(8)
        .replicas(3)
        .build();
  }


}
