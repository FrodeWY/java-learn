package third_week.com.simple.gateway.invoker;

import io.netty.channel.ChannelHandlerContext;
import third_week.com.simple.gateway.result.Result;

public interface Invoker {

  default Result get(String url, ChannelHandlerContext ctx) {
    throw new UnsupportedOperationException();
  }

  default Result post(String url, ChannelHandlerContext ctx) {
    throw new UnsupportedOperationException();
  }


  default Result delete(String url, ChannelHandlerContext ctx) {
    throw new UnsupportedOperationException();
  }


}
