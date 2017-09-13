/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.config;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 *
 * @author jllach
 */
@Configuration
@PropertySource(value = {"classpath:defaults.properties"}, ignoreResourceNotFound = true)
public class DefaultsConfiguration {

    @Value("${jl.defaults.timeOut:5000}")
    private long            timeOut;
    @Value("${jl.defaults.fetchTimeOut:5000}")
    private int             fetchTimeOut;
    @Value("${jl.defaults.corePoolSize:10}")
    private int             corePoolSize;
    @Value("${jl.defaults.maxPoolSize:100}")
    private int             maxPoolSize;
    @Value("#{'${jl.defaults.words}'.split(',')}")
    private List<String>    words;
    // mongo ones
    @Value("${jl.defaults.mongoHost:localhost}")
    private String          mongoHost;
    @Value("${jl.defaults.mongoPort:27017}")
    private int             mongoPort;
    @Value("${jl.defaults.mongoDBName:crawler}")
    private String          mongoDBName;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() { 
        return new PropertySourcesPlaceholderConfigurer();
    } 

    public long getTimeOut() {
        return timeOut;
    }

    public int  getFetchTimeOut() {
        return fetchTimeOut;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public List<String> getWords() {
        return words == null ? Collections.EMPTY_LIST : words;
    }

    public String getMongoHost() {
        return mongoHost;
    }

    public int getMongoPort() {
        return mongoPort;
    }

    public String getMongoDBName() {
        return mongoDBName;
    }
}