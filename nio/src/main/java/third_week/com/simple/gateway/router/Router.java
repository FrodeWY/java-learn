package third_week.com.simple.gateway.router;

import io.netty.handler.codec.http.FullHttpRequest;
import third_week.com.simple.gateway.invoker.Invoker;

import java.net.URL;
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
