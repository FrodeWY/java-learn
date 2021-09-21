package com.mq.pojo.encoder;

/**
 * @author wangyang
 * @ClassName Encoder
 * @Description TODO
 * @Date 2021/9/19 下午10:16
 * @Version 1.0
 */
public interface Encoder {

  byte[] encode(Object object);
}
