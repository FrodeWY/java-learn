package com.spring.service;

import org.springframework.stereotype.Component;

/**
 * @author wangyang
 * @ClassName ReceiveService
 * @Description TODO
 * @Date 2021/7/24 下午8:59
 * @Version 1.0
 */
@Component
public class ReceiveService {

  public void receive(String msg) {
    System.out.println("receive msg:" + msg);
  }

  public String ack() {
    String msg = "你好,稍后再拨";
    System.out.println("ack :" + msg);
    return msg;
  }
}
