import com.mq.pojo.message.Message;
import com.mq.producer.DefaultProducer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyang
 * @ClassName Test
 * @Description TODO
 * @Date 2021/9/21 上午8:55
 * @Version 1.0
 */
public class ProducerTest {

  public static void main(String[] args) {
    DefaultProducer producer = new DefaultProducer("localhost", 8080);
    producer.start();
    List<Message> messageList = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      Message message = new Message("message" + i);
      messageList.add(message);
    }
    producer.send(messageList, "test-topic");
    producer.stop();
  }
}
