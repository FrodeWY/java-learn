package com.simple.gateway.loadbalance.impl;

import com.simple.gateway.loadbalance.LoadBalance;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡
 */
@Component
public class RandomLoadBalance implements LoadBalance {

    @Override
    public String select(List<String> invokerUrlList, FullHttpRequest request) {
        if(invokerUrlList==null){
            throw new IllegalArgumentException("available backend server url is empty");
        }
        return invokerUrlList.get(new Random().nextInt(invokerUrlList.size()));
    }
}
