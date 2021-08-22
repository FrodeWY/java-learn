package com.account.dto.request;

import java.io.Serializable;
import lombok.Data;

/**
 * @author wangyang
 * @ClassName PersonalAccountExchangeRequestDTO
 * @Description TODO
 * @Date 2021/8/21 下午11:24
 * @Version 1.0
 */
@Data
public class PersonalAccountExchangeRequestDTO implements Serializable {

  /**
   * 用户id
   */
  Long userId;
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
