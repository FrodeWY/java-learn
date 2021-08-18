package com.rpc.core.codec;

import com.alibaba.fastjson.JSON;
import com.rpc.core.api.Codec;
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
  public Object decode(byte[] bytes) throws IOException {
    return JSON.parse(bytes);
  }
}
