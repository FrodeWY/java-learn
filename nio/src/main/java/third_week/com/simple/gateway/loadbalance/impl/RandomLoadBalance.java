package third_week.com.simple.gateway.loadbalance.impl;

import io.netty.handler.codec.http.FullHttpRequest;
import third_week.com.simple.gateway.invoker.Invoker;

import java.net.URL;
import java.util.List;
import java.util.Random;
import third_week.com.simple.gateway.loadbalance.LoadBalance;

/**
 * 随机负载均衡
 */
public class RandomLoadBalance implements LoadBalance {

    @Override
    public String select(List<String> invokerUrlList, FullHttpRequest request) {
        if(invokerUrlList==null){
            throw new IllegalArgumentException("available backend server url is empty");
        }
        return invokerUrlList.get(new Random().nextInt(invokerUrlList.size()));
    }
}
