package com.activemq.consumer;

import com.activemq.conifg.ActivemqConfig;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.jms.Destination;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;

/**
 * @author wangyang
 * @ClassName TopicConsumer
 * @Description TODO
 * @Date 2021/9/10 下午10:38
 * @Version 1.0
 */
@Slf4j
public class TopicConsumer<T> extends AbstractConsumer {

  private final Class<T> clazz;
  private CompletableFuture<T> future;
  private Executor executor;
  private final Destination destination;

  public TopicConsumer(ActivemqConfig config, String topicName, Class<T> clazz, CompletableFuture<T> future, Executor executor) {
    super(config);
    this.destination = new ActiveMQTopic(topicName);
    this.executor = executor;
    this.clazz = clazz;
    this.future = future;
  }

  public TopicConsumer(ActivemqConfig config, String topicName, Class<T> clazz) {
    this(config, topicName, clazz, null, null);
  }

  public TopicConsumer(ActivemqConfig config, String topicName, Class<T> clazz, Executor executor) {
    this(config, topicName, clazz, null, executor);
  }

  public void startAsync() {
    if (executor == null) {
      this.defaultExecutor.execute(() -> super.start(destination, clazz, future));
      return;
    }
    executor.execute(() -> super.start(destination, clazz, future));
  }

  public void startSync() {
    super.start(destination, clazz, future);
  }
}
