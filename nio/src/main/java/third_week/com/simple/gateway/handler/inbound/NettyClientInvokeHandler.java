package third_week.com.simple.gateway.handler.inbound;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;
import third_week.com.simple.gateway.future.DefaultFuture;

/**
 * @author wangyang
 * @ClassName NettyClientOutBoundHandler
 * @Description TODO
 * @Date 2021/7/7 下午9:49
 * @Version 1.0
 */
public class NettyClientInvokeHandler extends ChannelInboundHandlerAdapter {

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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
