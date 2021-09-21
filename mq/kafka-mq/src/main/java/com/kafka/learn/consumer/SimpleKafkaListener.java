package com.kafka.learn.consumer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author wangyang
 * @ClassName SimpleKafkaListener
 * @Description TODO
 * @Date 2021/9/16 下午10:34
 * @Version 1.0
 */
@Component
@Slf4j
public class SimpleKafkaListener {

  @KafkaListener(topics = {"topic1"}, groupId = "testGroup")
  public void process(List<String> list, Acknowledgment ack) {
    log.info("本次批量拉取数量:" + list.size() + " 开始消费....");
    List<String> messageList = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
    for (String message : messageList) {
      log.info("消费消息:{}", message);
    }
    ack.acknowledge();
  }
}
