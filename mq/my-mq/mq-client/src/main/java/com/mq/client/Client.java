package com.mq.client;

import com.mq.pojo.DefaultFuture;
import com.mq.pojo.RemoteCommand;

/**
 * @author wangyang
 * @ClassName Client
 * @Description TODO
 * @Date 2021/9/20 下午3:03
 * @Version 1.0
 */
public interface Client {

  void start(String addr, Integer port) throws InterruptedException;

  DefaultFuture send(RemoteCommand remoteCommand);

  void close();
}
