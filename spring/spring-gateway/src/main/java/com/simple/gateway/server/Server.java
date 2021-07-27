package com.simple.gateway.server;

public interface Server {

    /**
     * 运行监听服务监听指定端口
     *
     * @param port 监控端口
     */
    void run(int port);
}
