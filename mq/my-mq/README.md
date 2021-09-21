# 简单的MQ实现

## 如何使用
- 启动broker
```java
    NettyServer server=new NettyServer(8080);
    server.start();
```

- 启动生产者

```java
    DefaultProducer producer=new DefaultProducer("localhost",8080);
    producer.start();
    List<Message> messageList=new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      Message message = new Message("message"+i);
      messageList.add(message);
    }
    producer.send(messageList,"test-topic");
    producer.stop();
```

- 启动消费者

  - 集群模式

    ```java
     DefaultConsumer consumer=new DefaultConsumer("consumer-group-01");
        consumer.setBatchSize(10);
        consumer.setBrokerAddr("localhost");
        consumer.setBrokerPort(8080);
        consumer.setConsumeModelEnum(ConsumeModelEnum.CLUSTERING);
        consumer.setListener(new ConsumeListener() {
          @Override
          public ConsumeMessageStatus consumerMessage(PullMessageResponse response) {
            String toJSONString = JSON.toJSONString(response);
            System.out.println("receive response"+toJSONString);
            return ConsumeMessageStatus.CONSUME_SUCCESS;
          }
        });
        consumer.setTopic("test-topic");
        consumer.start();
    ```

  - 广播模式

    ```java
        DefaultConsumer consumer=new DefaultConsumer("consumer-group-01");
        consumer.setBatchSize(10);
        consumer.setOffset(1);
        consumer.setBrokerAddr("localhost");
        consumer.setBrokerPort(8080);
        consumer.setConsumeModelEnum(ConsumeModelEnum.BROADCASTING);
        consumer.setListener(new ConsumeListener() {
          @Override
          public ConsumeMessageStatus consumerMessage(PullMessageResponse response) {
            String toJSONString = JSON.toJSONString(response);
            System.out.println("receive response"+toJSONString);
            return ConsumeMessageStatus.CONSUME_SUCCESS;
          }
        });
        consumer.setTopic("test-topic");
        consumer.start();
      }
    ```

    

## 支持特性

- 目前只实现了基于内存的存储,最大能存储1<<20 个元素,超过最大数量使用淘汰最老的数据
- 支持集群消费和广播消费,broker 维护了各个消费组的offset
- 支持消费回调
- 广播模式下支持消费失败后重发
- 支持拉取远程消费组offset信息
- 网络通信层使用netty