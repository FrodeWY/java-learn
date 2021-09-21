import com.mq.broker.server.NettyServer;

/**
 * @author wangyang
 * @ClassName ServerTest
 * @Description TODO
 * @Date 2021/9/21 上午8:56
 * @Version 1.0
 */
public class ServerTest {

  public static void main(String[] args) throws InterruptedException {
    NettyServer server = new NettyServer(8080);
    server.start();
  }
}
