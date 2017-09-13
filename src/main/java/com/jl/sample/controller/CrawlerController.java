/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.controller;

import com.jl.sample.service.CrawlerService;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jllach
 */
@RestController
@RequestMapping(value="/crawl")
public class CrawlerController {
    
    private static final Logger logger = LoggerFactory.getLogger(CrawlerController.class);
    
    @Autowired
    private CrawlerService crawlService;
    
    @RequestMapping(value="/single", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<?>               crawlSingle(@RequestBody Site site) {
        if (site != null && site.isValid()) {
            crawlService.crawl(site.getUrl());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @RequestMapping(value="/multiple", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<?>               crawlMultiple(@RequestBody Site[] siteList) {
        if (siteList == null || siteList.length == 0) {
            if (logger.isInfoEnabled()) logger.info("crawlme : invoked but no Site can be found in the json payload");
            return ResponseEntity.noContent().build();
        } else {
            Arrays.asList(siteList).stream().filter(Site::isValid).forEach(site -> crawlService.crawl(site.getUrl()));
            return ResponseEntity.accepted().build();
        }
    }
}