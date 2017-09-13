/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.controller;

import com.jl.sample.config.AsyncConfiguration;
import com.jl.sample.config.DefaultsConfiguration;
import com.jl.sample.config.MongoDBConfiguration;
import com.jl.sample.config.ServicesConfiguration;
import com.jl.sample.config.WebMvcConfiguration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author jllach
 */
@Import({DefaultsConfiguration.class, AsyncConfiguration.class, MongoDBConfiguration.class, ServicesConfiguration.class,
         WebMvcConfiguration.class})
public class CrawlerITControllerTestConfiguration {

}
