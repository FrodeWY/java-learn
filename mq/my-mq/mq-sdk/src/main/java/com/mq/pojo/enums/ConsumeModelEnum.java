package com.mq.pojo.enums;

/**
 * 消费模式
 */
public enum ConsumeModelEnum {
  /**
   * 广播
   */
  BROADCASTING(1),
  /**
   * 集群消费模式
   */
  CLUSTERING(2);

  private Integer code;

  public Integer getCode() {
    return code;
  }

  public static ConsumeModelEnum getByValue(Integer code) {
    for (ConsumeModelEnum value : ConsumeModelEnum.values()) {
      if (value.getCode().equals(code)) {
        return value;
      }
    }
    return null;
  }

  ConsumeModelEnum(Integer code) {
    this.code = code;
  }
}
