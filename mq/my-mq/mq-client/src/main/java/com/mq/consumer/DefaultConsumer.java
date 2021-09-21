package com.mq.consumer;

import com.mq.api.MqClientApi;
import com.mq.client.Client;
import com.mq.client.NettyClient;
import com.mq.listener.ConsumeListener;
import com.mq.pojo.DefaultFuture;
import com.mq.pojo.enums.ConsumeMessageStatus;
import com.mq.pojo.enums.ConsumeModelEnum;
import com.mq.pojo.enums.ResponseCodeEnum;
import com.mq.pojo.message.Message;
import com.mq.pojo.request.PullMessageRequest;
import com.mq.pojo.response.GetRemoteOffsetInfoResponse;
import com.mq.pojo.response.PullMessageResponse;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyang
 * @ClassName DefaultConsumer
 * @Description TODO
 * @Date 2021/9/20 下午8:44
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultConsumer implements Consumer {

  private ConsumeListener listener;
  private ConsumeModelEnum consumeModelEnum;
  private String consumerGroupName;
  private Integer offset;
  private String topic;
  private Integer batchSize;
  private Client client;
  private String brokerAddr;
  private Integer brokerPort;
  private PullMessageService pullMessageService;

  public DefaultConsumer(String consumerGroupName) {
    this.consumerGroupName = consumerGroupName;
  }

  @Override
  public void registryMessageListener(ConsumeListener listener) {
    this.listener = listener;
  }

  @Override
  public void start() {
    if (client == null) {
      client = new NettyClient();
    }
    MqClientApi clientApi = new MqClientApi(client);
    if (pullMessageService == null) {
      pullMessageService = new PullMessageService(clientApi, listener);
    }
    PullMessageRequest messageRequest;
    try {
      client.start(brokerAddr, brokerPort);
      if (ConsumeModelEnum.CLUSTERING.equals(consumeModelEnum)) {
        DefaultFuture defaultFuture = clientApi.getRemoteOffsetInfo(topic, consumerGroupName);
        GetRemoteOffsetInfoResponse response = (GetRemoteOffsetInfoResponse) defaultFuture.get();
        Integer code = response.getCode();
        if (ResponseCodeEnum.SUCCESS.getCode().equals(code)) {
          messageRequest = PullMessageRequest.builder()
              .batchSize(batchSize)
              .consumeGroupName(consumerGroupName)
              .consumeModel(consumeModelEnum.getCode())
              .offset(response.getOffset())
              .topic(topic)
              .build();
          if (response.getMaxOffset() <= response.getOffset()) {
            Thread.sleep(2000);
          }
        } else {
          throw new RuntimeException(response.getErrorMessage());
        }
      } else {
        messageRequest = PullMessageRequest.builder()
            .batchSize(batchSize)
            .consumeGroupName(consumerGroupName)
            .consumeModel(consumeModelEnum.getCode())
            .offset(offset)
            .topic(topic)
            .build();
      }
      pullMessageService.addPullMessageRequest(messageRequest);
      pullMessageService.start();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void stop() {
    pullMessageService.interrupt();
    pullMessageService.close();
    client.close();
  }


  public class PullMessageService extends Thread {

    private BlockingQueue<PullMessageRequest> blockingQueue = new LinkedBlockingQueue<>();
    private MqClientApi clientApi;
    private ConsumeListener listener;
    private ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
        r -> new Thread(r, "PullMessageServiceThread-"));

    public PullMessageService(MqClientApi clientApi, ConsumeListener listener) {
      this.clientApi = clientApi;
      this.listener = listener;
    }

    public void addPullMessageRequest(PullMessageRequest request) {
      blockingQueue.add(request);
    }

    public void close() {
      scheduledExecutor.shutdown();
    }

    @Override
    public void run() {
      try {
        while (!Thread.interrupted()) {
          PullMessageRequest pullMessageRequest = blockingQueue.take();
          DefaultFuture defaultFuture = clientApi.pullMessage(pullMessageRequest);
          defaultFuture.whenComplete((r, e) -> {
            PullMessageResponse pullMessageResponse = (PullMessageResponse) r;
            Integer responseCode = pullMessageResponse.getCode();
            if (ResponseCodeEnum.SUCCESS.getCode().equals(responseCode)) {
              List<Message> messageList = pullMessageResponse.getMessageList();
              if (messageList != null && messageList.size() > 0) {
                ConsumeMessageStatus consumeMessageStatus = listener.consumerMessage(pullMessageResponse);
                if (consumeMessageStatus == ConsumeMessageStatus.CONSUME_SUCCESS) {
                  Integer nextBeginOffset = pullMessageResponse.getNextBeginOffset();
                  pullMessageRequest.setOffset(nextBeginOffset);
                  if (nextBeginOffset >= pullMessageResponse.getMaxOffset()) {
                    scheduledExecutor.schedule(() -> addPullMessageRequest(pullMessageRequest), 2000, TimeUnit.MILLISECONDS);
                  }
                  addPullMessageRequest(pullMessageRequest);
                } else {
                  addPullMessageRequest(pullMessageRequest);
                }
              }
            } else {
              throw new RuntimeException(pullMessageResponse.getErrorMessage());
            }
          });
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
