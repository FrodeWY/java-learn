package com.activemq.producer;

import com.activemq.conifg.ActivemqConfig;
import java.util.concurrent.Executor;
import javax.jms.Destination;
import org.apache.activemq.command.ActiveMQTopic;

/**
 * @author wangyang
 * @ClassName TopicProducer
 * @Description TODO
 * @Date 2021/9/10 下午11:32
 * @Version 1.0
 */
public class TopicProducer<T> extends AbstractProducer {

  private final Destination destination;
  private Executor executor;

  public TopicProducer(ActivemqConfig config, String topicName) {
    super(config);
    destination = new ActiveMQTopic(topicName);
  }

  public TopicProducer(ActivemqConfig config, String topicName, Executor executor) {
    super(config);
    destination = new ActiveMQTopic(topicName);
    this.executor = executor;
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
