/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @author jllach
 */
@Configuration
@EnableAsync
public class AsyncConfiguration 
implements   AsyncConfigurer {
    
    @Autowired
    private DefaultsConfiguration   defaultConfig;

    @Override
    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor           getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(defaultConfig.getCorePoolSize());
        executor.setMaxPoolSize(defaultConfig.getMaxPoolSize());
        //executor.setQueueCapacity(??); // "unbounded" queue
        executor.setThreadNamePrefix("AsyncExecutor-");
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler    getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}