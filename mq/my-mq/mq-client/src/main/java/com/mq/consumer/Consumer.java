package com.mq.consumer;

import com.mq.listener.ConsumeListener;

/**
 * @author wangyang
 * @ClassName Consumer
 * @Description TODO
 * @Date 2021/9/20 下午8:45
 * @Version 1.0
 */
public interface Consumer {

  void registryMessageListener(ConsumeListener listener);

  void start();

  void stop();
}
