package third_week.com.simple.gateway.handler.outbound;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

/**
 * @author wangyang
 * @ClassName NettyClientOutBoundHandler
 * @Description TODO
 * @Date 2021/7/7 下午9:49
 * @Version 1.0
 */
public class NettyClientInvokeHandler extends ChannelDuplexHandler {

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    super.write(ctx, msg, promise);
    System.out.println("write ....");
  }

  @Override
  public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
    super.connect(ctx, remoteAddress, localAddress, promise);
    InetSocketAddress address = (InetSocketAddress) remoteAddress;
    System.out.println("connect  " + address);
  }

  @Override
  public void read(ChannelHandlerContext ctx) throws Exception {
    super.read(ctx);
    System.out.println("read...");
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    super.channelRead(ctx, msg);
    if (msg instanceof DefaultHttpResponse) {
      DefaultHttpResponse response = (DefaultHttpResponse) msg;
      System.out.println("receive result:" + response.decoderResult().toString());
    }
  }
}
