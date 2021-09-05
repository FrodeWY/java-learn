import com.cache.OrderApplication;
import com.cache.redis.RedisCounter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wangyang
 * @ClassName RedisCounterTest
 * @Description TODO
 * @Date 2021/9/3 下午10:22
 * @Version 1.0
 */
@SpringBootTest(classes = OrderApplication.class)
public class RedisCounterTest {

  @Autowired
  private RedisCounter redisCounter;

  @Test
  public void add() {
    boolean counter = redisCounter.add("counter", 10L);
    Assertions.assertTrue(counter);
  }

  @Test
  public void decr() {
    boolean counter1 = redisCounter.decr("counter1", 10L);
    Assertions.assertFalse(counter1);
    redisCounter.add("counter", 20L);
    boolean b1 = redisCounter.decr("counter", 15L);
    Assertions.assertTrue(b1);
    boolean b2 = redisCounter.decr("counter", 6L);
    Assertions.assertFalse(b2);

  }
}
