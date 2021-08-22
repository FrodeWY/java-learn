package com.account.service;

import com.account.dto.request.PersonalAccountExchangeRequestDTO;

/**
 * 账户外汇交易服务
 */
public interface AccountExchangeTransactionsService {

  /**
   * 用户不同账户金额交换-->冻结资产
   *
   * @return true:成功  false:失败
   */
  boolean personalAccountExchange(PersonalAccountExchangeRequestDTO requestDTO);

  /**
   * 用户不同账户金额交换--->使用冻结资产真正的完成金额交换
   */
  boolean personalAccountExchangeConfirm(PersonalAccountExchangeRequestDTO requestDTO);

  /**
   * 用户不同账户金额交换--->解除冻结资产
   */
  boolean personalAccountExchangeConfirmCancel(PersonalAccountExchangeRequestDTO requestDTO);
}
