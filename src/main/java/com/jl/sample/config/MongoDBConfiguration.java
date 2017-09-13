/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.config;

import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 *
 * @author jllach
 */
@Configuration
@EnableMongoRepositories(value = "com.jl.sample.model")
public class MongoDBConfiguration {
    
    @Autowired
    private DefaultsConfiguration config;

    @Bean
    public MongoClientFactoryBean   mongo() {
        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        mongo.setHost(config.getMongoHost());
        mongo.setPort(config.getMongoPort());
        return mongo;
    }

    @Bean
    public MongoOperations          mongoTemplate(Mongo mongo) {
        //TODO remove _class when writing
        return new MongoTemplate(mongo, config.getMongoDBName());
    }
}