package com.mq.pojo.decoder;

import com.alibaba.fastjson.JSON;
import com.mq.pojo.RemoteCommand;

/**
 * @author wangyang
 * @ClassName RemoteCommandDecoder
 * @Description TODO
 * @Date 2021/9/20 上午11:51
 * @Version 1.0
 */
public class RemoteCommandFastjsonDecoder implements Decoder {

  @Override
  public Object decode(byte[] bytes) {
    return JSON.parseObject(bytes, RemoteCommand.class);
  }
}
