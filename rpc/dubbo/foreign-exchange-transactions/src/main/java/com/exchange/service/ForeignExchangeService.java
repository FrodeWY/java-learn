package com.exchange.service;

import com.exchange.dto.request.ExchangeRequestDTO;

public interface ForeignExchangeService {

  /**
   * 外汇交易
   */
  boolean exchange(ExchangeRequestDTO requestDTO);
}
