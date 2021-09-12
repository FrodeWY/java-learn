package com.activemq.consumer;

import com.activemq.conifg.ActivemqConfig;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;

/**
 * @author wangyang
 * @ClassName AbstractConsumer
 * @Description TODO
 * @Date 2021/9/10 下午11:09
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractConsumer implements Consumer {

  private final ActivemqConfig config;
  private ActiveMQConnection conn;
  private Session session;
  private final AtomicBoolean started = new AtomicBoolean(false);
  protected final Executor defaultExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
      2 * Runtime.getRuntime().availableProcessors(), 60,
      TimeUnit.SECONDS, new ArrayBlockingQueue(1000));

  public AbstractConsumer(ActivemqConfig config) {
    this.config = config;
  }


  @Override
  public <T> void start(Destination destination, Class<T> clazz, CompletableFuture<T> future) {
    if (started.compareAndSet(false, true)) {
      log.info("start consumer success");
      try {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(config.getBrokerUrl());
        conn = (ActiveMQConnection) factory.createConnection();
        conn.start();
        conn.setTrustAllPackages(true);
        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(destination);
        MessageListener messageListener = message -> {
          try {
            ActiveMQObjectMessage mqObjectMessage = (ActiveMQObjectMessage) message;
            T body = (T) mqObjectMessage.getObject();
            log.info("receive content:{}", body);
            Optional.ofNullable(future).ifPresent(f -> f.complete(body));
          } catch (JMSException e) {
            log.error(e.getMessage());
          }
        };
        consumer.setMessageListener(messageListener);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void close() {
    try {
      if (session != null) {
        session.close();
      }
      if (conn != null) {
        conn.close();
      }
    } catch (JMSException e) {
      e.printStackTrace();
    }
  }
}
