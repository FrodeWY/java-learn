package com.rpc.core.codec;

import com.rpc.core.api.Codec;

/**
 * @author wangyang
 * @ClassName CodecFactory
 * @Description TODO
 * @Date 2021/8/18 下午8:14
 * @Version 1.0
 */
public class CodecFactory {

  public static Codec getCodec(String type) {
    Codec codec;
    if (FastjsonCodec.NAME.equals(type)) {
      codec = new FastjsonCodec();
    } else if (MsgPackCodec.NAME.equals(type)) {
      codec = new MsgPackCodec();
    } else {
      throw new IllegalArgumentException("not found class type is :" + type + " codec");
    }
    return codec;
  }
}
