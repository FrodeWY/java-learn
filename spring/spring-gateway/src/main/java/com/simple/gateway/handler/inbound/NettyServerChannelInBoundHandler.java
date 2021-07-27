package com.simple.gateway.handler.inbound;

import com.simple.gateway.exception.RequestFailedException;
import com.simple.gateway.filter.Filter;
import com.simple.gateway.invoker.Invoker;
import com.simple.gateway.loadbalance.LoadBalance;
import com.simple.gateway.result.Result;
import com.simple.gateway.router.Router;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class NettyServerChannelInBoundHandler extends ChannelInboundHandlerAdapter {

    private LoadBalance loadBalance;
    private Router router;
    private Invoker invoker;
    private Filter filter;
    private Executor executor;

    public NettyServerChannelInBoundHandler(LoadBalance loadBalance, Router router, Invoker invoker, Filter filter, Executor executor) {
        this.loadBalance = loadBalance;
        this.router = router;
        this.invoker = invoker;
        this.filter = filter;
        this.executor = executor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
        final List<String> urlList = router.route(fullHttpRequest);
        if (urlList == null || urlList.size() == 0) {
            throw new RequestFailedException("No service available");
        }
        try {
            final String endPoint = loadBalance.select(urlList, fullHttpRequest);
            if (filter != null) {
                filter.preInvoke(fullHttpRequest);
            }
            String apiPath = getApiPath(fullHttpRequest);
            String backendUrl = endPoint + apiPath;
            CompletableFuture.supplyAsync(() -> invoker.get(backendUrl, ctx), executor).whenComplete((v, t) -> {
                if (t != null) {
                    System.out.println(t.getMessage());
                    exceptionCaught(ctx, t);
                    return;
                }
                handlerResponse(fullHttpRequest, ctx, v);
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //减少ByteBuf 的引用计数
            ReferenceCountUtil.release(msg);
        }

    }

    /**
     * 获取api path
     */
    private String getApiPath(FullHttpRequest fullHttpRequest) {
        final String uri = fullHttpRequest.uri();
        final String substring = uri.substring(1);
        final int beginIndex = substring.indexOf("/");
        if (beginIndex == -1) {
            return "/";
        }
        String apiPath = substring.substring(beginIndex);
        if (!apiPath.endsWith("/")) {
            apiPath = apiPath + "/";
        }
        return apiPath;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void handlerResponse(FullHttpRequest fullRequest, ChannelHandlerContext ctx, Result result) {
        if (result == null) {
            ctx.close();
            return;
        }
        if (result.hasException()) {
            exceptionCaught(ctx, result.getException());
        }
        FullHttpResponse fullResponse = null;

        try {
            final byte[] bytes = result.getBody();
            fullResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(bytes));
            final String contentType = "Content-Type";
            final String contentLength = "Content-Length";
            fullResponse.headers().set(contentType, result.getHeader(contentType));
            fullResponse.headers().setInt(contentLength, Integer.parseInt(result.getHeader(contentLength)));
            filter.onResponse(fullRequest, fullResponse);
        } catch (Exception e) {
            e.printStackTrace();
            fullResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(fullResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(fullResponse);
                }
            }
            ctx.flush();
        }

    }
}
