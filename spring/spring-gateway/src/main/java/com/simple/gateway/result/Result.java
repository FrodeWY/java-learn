package com.simple.gateway.result;

import java.util.Map;

public interface Result {

    /**
     * 获取结果
     */
    byte[] getBody();

    /**
     * 获取所有头部信息
     */
    Map<String, String> getHeader();

    /**
     * 获取某个头部信息
     *
     * @param headerName 指定的头部
     */
    String getHeader(String headerName);

    /**
     * 是否有异常
     *
     * @return true:存在异常,false:不存在异常
     */
    boolean hasException();

    /**
     * 如果有异常,获取异常对象
     *
     * @return Throwable, 如果存在异常的话, 返回异常独享
     */
    Throwable getException();
}
