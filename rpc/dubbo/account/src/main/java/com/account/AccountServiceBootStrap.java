package com.account;

import com.account.dto.request.PersonalAccountExchangeRequestDTO;
import com.account.service.AccountExchangeTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangyang
 * @ClassName AccountServiceBootStrap
 * @Description TODO
 * @Date 2021/8/22 上午2:12
 * @Version 1.0
 */
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@RestController
public class AccountServiceBootStrap {

  @Autowired
  private AccountExchangeTransactionsService accountExchangeTransactionsService;

  public static void main(String[] args) {
    SpringApplication.run(AccountServiceBootStrap.class, args);
  }

  @PostMapping(value = "/")
  public void test(@RequestBody PersonalAccountExchangeRequestDTO requestDTO) {
    accountExchangeTransactionsService.personalAccountExchange(requestDTO);
  }
}
