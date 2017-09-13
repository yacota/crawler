/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample;

import com.jl.sample.config.AsyncConfiguration;
import com.jl.sample.config.DefaultsConfiguration;
import com.jl.sample.config.MongoDBConfiguration;
import com.jl.sample.config.ServicesConfiguration;
import com.jl.sample.config.WebMvcConfiguration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 *
 * @author jllach
 */
public class WebAppInitializer 
extends      AbstractAnnotationConfigDispatcherServletInitializer {
 
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{DefaultsConfiguration.class, AsyncConfiguration.class, MongoDBConfiguration.class, ServicesConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebMvcConfiguration.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}