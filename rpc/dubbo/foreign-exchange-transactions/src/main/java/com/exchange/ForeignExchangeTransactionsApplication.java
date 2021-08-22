package com.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * @author wangyang
 * @ClassName ForeignExchangeTransactionsApplication
 * @Description TODO
 * @Date 2021/8/22 下午12:05
 * @Version 1.0
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class ForeignExchangeTransactionsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ForeignExchangeTransactionsApplication.class, args);
  }
}
