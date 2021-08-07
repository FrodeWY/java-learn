package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author wangyang
 * @ClassName ReadWriteApplication
 * @Description TODO
 * @Date 2021/8/6 下午1:17
 * @Version 1.0
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ReadWriteApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReadWriteApplication.class);
  }
}
