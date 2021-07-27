package com.simple.gateway.router;

import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;

public interface Router {
    /**
     * 根据请求获取后端服务调用者列表
     *
     * @param request 请求
     * @return 返回可用后端地址
     */
    List<String> route(FullHttpRequest request);

}
