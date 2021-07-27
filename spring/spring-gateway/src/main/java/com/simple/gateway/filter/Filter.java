package com.simple.gateway.filter;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface Filter {
    /**
     * 发生实际调用后端服务之前,主要做参数处理,前置校验等前置操作
     */
    void preInvoke(FullHttpRequest request);

    /**
     * 调用时机:实际调用后获取到调用结果,主要负责对结果的一些后置处理
     */
    void onResponse(FullHttpRequest request, FullHttpResponse response);

    /**
     * 设置filter优先级 order越大优先级越大
     * @return order越大优先级越大
     */
    Integer getOrder();
}
