package com.mq.producer;

import com.alibaba.fastjson.JSON;
import com.mq.api.MqClientApi;
import com.mq.client.Client;
import com.mq.client.NettyClient;
import com.mq.pojo.DefaultFuture;
import com.mq.pojo.message.Message;
import com.mq.pojo.response.SendMessageResponse;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangyang
 * @ClassName DefaultProducer
 * @Description TODO
 * @Date 2021/9/21 上午12:31
 * @Version 1.0
 */
@Data
@Slf4j
public class DefaultProducer implements Producer {

  private MqClientApi clientApi;
  private Client client;
  private final String brokerAddr;
  private final Integer port;

  public DefaultProducer(String brokerAddr, Integer port) {
    this.brokerAddr = brokerAddr;
    this.port = port;
  }

  @Override
  public SendMessageResponse send(List<Message> messages, String topic) {
    try {
      DefaultFuture defaultFuture = clientApi.sendMessage(messages, topic);
      SendMessageResponse response = (SendMessageResponse) defaultFuture.get();
      log.info("send response:{}", JSON.toJSONString(response));
      return response;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void start() {
    if (client == null) {
      client = new NettyClient();
    }
    try {
      client.start(brokerAddr, port);
      clientApi = new MqClientApi(client);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void stop() {
    client.close();
  }
}
