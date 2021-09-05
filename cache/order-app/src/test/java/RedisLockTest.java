import com.alibaba.fastjson.JSON;
import com.cache.OrderApplication;
import com.cache.dto.request.CreateOrderRequestDTO;
import com.cache.dto.request.CreateOrderRequestDTO.Order;
import com.cache.redis.RedisLockUtil;
import com.cache.utils.DistributionIdGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wangyang
 * @ClassName RedisLockTest
 * @Description TODO
 * @Date 2021/9/2 下午11:16
 * @Version 1.0
 */
@SpringBootTest(classes = OrderApplication.class)
public class RedisLockTest {

  @Autowired
  private RedisLockUtil client;

  @Test
  public void test01() {
    client.lock("test1", "test01", 10000L);
    System.out.println(1);
    try {
      Thread.sleep(6000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    boolean unlock = client.unlock("test1", "test02");
    if (unlock) {
      System.out.println("unlock success");
    } else {
      System.out.println("unlock failed");
    }
  }

  @Test
  public void test02() {
    boolean tryLock = false;
    try {
      tryLock = client.tryLock("test1", "test01", 10000L, TimeUnit.MILLISECONDS.toNanos(10000L));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (tryLock) {
      System.out.println("try lock success");
    } else {
      System.out.println("try lock failed");
      return;
    }
    System.out.println(1);
    try {
      Thread.sleep(6000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    boolean unlock = client.unlock("test1", "test01");
    if (unlock) {
      System.out.println("unlock success");
    } else {
      System.out.println("unlock failed");
    }


  }

  @Test
  public void test3() {
    CreateOrderRequestDTO requestDTO = new CreateOrderRequestDTO();
    CreateOrderRequestDTO.Order order = new Order();
    order.setBuyerMemberId(DistributionIdGenerator.generateSingleId());
    order.setOrderAmount("100");
    order.setPayAmount("100");
    order.setDiscountAmount("0");
    order.setTaxAmount("0");
    order.setScoreDeduction("0");
    order.setScore(0);
    order.setGrowthValue(0);
    order.setReceiveAddress("测试地址");
    order.setReceiverName("测试");
    order.setReceiverPhone(8888888L);
    order.setCurrency("CNY");

    requestDTO.setOrder(order);
    List<CreateOrderRequestDTO.OrderDetail> orderDetailList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      CreateOrderRequestDTO.OrderDetail orderDetail = new CreateOrderRequestDTO.OrderDetail();
      orderDetail.setSkuCode("000" + i);
      orderDetail.setQuantity(i);
      orderDetail.setUnitPrice("" + i);
      orderDetail.setTaxAmount("0");
      orderDetail.setActualAmount("30");
      orderDetail.setUnitExchangeScore(0);
      orderDetail.setCurrency("CNY");
      orderDetailList.add(orderDetail);
    }
    requestDTO.setOrderDetailList(orderDetailList);
    System.out.println(JSON.toJSONString(requestDTO));
  }
}
