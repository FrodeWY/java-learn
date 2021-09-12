package com.activemq.producer;

import com.activemq.conifg.ActivemqConfig;
import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author wangyang
 * @ClassName AbstractProducer
 * @Description TODO
 * @Date 2021/9/10 下午11:34
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractProducer implements Producer {

  private ActiveMQConnection conn;
  private Session session;
  private final ActivemqConfig config;
  protected final Executor defaultExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
      2 * Runtime.getRuntime().availableProcessors(), 60,
      TimeUnit.SECONDS, new ArrayBlockingQueue(1000));

  public AbstractProducer(ActivemqConfig config) {
    this.config = config;
  }

  @Override
  public <T> void send(Destination destination, T message) {
    log.info("send message:{}", message);
    try {
      if (session == null || conn == null) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(config.getBrokerUrl());
        conn = (ActiveMQConnection) factory.createConnection();
        conn.start();
        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
      }
      MessageProducer producer = session.createProducer(destination);
      Message mqMessage = session.createObjectMessage((Serializable) message);
      producer.send(mqMessage);
    } catch (JMSException e) {
      e.printStackTrace();
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
