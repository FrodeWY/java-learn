package com.rpc.core.codec.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rpc.core.api.Codec;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * @author wangyang
 * @ClassName FastjsonCodec
 * @Description TODO
 * @Date 2021/8/18 下午7:34
 * @Version 1.0
 */
public class FastjsonCodec implements Codec {

  public static final String NAME = "fastjson";

  @Override
  public byte[] encode(Object object) throws IOException {
    return JSON.toJSONBytes(object);
  }

  @Override
  public <T> T decode(Object object, Class<T> type) throws IOException {
    byte[] bytes = JSON.toJSONBytes(object);
    return JSON.parseObject(bytes, type);
  }

  @Override
  public <T> T decode(String str, Class<T> type) throws IOException {
    return JSON.parseObject(str, type);
  }

  @Override
  public <T> T decode(ByteBuf in, Class<T> type) throws IOException {
    byte[] bytes = new byte[in.readableBytes()];
    in.readBytes(bytes);
    return decode(bytes, type);
  }

  @Override
  public <T> T decode(byte[] bytes, Class<T> type) throws IOException {
    return JSON.parseObject(bytes, type);
  }
}
