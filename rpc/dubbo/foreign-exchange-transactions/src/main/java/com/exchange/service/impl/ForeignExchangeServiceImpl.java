package com.exchange.service.impl;

import com.account.dto.request.PersonalAccountExchangeRequestDTO;
import com.account.service.AccountExchangeTransactionsService;
import com.exchange.dto.request.ExchangeRequestDTO;
import com.exchange.service.ForeignExchangeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.stereotype.Service;

/**
 * @author wangyang
 * @ClassName ForeignExchangeServiceImpl
 * @Description TODO
 * @Date 2021/8/22 上午11:42
 * @Version 1.0
 */
@Service
public class ForeignExchangeServiceImpl implements ForeignExchangeService {

  @DubboReference(version = "1.0.0", group = "account")
  private AccountExchangeTransactionsService accountExchangeTransactionsService;

  @Override
  @HmilyTCC(confirmMethod = "exchangeConfirm", cancelMethod = "exchangeCancel")
  public boolean exchange(ExchangeRequestDTO requestDTO) {
    PersonalAccountExchangeRequestDTO personalExchangeFrom = getPersonalExchangeRequestDTO(requestDTO, requestDTO.getFromUserId(),
        requestDTO.getFromAccountType(), requestDTO.getToAccountType(), requestDTO.getFromAmount(), requestDTO.getToAmount());

    PersonalAccountExchangeRequestDTO personalExchangeTo = getPersonalExchangeRequestDTO(requestDTO, requestDTO.getToUserId(),
        requestDTO.getToAccountType(), requestDTO.getFromAccountType(), requestDTO.getToAmount(), requestDTO.getFromAmount());

    boolean result1 = accountExchangeTransactionsService.personalAccountExchange(personalExchangeFrom);
    boolean result2 = accountExchangeTransactionsService.personalAccountExchange(personalExchangeTo);
    return result1 && result2;
  }

  public boolean exchangeConfirm(ExchangeRequestDTO requestDTO) {
    PersonalAccountExchangeRequestDTO personalExchangeFrom = getPersonalExchangeRequestDTO(requestDTO, requestDTO.getFromUserId(),
        requestDTO.getFromAccountType(), requestDTO.getToAccountType(), requestDTO.getFromAmount(), requestDTO.getToAmount());

    PersonalAccountExchangeRequestDTO personalExchangeTo = getPersonalExchangeRequestDTO(requestDTO, requestDTO.getToUserId(),
        requestDTO.getToAccountType(), requestDTO.getFromAccountType(), requestDTO.getToAmount(), requestDTO.getFromAmount());
    boolean result1 = accountExchangeTransactionsService.personalAccountExchangeConfirm(personalExchangeTo);
    boolean result2 = accountExchangeTransactionsService.personalAccountExchangeConfirm(personalExchangeFrom);
    return result1 & result2;
  }

  public boolean exchangeCancel(ExchangeRequestDTO requestDTO) {
    PersonalAccountExchangeRequestDTO personalExchangeFrom = getPersonalExchangeRequestDTO(requestDTO, requestDTO.getFromUserId(),
        requestDTO.getFromAccountType(), requestDTO.getToAccountType(), requestDTO.getFromAmount(), requestDTO.getToAmount());

    PersonalAccountExchangeRequestDTO personalExchangeTo = getPersonalExchangeRequestDTO(requestDTO, requestDTO.getToUserId(),
        requestDTO.getToAccountType(), requestDTO.getFromAccountType(), requestDTO.getToAmount(), requestDTO.getFromAmount());
    boolean result1 = accountExchangeTransactionsService.personalAccountExchangeConfirmCancel(personalExchangeTo);
    boolean result2 = accountExchangeTransactionsService.personalAccountExchangeConfirmCancel(personalExchangeFrom);
    return result1 & result2;
  }

  private PersonalAccountExchangeRequestDTO getPersonalExchangeRequestDTO(ExchangeRequestDTO requestDTO, Long fromUserId, Integer fromAccountType,
      Integer toAccountType,
      String fromAmount, String toAmount) {
    PersonalAccountExchangeRequestDTO request = new PersonalAccountExchangeRequestDTO();
    request.setUserId(fromUserId);
    request.setFromAccountType(fromAccountType);
    request.setToAccountType(toAccountType);
    request.setFromAmount(fromAmount);
    request.setToAmount(toAmount);
    request.setTransactionNo(requestDTO.getTransactionNo());
    return request;
  }
}
