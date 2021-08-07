package com.readwrite.loadbalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author wangyang
 * @ClassName RandomLoadBalance
 * @Description TODO
 * @Date 2021/8/7 下午1:08
 * @Version 1.0
 */
@Component
@ConditionalOnProperty(value = "readwrite.datasource.loadBalance", havingValue = "random")
public class RandomLoadBalance implements LoadBalance {

  @Override
  public Object select(List<Object> dataSourceKeyList) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    int i = random.nextInt(dataSourceKeyList.size());
    return dataSourceKeyList.get(i);
  }
}
