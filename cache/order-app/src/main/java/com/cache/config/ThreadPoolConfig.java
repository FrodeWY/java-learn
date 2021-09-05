package com.cache.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangyang
 * @ClassName ThreadPoolConfig
 * @Description TODO
 * @Date 2021/9/4 上午12:37
 * @Version 1.0
 */
@Configuration
public class ThreadPoolConfig {

  @Bean
  public ThreadPoolExecutor commonThreadPool() {
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    return new ThreadPoolExecutor(availableProcessors, 2 * availableProcessors + 1,
        60000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1000));
  }
}
