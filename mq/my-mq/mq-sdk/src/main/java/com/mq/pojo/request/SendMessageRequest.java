package com.mq.pojo.request;

import com.mq.pojo.message.Message;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyang
 * @ClassName SendMessageRequest
 * @Description 发送消息请求体
 * @Date 2021/9/17 下午10:00
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {

  private String topic;
  private List<Message> messageList;


}
