package com.cache.redis;

import java.util.Collections;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * @author wangyang
 * @ClassName redisCounter
 * @Description redis  计数器
 * @Date 2021/9/3 下午10:15
 * @Version 1.0
 */
public class RedisCounter {

  private final StringRedisTemplate redisTemplate;


  public RedisCounter(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  /**
   * 对key 增加 count
   *
   * @param key 业务唯一标识
   * @param count 增加数量
   * @return true :成功,false:失败
   */
  public boolean add(String key, Long count) {
    Long increment = redisTemplate.opsForValue().increment(key, count);
    return increment != null && increment > 0;
  }

  /**
   * 扣减数量
   *
   * @param key 业务标识
   * @param count 扣减数量
   * @return true :成功,false:失败
   */
  public boolean decr(String key, Long count) {
    if (count <= 0) {
      return false;
    }
    StringBuilder builder = new StringBuilder();
    builder.append("if redis.call('exists',KEYS[1])==1 then ");
    builder.append("   local stock=tonumber(redis.call('get',KEYS[1])) ");
    builder.append("   if(stock+tonumber(ARGV[1])>=0) then ");
    builder.append("       redis.call('incrby',KEYS[1],ARGV[1]) ");
    builder.append("       return stock");
    builder.append("   end");
    builder.append("   return -1 ");
    builder.append("end ");
    builder.append("return -1");
    DefaultRedisScript<Long> script = new DefaultRedisScript<>(builder.toString(), Long.class);
    Long execute = redisTemplate.execute(script, Collections.singletonList(key), String.valueOf(-count));
    return execute != null && execute > 0;
  }
}
