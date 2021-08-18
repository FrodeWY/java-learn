package com.rpc.core.codec;

import com.rpc.core.api.Codec;
import java.io.IOException;
import org.msgpack.MessagePack;

/**
 * @author wangyang
 * @ClassName MsgPackCodec
 * @Description TODO
 * @Date 2021/8/18 下午3:21
 * @Version 1.0
 */
public class MsgPackCodec implements Codec {

  public static final String NAME = "msgPack";

  @Override
  public byte[] encode(Object object) throws IOException {
    MessagePack messagePack = new MessagePack();
    return messagePack.write(object);
  }

  @Override
  public Object decode(byte[] bytes) throws IOException {
    MessagePack messagePack = new MessagePack();
    return messagePack.read(bytes);
  }
}
