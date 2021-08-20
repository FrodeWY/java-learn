package com.rpc.autoconfigure.server;

import com.rpc.autoconfigure.config.RpcConfigProperties;
import com.rpc.autoconfigure.handler.IOCChannelHandler;
import com.rpc.core.codec.CodecFactory;
import com.rpc.core.common.ServerEnum;
import com.rpc.core.server.RpcfxInvoker;
import com.rpc.core.server.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

public class RpcServer {


    public static void start(Class<?> mainClass, String[] args, ServerEnum serverEnum) throws InterruptedException {
        if (serverEnum == ServerEnum.HTTP) {
            SpringApplication.run(mainClass, args);
        } else if (serverEnum == ServerEnum.NETTY) {
            ConfigurableApplicationContext context = new SpringApplicationBuilder(mainClass).web(WebApplicationType.NONE).run(args);
            context.start();
            Environment environment = context.getBean(Environment.class);
            RpcfxInvoker rpcfxInvoker = context.getBean(RpcfxInvoker.class);
            RpcConfigProperties properties = context.getBean(RpcConfigProperties.class);
            NettyServer server = new NettyServer(environment.getProperty("server.port", Integer.class),
                    new IOCChannelHandler(rpcfxInvoker), CodecFactory.getCodec(properties.getProvider().getCodec()));
            server.init();
        }
    }
}