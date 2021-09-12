package com.activemq;

import com.activemq.common.MyMessage;
import com.activemq.conifg.ActivemqConfig;
import com.activemq.consumer.QueueConsumer;
import com.activemq.producer.QueueProducer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author wangyang
 * @ClassName ActiveMqApplication
 * @Description TODO
 * @Date 2021/9/10 下午11:53
 * @Version 1.0
 */
@SpringBootApplication
public class ActiveMqApplication {

  public static void main(String[] args) throws Exception {
    ConfigurableApplicationContext context = new SpringApplicationBuilder(ActiveMqApplication.class).web(WebApplicationType.NONE).run(args);
    int core = Runtime.getRuntime().availableProcessors() + 1;
    ThreadPoolExecutor consumerExecutor = new ThreadPoolExecutor(core, 2 * core, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
    ThreadPoolExecutor producerExecutor = new ThreadPoolExecutor(core, 2 * core, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
    ActivemqConfig activemqConfig = new ActivemqConfig();
    activemqConfig.setBrokerUrl("tcp://127.0.0.1:61616");
//    TopicConsumer<MyMessage> topicConsumer = new TopicConsumer<>(activemqConfig, "mytopic", MyMessage.class,consumerExecutor);
//    topicConsumer.startAsync();
//    TopicConsumer<MyMessage> topicConsumer2 = new TopicConsumer<>(activemqConfig, "mytopic", MyMessage.class,consumerExecutor);
//    topicConsumer2.startAsync();

    QueueConsumer<MyMessage> queueConsumer = new QueueConsumer<>(activemqConfig, "myqueue", MyMessage.class, consumerExecutor);
    queueConsumer.startAsync();
//    QueueConsumer<MyMessage> queueConsumer2 = new QueueConsumer<>(activemqConfig, "myqueue", MyMessage.class, consumerExecutor);
//    queueConsumer2.startAsync();
//    TopicProducer<MyMessage> topicProducer=new TopicProducer<>(activemqConfig,"mytopic",producerExecutor);
    QueueProducer<MyMessage> queueProducer = new QueueProducer<>(activemqConfig, "myqueue", producerExecutor);
    for (int i = 0; i < 10; i++) {
      MyMessage myMessage = new MyMessage("name" + i, i, "myMessage" + i);
//      topicProducer.sendSync(myMessage);
      queueProducer.sendSync(myMessage);
    }

    Thread.sleep(10000);
    queueConsumer.close();
    queueProducer.close();
    consumerExecutor.shutdown();
    producerExecutor.shutdown();
  }
}
