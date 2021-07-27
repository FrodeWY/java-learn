package com.simple.gateway;

import com.simple.gateway.server.impl.NettyServer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NettyGatewayApplication {
    public static void main(String[] args) {
        final ConfigurableApplicationContext context =
                new SpringApplicationBuilder(NettyGatewayApplication.class).web(WebApplicationType.NONE).run(args);
        final NettyServer server = context.getBean("nettyServer", NettyServer.class);
        server.run(8004);
    }
}
