package third_week.com.simple.gateway.handler.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import third_week.com.simple.gateway.feture.DefaultFuture;

/**
 * @author wangyang
 * @ClassName NettyClientOutBoundHandler
 * @Description TODO
 * @Date 2021/7/7 下午9:49
 * @Version 1.0
 */
public class NettyClientInvokeHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            FullHttpResponse response = (FullHttpResponse) msg;
            DefaultFuture future = DefaultFuture.getFuture(ctx.channel().id().asLongText());
            future.complete(response);
        } finally {
            ctx.flush();
        }
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
