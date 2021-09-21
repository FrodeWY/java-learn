package com.mq.pojo.message;

/**
 * @author wangyang
 * @ClassName Message
 * @Description TODO
 * @Date 2021/9/17 下午11:07
 * @Version 1.0
 */
public class Message {

  private String content;

  public Message(String content) {
    this.content = content;
  }

  public Message() {
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
