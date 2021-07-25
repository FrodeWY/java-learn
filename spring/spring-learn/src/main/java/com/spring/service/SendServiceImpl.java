package com.spring.service;

import org.springframework.stereotype.Service;

/**
 * @author wangyang
 * @ClassName SendService
 * @Description TODO
 * @Date 2021/7/24 下午4:03
 * @Version 1.0
 */
@Service
public class SendServiceImpl implements SendService {

  public String send(String s) {
    String msg = "send 你好:" + s;
    System.out.println(msg);
    return msg;
  }


  public void send2(String s) {
    String msg = "send2 你好:" + s;
    System.out.println(msg);
  }
}
