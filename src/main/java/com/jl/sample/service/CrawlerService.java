/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;

/**
 *
 * @author jllach
 */
public class CrawlerService {
    
    private static final Logger L = LoggerFactory.getLogger(CrawlerService.class);

    private final FetchService      fetchService;
    private final CrawlerCallback   callback;

    public CrawlerService(FetchService fetchService, CrawlerCallback callback) {
        Assert.notNull(fetchService, "CrawlerService : FetchService can not be null");
        Assert.notNull(callback,     "CrawlerService : CrawlerCallback can not be null");
        //this.urls         = ConcurrentHashMap.<String>newKeySet();
        this.fetchService = fetchService;
        this.callback     = callback;
    }

    @Async
    public void     crawl(String url) {
        try {
            this.fetchService.fetch(url, callback.callback(url));
        } catch (RuntimeException e) {
            L.error("crawl error while processing url "+url +" due to "+e.getMessage());
        }
    }
}