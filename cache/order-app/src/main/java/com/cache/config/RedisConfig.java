package com.cache.config;

import com.cache.redis.RedisCounter;
import com.cache.redis.RedisLockUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author wangyang
 * @ClassName RedisConfig
 * @Description TODO
 * @Date 2021/9/2 下午11:15
 * @Version 1.0
 */
@Configuration
public class RedisConfig {

  @Bean
  public RedisLockUtil redisClient(StringRedisTemplate redisTemplate) {
    return new RedisLockUtil(redisTemplate);
  }

  @Bean
  public RedisCounter redisCounter(StringRedisTemplate redisTemplate) {
    return new RedisCounter(redisTemplate);
  }
}
