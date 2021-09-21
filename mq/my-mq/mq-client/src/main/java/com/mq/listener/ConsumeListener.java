package com.mq.listener;

import com.mq.pojo.enums.ConsumeMessageStatus;
import com.mq.pojo.response.PullMessageResponse;

/**
 * @author wangyang
 * @ClassName listener
 * @Description TODO
 * @Date 2021/9/20 下午8:47
 * @Version 1.0
 */
public interface ConsumeListener {

  ConsumeMessageStatus consumerMessage(PullMessageResponse response);
}
