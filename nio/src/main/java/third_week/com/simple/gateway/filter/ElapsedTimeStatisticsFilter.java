package third_week.com.simple.gateway.filter;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 接口耗时统计过滤器
 */
public class ElapsedTimeStatisticsFilter implements Filter {
    Map<String, Long> cache = new ConcurrentHashMap<>();

    @Override
    public void preInvoke(FullHttpRequest request) {
        final String uniqueId = request.headers().get(HeaderAppendUniqueIdFilter.X_UNIQUE_ID);
        cache.put(uniqueId,System.currentTimeMillis());
    }

    @Override
    public void onResponse(FullHttpRequest request, FullHttpResponse response) {
        final String requestUniqueId = request.headers().get(HeaderAppendUniqueIdFilter.X_UNIQUE_ID);
        final Long startTime = cache.get(requestUniqueId);
        System.out.println("request unique id:"+requestUniqueId+"elapsed time:"+(System.currentTimeMillis()-startTime));
    }

    @Override
    public Integer getOrder() {
        return Integer.MAX_VALUE - 10;
    }
}
