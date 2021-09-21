package com.mq.pojo.response;

import com.mq.pojo.message.Message;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyang
 * @ClassName PullMessageResponse
 * @Description 拉取消息响应体
 * @Date 2021/9/17 下午10:02
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PullMessageResponse {

  /**
   * 消息列表
   */
  private List<Message> messageList;
  /**
   * 当前topic最大偏移量
   */
  private Integer maxOffset;
  /**
   * 下次消费起始 offset
   */
  private Integer nextBeginOffset;
  /**
   * 主题
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
