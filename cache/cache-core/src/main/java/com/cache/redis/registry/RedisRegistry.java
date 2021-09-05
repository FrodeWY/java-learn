package com.cache.redis.registry;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.util.NamedThreadFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.util.StringUtils;

/**
 * @author wangyang
 * @ClassName RedisRegistry
 * @Description TODO
 * @Date 2021/9/4 上午10:24
 * @Version 1.0
 */
@Slf4j
public class RedisRegistry {

  private String root = "redisRegistry";
  private final StringRedisTemplate redisTemplate;
  private final RedisMessageListenerContainer redisMessageListenerContainer;
  private static final int expire = 60 * 1000;
  private final String REGISTRY_SIGN = "registry";
  private final String UNREGISTRY_SIGN = "unRegistry";
  private final Map<RegistryCenterListener, RedisSubscribeListener> listenerMap = new ConcurrentHashMap<>();
  private final Set<String> registeredServiceAddressCache = new ConcurrentSkipListSet<>();
  private final ScheduledExecutorService expireExecutor = Executors
      .newScheduledThreadPool(1, new NamedThreadFactory("redisRegistryExpireTimer", true));

  public RedisRegistry(StringRedisTemplate redisTemplate,
      RedisMessageListenerContainer redisMessageListenerContainer) {
    this.redisTemplate = redisTemplate;
    this.redisMessageListenerContainer = redisMessageListenerContainer;
    expireExecutor.scheduleAtFixedRate(new DeferExpiredTask(), expire / 2, expire / 2, TimeUnit.MILLISECONDS);
  }


  /**
   * 注册服务
   *
   * @param serviceName 服务名
   * @param address 服务地址
   */
  public void register(String serviceName, String address) {
    BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(root);
    String registeredServiceAddress = serviceName + "/" + address;
    hashOps.put(registeredServiceAddress, System.currentTimeMillis() + expire);
    redisTemplate.convertAndSend(root + "/" + serviceName, REGISTRY_SIGN);
    this.registeredServiceAddressCache.add(registeredServiceAddress);
  }

  /**
   * 订阅服务
   *
   * @param subscribeServiceName 订阅的服务名
   * @param registryCenterListener 回调监听器
   */
  void subscribe(String subscribeServiceName, RegistryCenterListener registryCenterListener) {
    RedisSubscribeListener subscribeListener = listenerMap
        .computeIfAbsent(registryCenterListener, k -> new RedisSubscribeListener(registryCenterListener, subscribeServiceName));
    redisMessageListenerContainer.addMessageListener(subscribeListener, new ChannelTopic(root + "/" + subscribeServiceName));
  }

  /**
   * 自动续约,清除过期服务地址
   */
  private class DeferExpiredTask implements Runnable {

    @Override
    public void run() {
      boolean exists = redisTemplate.hasKey(root) == null ? false : redisTemplate.hasKey(root);
      if (!exists) {
        return;
      }
      BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(root);
      Set<String> keys = hashOps.keys().stream().map(String::valueOf).collect(Collectors.toSet());
      //清除过期key
      for (String key : keys) {
        long expiredTime = Long.parseLong(String.valueOf(hashOps.get(key)));
        long currentTimeMillis = System.currentTimeMillis();
        if (expiredTime < currentTimeMillis) {
          hashOps.delete(key);
          String serviceName = key.substring(0, key.indexOf("/"));
          redisTemplate.convertAndSend(serviceName, UNREGISTRY_SIGN);
        }
      }
      for (String registeredServiceAddress : registeredServiceAddressCache) {
        //续约
        hashOps.put(registeredServiceAddress, System.currentTimeMillis() + expire);
        //如果当前不存在这个服务地址,需要推送一次
        if (!keys.contains(registeredServiceAddress)) {
          String serviceName = registeredServiceAddress.substring(0, registeredServiceAddress.indexOf("/"));
          redisTemplate.convertAndSend(serviceName, REGISTRY_SIGN);
        }
      }
    }
  }

  private class RedisSubscribeListener extends MessageListenerAdapter {

    private final RegistryCenterListener listener;
    public final String serviceName;

    private RedisSubscribeListener(RegistryCenterListener listener, String serviceName) {
      this.listener = listener;
      this.serviceName = serviceName;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
      byte[] channelBody = message.getChannel();
      byte[] messageBody = message.getBody();
      String channelStr = redisTemplate.getStringSerializer().deserialize(channelBody);
      String messageStr = redisTemplate.getStringSerializer().deserialize(messageBody);
      log.info("redisSubscribeListener receive channel:{},message:{}", channelStr, messageStr);
      if (!StringUtils.hasText(messageStr)) {
        return;
      }
      HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
      Set<Object> keys = opsForHash.keys(root);
      List<String> servicePathList = keys.stream().map(String::valueOf).filter(key -> key.startsWith(serviceName)).collect(Collectors.toList());
      listener.notify(servicePathList);
    }
  }
}
