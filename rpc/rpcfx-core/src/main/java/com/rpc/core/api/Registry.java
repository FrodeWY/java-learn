package com.rpc.core.api;


public interface Registry {


  void register(String registerPath);

  void subscribe(String subscribePath, Listener listener);

  void unSubscribe(String unSubscribePath, Listener listener);

  void unRegister(String unRegisterPath);

  void destroy();
}
