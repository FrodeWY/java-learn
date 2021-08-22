package com.rpc.core.client.netty;

import com.rpc.core.api.Client;
import com.rpc.core.api.Codec;
import com.rpc.core.common.RpcfxRequest;
import com.rpc.core.common.RpcfxResponse;
import com.rpc.core.server.netty.EncodeHandler;
import com.rpc.core.server.netty.RpcfxResponseDecodeHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NettyClient implements Client {

  public static final String NAME = "netty";
  private final String serverUrl;
  private final Codec codec;
  private final String scheme = "http://";
  private final InternalNettyClient client;

  public NettyClient(String serverUrl, Codec codec) {
    this.serverUrl = serverUrl;
    this.codec = codec;
    try {
      URL url = new URL(scheme + serverUrl);
      client = new InternalNettyClient(url.getHost(), url.getPort(), this.codec);
      client.start();
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public RpcfxResponse send(RpcfxRequest request) {
    RpcfxResponse result;
    try {
      log.info("request:" + serverUrl);
      DefaultFuture defaultFuture = send(client.getChannel(), request);
      result = getResponse(defaultFuture, request);
    } catch (Exception e) {
      e.printStackTrace();
      result = new RpcfxResponse(e);
    }
    return result;
  }

  private static class InternalNettyClient {


    private final EventLoopGroup workerGroup;
    private final Bootstrap bootstrap;
    private final String host;
    private final Integer port;
    private Channel channel;

    InternalNettyClient(String host, int port, Codec codec) {
      this.host = host;
      this.port = port;
      workerGroup = new NioEventLoopGroup(1);
      bootstrap = new Bootstrap();
      bootstrap.group(workerGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, true)
          //                    .option(ChannelOption.TCP_NODELAY, true)
          .handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
              ch.pipeline()
                  .addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                  .addLast("decoder", new RpcfxResponseDecodeHandler(codec))
                  .addLast("frameEncoder", new LengthFieldPrepender(2))
                  .addLast("encoder", new EncodeHandler(codec))
                  .addLast(new NettyClientInvokeHandler());
            }
          });
    }

    public void start() {
      try {
        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
        boolean success = channelFuture.awaitUninterruptibly(2000, TimeUnit.MILLISECONDS);
        if (success && channelFuture.isSuccess()) {
          if (channel != null) {
            channel.close();
          }
          channel = channelFuture.channel();
        }
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }


    public Channel getChannel() {
      return channel;
    }

    public void close() {
      if (workerGroup != null) {
        workerGroup.shutdownGracefully();
      }
      if (channel != null) {
        channel.close();
      }
    }
  }


  public DefaultFuture send(Channel channel, Object object) {
    if (object == null) {
      object = "";
    }
    channel.write(object);
    channel.flush();
    return new DefaultFuture(channel);
  }

  @NotNull
  private RpcfxResponse getResponse(DefaultFuture defaultFuture, RpcfxRequest request) throws Exception {
    RpcfxResponse response = null;
    try {
      response = (RpcfxResponse) defaultFuture.get();
      response.setResult(codec.decode(response.getResult(), request.getReturnType()));
      response.setStatus(true);
    } catch (Exception e) {
      log.error(e.getMessage());
      response = new RpcfxResponse();
      response.setException(e);
    }
    return response;
  }

}
