package com.mq.broker.processor;

import com.mq.pojo.RemoteCommand;
import io.netty.channel.Channel;

/**
 * @author wangyang
 * @ClassName Processor
 * @Description 处理器
 * @Date 2021/9/20 上午11:59
 * @Version 1.0
 */
public interface Processor {

  /**
   * 处理客户端请求
   *
   * @param request 客户端请求
   * @return 响应
   */
  RemoteCommand process(RemoteCommand request, Channel channel);

  boolean matcher(RemoteCommand request);
}
