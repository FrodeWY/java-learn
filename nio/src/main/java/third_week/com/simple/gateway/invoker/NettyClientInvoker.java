package third_week.com.simple.gateway.invoker;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import third_week.com.simple.gateway.handler.outbound.NettyClientInvokeHandler;
import third_week.com.simple.gateway.result.Result;

/**
 * @author wangyang
 * @ClassName NettyClientInvoker
 * @Description TODO
 * @Date 2021/7/7 下午9:32
 * @Version 1.0
 */
public class NettyClientInvoker implements Invoker {

  private final static Map<String, NettyClient> CLIENT_CACHE = new ConcurrentHashMap<>();

  private static class NettyClient {

    private Channel channel;
    private EventLoopGroup workerGroup;

    NettyClient(String host, int port) {
      workerGroup = new NioEventLoopGroup();
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(workerGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, true)
          .option(ChannelOption.TCP_NODELAY, true)
          .handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
              ch.pipeline()
                  .addLast(new HttpRequestEncoder())
                  .addLast(new HttpResponseDecoder())
                  .addLast(new NettyClientInvokeHandler());
            }
          });
      ChannelFuture channelFuture = bootstrap.connect(host, port);
      boolean success = channelFuture.awaitUninterruptibly(2, TimeUnit.SECONDS);
      if (success) {
        channel = channelFuture.channel();
      } else {
        throw new RuntimeException("connect timeout");
      }
    }

    private void close() {
      if (channel != null) {
        channel.close();
      }
      workerGroup.shutdownGracefully();
    }
  }


  @Override
  public Result get(String urlString) {
    try {
      URL url = new URL(urlString);
      String host = url.getHost();
      int port = url.getPort();
      String cacheKey = getCacheKey(host, port);
      NettyClient client;
      if (CLIENT_CACHE.containsKey(cacheKey)) {
        client = CLIENT_CACHE.get(cacheKey);
      } else {
        client = new NettyClient(host, port);
      }
      Channel channel = client.channel;
      channel.writeAndFlush(new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, urlString));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private String getCacheKey(String host, int port) {
    return host + port;
  }
}
