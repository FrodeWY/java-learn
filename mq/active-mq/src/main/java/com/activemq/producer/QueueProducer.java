package com.activemq.producer;

import com.activemq.conifg.ActivemqConfig;
import java.util.concurrent.Executor;
import javax.jms.Destination;
import org.apache.activemq.command.ActiveMQQueue;

/**
 * @author wangyang
 * @ClassName QueueProducer
 * @Description TODO
 * @Date 2021/9/10 下午11:44
 * @Version 1.0
 */
public class QueueProducer<T> extends AbstractProducer {

  private final Destination destination;

  private Executor executor;

  public QueueProducer(ActivemqConfig config, String queueName, Executor executor) {
    super(config);
    this.destination = new ActiveMQQueue(queueName);
    this.executor = executor;
  }

  public QueueProducer(ActivemqConfig config, String queueName) {
    super(config);
    destination = new ActiveMQQueue(queueName);
  }

  public void sendSync(T message) {
    super.send(destination, message);
  }

  public void sendAsync(T message) {
    if (executor == null) {
      this.defaultExecutor.execute(() -> send(destination, message));
      return;
    }
    executor.execute(() -> send(destination, message));
  }
}
