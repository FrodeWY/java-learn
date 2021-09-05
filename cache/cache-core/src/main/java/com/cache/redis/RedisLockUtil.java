package com.cache.redis;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * @author wangyang
 * @ClassName RedisClient
 * @Description redis 分布式锁
 * @Date 2021/9/1 下午11:15
 * @Version 1.0
 */
@Slf4j
public class RedisLockUtil {

  private final StringRedisTemplate redisTemplate;


  public RedisLockUtil(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  /**
   * 获取redis lock
   *
   * @param lockName lock 名称
   * @param requestId 请求id
   * @param lockExpireTime 锁过期时间(millis)
   */
  public boolean lock(String lockName, String requestId, Long lockExpireTime) {
    BoundValueOperations<String, String> operations = redisTemplate.boundValueOps(lockName);
    Boolean aBoolean = operations.setIfAbsent(requestId, lockExpireTime, TimeUnit.MILLISECONDS);
    return aBoolean == null ? false : aBoolean;
  }

  /**
   * 在一段时间内(timeoutNanos)不断尝试获取锁
   *
   * @param lockName lock 名称
   * @param requestId 请求id
   * @param lockExpireTime 锁过期时间(millis)
   * @param timeoutNanos 获取锁超时时间
   * @return true:成功获取,false:获取锁失败
   */
  public boolean tryLock(String lockName, String requestId, Long lockExpireTime, Long timeoutNanos) throws InterruptedException {
    long deadline = System.nanoTime() + timeoutNanos;
    log.info("deadline :{}", deadline);
    long spinForTimeoutThreshold = 1000L;
    while (!Thread.interrupted()) {
      BoundValueOperations<String, String> operations = redisTemplate.boundValueOps(lockName);
      Boolean aBoolean = operations.setIfAbsent(requestId, lockExpireTime, TimeUnit.MILLISECONDS);
      if (aBoolean == null ? false : aBoolean) {
        return true;
      }
      timeoutNanos = deadline - System.nanoTime();

      if (deadline - System.nanoTime() < 0) {
        return false;
      }
      if (timeoutNanos > spinForTimeoutThreshold) {
        LockSupport.parkNanos(this, spinForTimeoutThreshold);
      }
    }
    return false;
  }

  /**
   * 释放锁
   *
   * @param lockName lock 名称
   * @param requestId 请求id
   */
  public boolean unlock(String lockName, String requestId) {
    String eval = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
    RedisScript<Long> script = new DefaultRedisScript<>(eval, Long.class);
    Long execute = redisTemplate.execute(script, Collections.singletonList(lockName), requestId);
    return execute != null && (execute > 0);
  }

}
