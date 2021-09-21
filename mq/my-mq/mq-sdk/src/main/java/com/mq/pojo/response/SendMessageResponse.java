package com.mq.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyang
 * @ClassName SendMessageResult
 * @Description 发送消息响应体
 * @Date 2021/9/17 下午10:01
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMessageResponse {


  /**
   * 当前主题最大offset
   */
  private Integer maxOffset;
  /**
   * 消息主题
   */
  private String topic;
  /**
   * 响应编码
   *
   * @see com.mq.pojo.enums.ResponseCodeEnum
   */
  private Integer code;

  /**
   * 错误信息
   */
  private String errorMessage;

}
