package com.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author wangyang
 * @ClassName OrderApplication
 * @Description TODO
 * @Date 2021/9/2 下午10:15
 * @Version 1.0
 */
@SpringBootApplication
@EnableCaching
public class OrderApplication {


  public static void main(String[] args) {
    SpringApplication.run(OrderApplication.class, args);


  }
}
