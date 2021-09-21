package com.mq.broker.server;

public interface Server {

  void start() throws InterruptedException;

  void close();
}
