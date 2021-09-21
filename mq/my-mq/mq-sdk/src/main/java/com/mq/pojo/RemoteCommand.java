package com.mq.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyang
 * @ClassName RemoteCommand
 * @Description TODO
 * @Date 2021/9/20 上午11:21
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoteCommand {

  /**
   * @see com.mq.pojo.enums.CommandCodeEnum
   */
  private Integer commandCode;
  /**
   * 请求id
   */
  private String requestId;

  /**
   * 消息体
   */
  private String body;


}
