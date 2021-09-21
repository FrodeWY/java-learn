package com.mq.producer;

import com.mq.pojo.message.Message;
import com.mq.pojo.response.SendMessageResponse;
import java.util.List;

/**
 * @author wangyang
 * @ClassName Producer
 * @Description TODO
 * @Date 2021/9/21 上午12:26
 * @Version 1.0
 */
public interface Producer {

  SendMessageResponse send(List<Message> messages, String topic);

  void start();

  void stop();
}
