package third_week.com.simple.gateway.filter.impl;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.UUID;
import third_week.com.simple.gateway.filter.Filter;

/**
 * 添加唯一id过滤器
 */
public class HeaderAppendUniqueIdFilter implements Filter {

    public static final String X_UNIQUE_ID = "x-unique-id";

    @Override
    public void preInvoke(FullHttpRequest request) {
        request.headers().set(X_UNIQUE_ID, UUID.randomUUID().toString());
    }

    @Override
    public void onResponse(FullHttpRequest request,FullHttpResponse response) {
        response.headers().set(X_UNIQUE_ID, request.headers().get(X_UNIQUE_ID));
    }

    @Override
    public Integer getOrder() {
        return Integer.MAX_VALUE-1;
    }


}
