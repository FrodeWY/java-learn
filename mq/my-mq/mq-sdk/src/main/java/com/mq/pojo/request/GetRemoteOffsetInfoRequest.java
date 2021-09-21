package com.mq.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyang
 * @ClassName GetConsumeGroupOffsetRequest
 * @Description 集群模式下获取消费组offset
 * @Date 2021/9/20 下午10:23
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRemoteOffsetInfoRequest {

  private String topic;

  private String consumeGroupName;
}
