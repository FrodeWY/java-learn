package com.rpc.rpcfx.demo.provider;

import com.rpc.autoconfigure.server.RpcServer;
import com.rpc.core.common.RpcfxRequest;
import com.rpc.core.common.RpcfxResponse;
import com.rpc.core.common.ServerEnum;
import com.rpc.core.server.RpcfxInvoker;
import com.rpc.rpcfx.demo.api.OrderService;
import com.rpc.rpcfx.demo.api.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RpcfxServerApplication {

    public static void main(String[] args) throws Exception {
        RpcServer.start(RpcfxServerApplication.class, args, ServerEnum.NETTY);
    }



    @Autowired
    RpcfxInvoker invoker;

    @PostMapping("/")
    public RpcfxResponse invoke(@RequestBody RpcfxRequest request) {
        return invoker.invoke(request);
    }

    @Bean
    public UserService createUserService() {
        return new UserServiceImpl();
    }

    @Bean
    public OrderService createOrderService() {
        return new OrderServiceImpl();
    }


}
