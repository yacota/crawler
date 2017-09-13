/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.service;

import java.nio.charset.Charset;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

/**
 * @author jllach
 */
public class FetchService {

    private static final Logger L = LoggerFactory.getLogger(FetchService.class);
    
    // avoids forbidden as tested in MiscTest
    //private static final String GOOGLE_BOT_UA = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    public  static final String MARFEEL_BOT_UA = "Mozilla/5.0 (compatible; Marfeelbot/2.1;)";
    private static final String HTTP           = "http";
    
    private AsyncRestTemplate    restTemplate;
    private final HttpEntity<?>  httpEntity;
    
    public FetchService(AsyncClientHttpRequestFactory factory) {
        Assert.notNull(factory, "AsyncClientHttpRequestFactory can not be null, check its hierarchy in order to find the appropiate impl, caveat: you might need to add some dependencies");
        this.restTemplate = new AsyncRestTemplate(factory);
        //to avoid encoding issues
        this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        this.httpEntity = buildHeaders();
    }

    public void fetch(String url, ListenableFutureCallback<ResponseEntity<String>> callback) {
        if (!StringUtil.isBlank(url)) {
            if (!url.startsWith("http")) { //http https are ok
                url = HTTP  + "://" + url;
            }
            this.restTemplate.exchange(url.trim(), HttpMethod.GET, httpEntity, String.class).addCallback(callback);
        }
    }

    private HttpEntity<?>   buildHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set(HttpHeaders.USER_AGENT, MARFEEL_BOT_UA);
        return new HttpEntity<Object>(requestHeaders);
    }
    
    //
    // package visibility for test purposes
    //

    void                    setRestTemplate(AsyncRestTemplate template) {
        this.restTemplate = template;
    }

    HttpEntity<?>           getHttpEntity() {
        return httpEntity;
    }
}