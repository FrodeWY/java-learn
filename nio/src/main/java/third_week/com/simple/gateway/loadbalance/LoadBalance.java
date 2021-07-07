package third_week.com.simple.gateway.loadbalance;

import io.netty.handler.codec.http.FullHttpRequest;
import third_week.com.simple.gateway.invoker.Invoker;

import java.net.URL;
import java.util.List;

public interface LoadBalance {
    /**
     * 负载均衡
     * @param invokerUrlList 可用的后端服务提供者URL列表
     * @param request 请求
     * @return 返回一个后端服务调用者
     */
    String select(List<String > invokerUrlList, FullHttpRequest request);


}
