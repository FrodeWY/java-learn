package com.rpc.core.api;


public interface Registry {


  void register(String registerPath);

  void subscribe(String subscribePath, RegistryCenterListener registryCenterListener);

  void unSubscribe(String unSubscribePath, RegistryCenterListener registryCenterListener);

  void unRegister(String unRegisterPath);

  void destroy();
}
