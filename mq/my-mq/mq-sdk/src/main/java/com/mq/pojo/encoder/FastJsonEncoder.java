package com.mq.pojo.encoder;

import com.alibaba.fastjson.JSON;

/**
 * @author wangyang
 * @ClassName FastJsonEncoder
 * @Description TODO
 * @Date 2021/9/20 上午10:10
 * @Version 1.0
 */
public class FastJsonEncoder implements Encoder {

  @Override
  public byte[] encode(Object object) {
    return JSON.toJSONBytes(object);
  }
}
