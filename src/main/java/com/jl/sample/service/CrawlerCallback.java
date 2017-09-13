/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.service;

import com.jl.sample.model.CrawlResult;
import com.jl.sample.model.CrawlResultRepository;
import java.util.function.Predicate;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 *
 * @author jllach
 */
public class CrawlerCallback {

    private static final Logger L = LoggerFactory.getLogger(CrawlerCallback.class);

    static final String ERROR_CODE_MESSAGE = "Error code : ";
    
    private final CrawlResultRepository crawlRepo;
    private final Predicate<String>     predicate;
    
    public CrawlerCallback(CrawlResultRepository crawlRepo, Predicate<String> predicate) {
        Assert.notNull(crawlRepo, "CrawlerCallback : CrawlResultRepository can not be null");
        Assert.notNull(predicate, "CrawlerCallback : predicate matcher can not be null");
        this.crawlRepo = crawlRepo;
        this.predicate = predicate;
    }

    public ListenableFutureCallback<ResponseEntity<String>>     callback(String url) {
        return new ListenableFutureCallback<ResponseEntity<String>>() {
            @Override
            public void onSuccess(ResponseEntity<String> result) {
                if (L.isInfoEnabled()) L.info("Html response received (async callable)");
                
                if        (HttpStatus.OK.value() == result.getStatusCodeValue()) {
                    callbackInternal(result.getBody());
                } else if (HttpStatus.MOVED_PERMANENTLY.value()  == result.getStatusCodeValue() || 
                           HttpStatus.MOVED_TEMPORARILY.value()  == result.getStatusCodeValue()) {
                    try {
                        // NOTE JDK SimpleClientHttpRequestFactory | Spring Resttemaplte should follow redirects ... should debug, open a bug or something
                        // http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4620571  O_O

                        // fallback directly using Jsoup
                        String html = Jsoup.connect(result.getHeaders().getLocation().toString()).execute().body();
                        callbackInternal(html);
                    } catch (Exception ex) {
                        onFailure(ex);
                    }
                } else {
                    // TODO : unknown host exception like huesca.es --> www.huesca.es are not taken into account
                    saveCrawlResult(url, CrawlResult.RESULT.ERROR, ERROR_CODE_MESSAGE + result.getStatusCode());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                saveCrawlResult(url, CrawlResult.RESULT.ERROR, t.getMessage());
            }

            private void    callbackInternal(String html) {
                CrawlResult.RESULT status = CrawlResult.RESULT.NOT_MARFEELIZABLE;
                if (predicate.test(html)) {
                    status = CrawlResult.RESULT.MARFEELIZABLE;
                }
                saveCrawlResult(url, status, null);
            }
        };
    }

    private void  saveCrawlResult(String url, CrawlResult.RESULT result, String reason) {
        this.crawlRepo.save(new CrawlResult(url, result, reason));
    }
}