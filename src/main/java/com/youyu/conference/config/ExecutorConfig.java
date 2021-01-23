package com.youyu.conference.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ExecutorConfig {

    /**
     * 配置异步线程池
     * @return
     */
    @Bean(name = "asyncChatExecutor")
    public Executor asyncChatExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(5);
        //最大线程数
        executor.setMaxPoolSize(10);
        //队列大小
        executor.setQueueCapacity(99999);
        executor.setThreadNamePrefix("async-chat");
        /**
         * rejection-policy :当pool达到max size的时候如何处理新任务
         * CallerRuns：不在新线程中执行任务，而是在调用者所在的线程执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化执行器
        executor.initialize();
        return executor;

    }

}