package com.kafka.learn.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author wangyang
 * @ClassName SimpleKafkaProducer
 * @Description TODO
 * @Date 2021/9/16 下午10:44
 * @Version 1.0
 */
@Component
public class SimpleKafkaProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public SimpleKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMessage() {
    for (int i = 0; i < 10; i++) {
      kafkaTemplate.send("topic1", "message:" + i);
    }
  }
}
