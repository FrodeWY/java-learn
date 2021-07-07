package third_week.com.simple.gateway.handler.inbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import third_week.com.simple.gateway.exception.RequestFailedException;
import third_week.com.simple.gateway.filter.FilterChain;
import third_week.com.simple.gateway.invoker.Invoker;
import third_week.com.simple.gateway.loadbalance.LoadBalance;
import third_week.com.simple.gateway.result.Result;
import third_week.com.simple.gateway.router.Router;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HttpInBoundHandler extends ChannelInboundHandlerAdapter {
    private LoadBalance loadBalance;
    private Router router;
    private Invoker invoker;
    private FilterChain filterChain;

    public HttpInBoundHandler(LoadBalance loadBalance, Router router, Invoker invoker) {
        this(loadBalance, router, invoker, null);
    }

    public HttpInBoundHandler(LoadBalance loadBalance, Router router, Invoker invoker, FilterChain filterChain) {
        this.loadBalance = loadBalance;
        this.router = router;
        this.invoker = invoker;
        this.filterChain = filterChain;
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
            if (filterChain != null) {
                filterChain.preInvoke(fullHttpRequest);
            }
            final String uri = fullHttpRequest.uri();
            final String substring = uri.substring(1);
            String apiPath = substring.substring(substring.indexOf("/"));
            if (!apiPath.endsWith("/")) {
                apiPath = apiPath + "/";
            }
            String backendUrl = endPoint + apiPath;

            CompletableFuture.supplyAsync(() -> invoker.get(backendUrl)).whenComplete((v, t) -> {
                if (t != null) {
                    System.out.println(t.getMessage());
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void handlerResponse(FullHttpRequest fullRequest, ChannelHandlerContext ctx, Result result) {

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
            filterChain.onResponse(fullRequest, fullResponse);
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
