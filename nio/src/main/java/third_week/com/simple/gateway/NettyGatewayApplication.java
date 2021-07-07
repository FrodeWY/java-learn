package third_week.com.simple.gateway;

import third_week.com.simple.gateway.server.NettyServer;

public class NettyGatewayApplication {
    public static void main(String[] args) {
        final NettyServer nettyServer = new NettyServer();
        nettyServer.run(8004);
    }
}
