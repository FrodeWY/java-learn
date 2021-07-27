package com.simple.gateway.config;

import com.simple.gateway.handler.inbound.NettyServerChannelInBoundHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ThreadPoolConfig {

    @Bean(name = "asyncThreadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor() {
        InvokerThreadFactory threadFactory = new InvokerThreadFactory();
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(1000);
        return new ThreadPoolExecutor(corePoolSize, corePoolSize * 2 + 2, 60, TimeUnit.SECONDS, workQueue, threadFactory);
    }


    private static class InvokerThreadFactory implements ThreadFactory {

        private final AtomicInteger count = new AtomicInteger(1);

        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread t = new Thread(r);
            t.setName("InvokeThread--" + count.getAndIncrement());
            t.setUncaughtExceptionHandler((thread, throwable) -> {
                System.out.println("The " + thread.getName() + " is running with an exception:" + throwable.getMessage());
            });
            return t;
        }
    }
}
