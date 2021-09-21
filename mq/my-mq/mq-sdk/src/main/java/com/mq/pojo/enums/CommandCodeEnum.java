package com.mq.pojo.enums;

/**
 * 命令编码
 */
public enum CommandCodeEnum {
  PULL_MESSAGE_REQUEST(1),
  PULL_MESSAGE_RESPONSE(2),
  SEND_MESSAGE_REQUEST(3),
  SEND_MESSAGE_RESPONSE(4),
  GET_CONSUME_GROUP_OFFSET_REQUEST(5),
  GET_CONSUME_GROUP_OFFSET_RESPONSE(6);

  private Integer code;

  public Integer getCode() {
    return code;
  }

  CommandCodeEnum(Integer code) {
    this.code = code;
  }
}
