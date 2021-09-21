package com.mq.pojo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangyang
 * @ClassName DefaultFuture
 * @Description TODO
 * @Date 2021/9/20 下午3:25
 * @Version 1.0
 */
public class DefaultFuture extends CompletableFuture {

  private static ConcurrentHashMap<String, DefaultFuture> FUTURE_CACHE = new ConcurrentHashMap<>();

  private RemoteCommand remoteCommand;


  public DefaultFuture(RemoteCommand remoteCommand) {
    this.remoteCommand = remoteCommand;
    FUTURE_CACHE.put(remoteCommand.getRequestId(), this);
  }

  public static DefaultFuture getFuture(String requestId) {
    return FUTURE_CACHE.get(requestId);
  }
}
