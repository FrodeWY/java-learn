package com.activemq.consumer;

import com.activemq.conifg.ActivemqConfig;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.jms.Destination;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.util.StringUtils;

/**
 * @author wangyang
 * @ClassName TopicConsumer
 * @Description TODO
 * @Date 2021/9/10 下午10:38
 * @Version 1.0
 */
@Slf4j
public class QueueConsumer<T> extends AbstractConsumer {

  private final String queueName;
  private final Class<T> clazz;
  private CompletableFuture<T> future;
  private Executor executor;

  public QueueConsumer(ActivemqConfig config, String queueName, Class<T> clazz, CompletableFuture<T> future, Executor executor) {
    super(config);
    this.queueName = queueName;
    this.clazz = clazz;
    this.executor = executor;
    this.future = future;
  }

  public QueueConsumer(ActivemqConfig config, String queueName, Class<T> clazz) {
    this(config, queueName, clazz, null, null);
  }

  public QueueConsumer(ActivemqConfig config, String queueName, Class<T> clazz, Executor executor) {
    this(config, queueName, clazz, null, executor);
  }

  public void startSync() {
    if (!StringUtils.hasText(queueName)) {
      log.error("queueName is empty");
      return;
    }
    Destination destination = new ActiveMQQueue(queueName);
    super.start(destination, clazz, future);
  }

  public void startAsync() {
    if (!StringUtils.hasText(queueName)) {
      log.error("queueName is empty");
      return;
    }
    Destination destination = new ActiveMQQueue(queueName);
    if (executor == null) {
      this.defaultExecutor.execute(() -> super.start(destination, clazz, future));
      return;
    }
    executor.execute(() -> super.start(destination, clazz, future));
  }
}
