package third_week.com.simple.gateway.server;

public interface Server {
    /**
     * 初始化一个监控服务
     */
    void init();

    /**
     * 运行初始化好的监听服务监听指定端口
     * @param port 监控端口
     */
    void run(int port);
}
