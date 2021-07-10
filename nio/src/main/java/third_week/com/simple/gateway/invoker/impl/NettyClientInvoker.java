package third_week.com.simple.gateway.invoker.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import third_week.com.simple.gateway.future.DefaultFuture;
import third_week.com.simple.gateway.handler.inbound.NettyClientInvokeHandler;
import third_week.com.simple.gateway.invoker.Invoker;
import third_week.com.simple.gateway.result.Result;
import third_week.com.simple.gateway.result.impl.SyncResult;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangyang
 * @ClassName NettyClientInvoker
 * @Description Netty invoker 实现
 * @Date 2021/7/7 下午9:32
 * @Version 1.0
 */
public class NettyClientInvoker implements Invoker {


  private static class NettyClient {

    private static final Map<String, NettyClient> CLIENT_CACHE = new ConcurrentHashMap<>();

    private final EventLoopGroup workerGroup;
    private final String uniqueKey;
    private final Bootstrap bootstrap;
    private final String host;
    private final Integer port;

    NettyClient(String host, int port) {
      this.host = host;
      this.port = port;
      uniqueKey = generateUniqueKey(host, port);
      workerGroup = new NioEventLoopGroup();
      bootstrap = new Bootstrap();
      bootstrap.group(workerGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, true)
          //                    .option(ChannelOption.TCP_NODELAY, true)
          .handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
              ch.pipeline()
                  //                                    .addLast(new HttpRequestEncoder())
                  //                                    .addLast(new HttpResponseDecoder())
                  .addLast(new HttpClientCodec())
                  .addLast(new HttpObjectAggregator(1024 * 1024))
                  .addLast(new NettyClientInvokeHandler());
            }
          });

    }

    public static NettyClient getClient(String host, int port) {
      String uniqueKey = generateUniqueKey(host, port);
      if (CLIENT_CACHE.containsKey(uniqueKey)) {
        return CLIENT_CACHE.get(uniqueKey);
      }
      NettyClient nettyClient = new NettyClient(host, port);
      CLIENT_CACHE.put(uniqueKey, nettyClient);
      return nettyClient;
    }

    private static String generateUniqueKey(String host, int port) {
      return host + "#" + port;
    }

    public Channel getChannel() {
      try {
        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
        return channelFuture.channel();
      } catch (InterruptedException e) {
        System.out.println("get channel failed" + e.getMessage());
        throw new RuntimeException(e);
      }
    }

    public void close() {
      if (workerGroup != null) {
        workerGroup.shutdownGracefully();
      }
      CLIENT_CACHE.remove(uniqueKey);
    }
  }


  @Override
  public Result get(String urlString, ChannelHandlerContext ctx) {
    NettyClient client;
    SyncResult result;
    Channel channel = null;
    try {
      URL url = new URL(urlString);
      System.out.println("request:" + urlString);
      String host = url.getHost();
      int port = url.getPort();
      String query = url.getQuery();
      client = NettyClient.getClient(host, port);
      channel = client.getChannel();
      DefaultFuture defaultFuture = send(channel, ctx, query);
      result = getSyncResult(defaultFuture);
    } catch (Exception e) {
      e.printStackTrace();
      result = new SyncResult(e);
    } finally {
      if (channel != null) {
        DefaultFuture.remove(channel.id().asLongText());
        channel.close();
      }
    }
    return result;
  }

  public DefaultFuture send(Channel channel, ChannelHandlerContext ctx, Object object) {
    if (object == null) {
      object = "";
    }
    channel.write(object);
    channel.flush();
    return new DefaultFuture(ctx, channel);
  }

  @NotNull
  private SyncResult getSyncResult(DefaultFuture defaultFuture) throws Exception {
    SyncResult result;
    FullHttpResponse response = (FullHttpResponse) defaultFuture.get();
    ByteBuf content = response.content();
    byte[] bytes = new byte[content.readableBytes()];
    content.readBytes(bytes);
    HttpHeaders headers = response.headers();
    result = new SyncResult(bytes, getHeaderMap(headers));
    return result;
  }

  private Map<String, String> getHeaderMap(HttpHeaders headers) {
    if (headers == null) {
      return new HashMap<>();
    }
    Map<String, String> headerMap = new HashMap<>();
    List<Map.Entry<String, String>> entries = headers.entries();
    for (Map.Entry<String, String> entry : entries) {
      headerMap.put(entry.getKey(), entry.getValue());
    }
    return headerMap;
  }
}
