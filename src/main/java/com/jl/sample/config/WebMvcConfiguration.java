/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 *
 * @author jllach
 */
@Configuration
@ComponentScan(basePackageClasses = {com.jl.sample.controller.CrawlerController.class})
@EnableWebMvc
public class WebMvcConfiguration 
extends      WebMvcConfigurationSupport {
    
    @Autowired
    private AsyncTaskExecutor       asyncExecutor;
    @Autowired
    private DefaultsConfiguration   defaultConfig;

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(defaultConfig.getTimeOut()).setTaskExecutor(asyncExecutor)
                  .registerCallableInterceptors(callableProcessingInterceptor());
        super.configureAsyncSupport(configurer);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public DomainClassConverter<?>      domainClassConverter() {
        return new DomainClassConverter<FormattingConversionService>(mvcConversionService());
    }
    
    @Bean
    public CallableProcessingInterceptor callableProcessingInterceptor() {
        return new TimeoutCallableProcessingInterceptor();
    }
}