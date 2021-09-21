package com.mq.broker.server.handler;

import com.mq.pojo.RemoteCommand;
import com.mq.pojo.decoder.RemoteCommandFastjsonDecoder;
import com.mq.pojo.encoder.Encoder;
import com.mq.pojo.encoder.FastJsonEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import java.util.List;

/**
 * @author wangyang
 * @ClassName DecodeHandler
 * @Description TODO
 * @Date 2021/9/20 上午9:52
 * @Version 1.0
 */
public class CodecHandler extends ByteToMessageCodec {

  private final RemoteCommandFastjsonDecoder decoder = new RemoteCommandFastjsonDecoder();
  private final Encoder encoder = new FastJsonEncoder();


  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
    byte[] bytes = encoder.encode(msg);
    out.writeBytes(bytes);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
    byte[] bytes = new byte[in.readableBytes()];
    in.readBytes(bytes);
    RemoteCommand remoteCommand = (RemoteCommand) decoder.decode(bytes);
    out.add(remoteCommand);
  }
}
