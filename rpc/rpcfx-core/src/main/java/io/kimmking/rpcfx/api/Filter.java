package io.kimmking.rpcfx.api;

public interface Filter {

    void filter(RpcfxRequest request);



    // Filter next();

    /**
     * 是否是消费端过滤器
     */
    default boolean isConsumer() {
        return false;
    }
    /**
     * 是否是服务提供端过滤器
     */
    default boolean isProvider(){
        return false;
    }
}
