import com.alibaba.fastjson.JSON;
import com.mq.consumer.DefaultConsumer;
import com.mq.listener.ConsumeListener;
import com.mq.pojo.enums.ConsumeMessageStatus;
import com.mq.pojo.enums.ConsumeModelEnum;
import com.mq.pojo.response.PullMessageResponse;

/**
 * @author wangyang
 * @ClassName ConsumerTest
 * @Description TODO
 * @Date 2021/9/21 上午9:30
 * @Version 1.0
 */
public class ConsumerTest {

  public static void main(String[] args) {
    DefaultConsumer consumer = new DefaultConsumer("consumer-group-01");
    consumer.setBatchSize(10);
    consumer.setOffset(1);
    consumer.setBrokerAddr("localhost");
    consumer.setBrokerPort(8080);
    consumer.setConsumeModelEnum(ConsumeModelEnum.BROADCASTING);
    consumer.setListener(new ConsumeListener() {
      @Override
      public ConsumeMessageStatus consumerMessage(PullMessageResponse response) {
        String toJSONString = JSON.toJSONString(response);
        System.out.println("receive response" + toJSONString);
        return ConsumeMessageStatus.CONSUME_FAIL;
      }
    });
    consumer.setTopic("test-topic");
    consumer.start();
  }
}
