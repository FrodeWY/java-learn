package com.exchange.controller;

import com.exchange.dto.request.ExchangeRequestDTO;
import com.exchange.service.ForeignExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangyang
 * @ClassName ForeignExchangeController
 * @Description TODO
 * @Date 2021/8/22 上午11:34
 * @Version 1.0
 */
@RestController("/")
public class ForeignExchangeController {

  @Autowired
  private ForeignExchangeService foreignExchangeService;

  /**
   * 外汇交易
   */
  @PostMapping("exchange")
  public void exchange(@RequestBody ExchangeRequestDTO exchangeRequestDTO) {
    foreignExchangeService.exchange(exchangeRequestDTO);
  }
}
