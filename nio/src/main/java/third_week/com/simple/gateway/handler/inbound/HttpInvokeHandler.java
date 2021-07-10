package third_week.com.simple.gateway.handler.inbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;
import third_week.com.simple.gateway.exception.RequestFailedException;
import third_week.com.simple.gateway.filter.FilterChain;
import third_week.com.simple.gateway.invoker.Invoker;
import third_week.com.simple.gateway.invoker.impl.NettyClientInvoker;
import third_week.com.simple.gateway.loadbalance.LoadBalance;
import third_week.com.simple.gateway.result.Result;
import third_week.com.simple.gateway.router.Router;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HttpInvokeHandler extends ChannelInboundHandlerAdapter {

  private LoadBalance loadBalance;
  private Router router;
  private Invoker invoker;
  private FilterChain filterChain;
  private Executor executor;
  private static final ThreadPoolExecutor defaultExecutor;


  static {
    InvokerThreadFactory threadFactory = new InvokerThreadFactory();
    int corePoolSize = Runtime.getRuntime().availableProcessors();
    ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(1000);
    defaultExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize * 2 + 2, 60, TimeUnit.SECONDS, workQueue, threadFactory);
  }

  public HttpInvokeHandler(LoadBalance loadBalance, Router router, Invoker invoker) {
    this(loadBalance, router, invoker, null, defaultExecutor);
  }
  public HttpInvokeHandler(LoadBalance loadBalance, Router router, Invoker invoker,FilterChain filterChain) {
    this(loadBalance, router, invoker, filterChain, defaultExecutor);
  }

  public HttpInvokeHandler(LoadBalance loadBalance, Router router, Invoker invoker, Executor executor) {
    this(loadBalance, router, invoker, null, executor);
  }

  public HttpInvokeHandler(LoadBalance loadBalance, Router router, Invoker invoker, FilterChain filterChain, Executor executor) {
    this.loadBalance = loadBalance;
    this.router = router;
    this.invoker = invoker;
    this.filterChain = filterChain;
    this.executor = executor;
  }

  private static class InvokerThreadFactory implements ThreadFactory {

    private final AtomicInteger count = new AtomicInteger(1);

    @Override
    public Thread newThread(@NotNull Runnable r) {
      Thread t = new Thread(r);
      t.setName("InvokeThread--" + count.getAndIncrement());
      t.setUncaughtExceptionHandler((thread, throwable) -> {
        System.out.println("The " + thread.getName() + " is running with an exception:" + throwable.getMessage());
      });
      return t;
    }
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
