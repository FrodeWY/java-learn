package com.exchange.dto.request;

import java.io.Serializable;
import lombok.Data;

/**
 * @author wangyang
 * @ClassName ExchangeRequestDTO
 * @Description 外汇交易请求体
 * @Date 2021/8/22 上午11:37
 * @Version 1.0
 */
@Data
public class ExchangeRequestDTO implements Serializable {

  /**
   * 交易甲方
   */
  private Long fromUserId;
  /**
   * 交易乙方
   */
  private Long toUserId;

  /**
   * 转出账户类型
   */
  Integer fromAccountType;
  /**
   * 转入账户类型
   */
  Integer toAccountType;
  /**
   * 转出金额
   */
  String fromAmount;
  /**
   * 转入金额
   */
  String toAmount;

  /**
   * 交易流水号
   */
  String transactionNo;
}
