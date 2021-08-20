package com.rpc.rpcfx.demo.consumer;

import com.rpc.autoconfigure.annotation.RpcReference;
import com.rpc.core.api.GenericService;
import com.rpc.core.protocol.RegistryProtocol;
import com.rpc.core.proxy.JdkProxy;
import com.rpc.rpcfx.demo.api.User;
import com.rpc.rpcfx.demo.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RpcfxClientApplication {


  @RpcReference
  private UserService userService;

  @Autowired
  private RegistryProtocol protocol;

  public static void main(String[] args) {
    SpringApplication.run(RpcfxClientApplication.class, args);
  }

  @GetMapping(path = "/user/{id}")
  public User getUser(@PathVariable int id) {
    User user = userService.findById(1);
    System.out.println("find user id=" + id + " from server: " + user.getName());
    //泛型调用
    GenericService genericService = new JdkProxy().genericServiceProxy(protocol);
    Object genericResult = genericService
        .invoke("com.rpc.rpcfx.demo.api.UserService", "findById", new String[]{"java.lang.Integer"}, new Object[]{1});
    System.out.println("genericResult = " + genericResult);
    return user;
  }

}



