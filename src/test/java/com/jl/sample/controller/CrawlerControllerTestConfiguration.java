/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.controller;

import com.jl.sample.config.DefaultsConfiguration;
import com.jl.sample.config.WebMvcConfiguration;
import com.jl.sample.service.CrawlerService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 *
 * @author jllach
 */
@Import({WebMvcConfiguration.class, DefaultsConfiguration.class})
public class CrawlerControllerTestConfiguration {

    @Bean
    public CrawlerService       fakeCrawlerService() {
        return Mockito.mock(CrawlerService.class);
    }
    
    @Bean(name = "taskExecutor")
    public AsyncTaskExecutor    getAsyncExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
        executor.setThreadNamePrefix("AsyncExecutor-");
        return executor;
    }
}