package com.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * @author wangyang
 * @ClassName AccountServiceBootStrap
 * @Description TODO
 * @Date 2021/8/22 上午2:12
 * @Version 1.0
 */
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class AccountServiceBootStrap {
  public static void main(String[] args) {
    SpringApplication.run(AccountServiceBootStrap.class, args);
  }

}
