package com.balls.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    //使用线程池异步执行任务，减少后台响应给前端的时间
    @Bean(value = "threadPoolInstance")
    public ExecutorService createThreadPoolInstance() {
        ExecutorService threadPool = new ThreadPoolExecutor(5, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        return threadPool;
    }

}

