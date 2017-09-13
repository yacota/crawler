/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.service;

import com.jl.sample.model.CrawlResult;
import com.jl.sample.model.CrawlResultRepository;
import java.net.URI;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 *
 * @author jllach
 */
public class CrawlerCallbackTest {
    
    private static final String URL = "http://www.google.es";

    @Test
    public void testCallbackResponseKO() {
        CrawlResultRepository mockRepo = mock(CrawlResultRepository.class);
        CrawlerCallback crawlerCallback = new CrawlerCallback(mockRepo, string -> {return false;}); //when reponse is ko predicate does not matter

        ListenableFutureCallback<ResponseEntity<String>> promise = crawlerCallback.callback(URL);
        promise.onFailure(new Exception("exception message"));

        verify(mockRepo, times(1)).save(new CrawlResult(URL, CrawlResult.RESULT.ERROR, "exception message"));
    }

    @Test
    public void testCallbackResponseOKAndPredicateOK() {
        CrawlResultRepository mockRepo = mock(CrawlResultRepository.class);
        CrawlerCallback crawlerCallback = new CrawlerCallback(mockRepo, string -> {return true;});
        
        ListenableFutureCallback<ResponseEntity<String>> promise = crawlerCallback.callback(URL);
        promise.onSuccess(ResponseEntity.ok().build());
        
        verify(mockRepo, times(1)).save(new CrawlResult(URL, CrawlResult.RESULT.MARFEELIZABLE));
    }

    // we see a 301 when a redirect from http -> https happens, in all the other cases we see a 200, that's why we differentiate it    
    @Test
    public void testCallbackResponseOKAndPredicateOKBut301StatusCode() throws Exception {
        final String redirectedSite = "http://www.google.com"; // not mocking JDK IOtuils behaviour thus this site must exist
        
        CrawlResultRepository mockRepo = mock(CrawlResultRepository.class);
        CrawlerCallback crawlerCallback = new CrawlerCallback(mockRepo, string -> {return true;});
        
        ListenableFutureCallback<ResponseEntity<String>> promise = crawlerCallback.callback(URL);
        promise.onSuccess(ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(new URI(redirectedSite)).build());
        
        // we store the original URL
        verify(mockRepo, times(1)).save(new CrawlResult(URL, CrawlResult.RESULT.MARFEELIZABLE));
    }

    @Test
    public void testCallbackResponseOKAndPredicateOKButUnexpectedStatusCode() {
        CrawlResultRepository mockRepo = mock(CrawlResultRepository.class);
        CrawlerCallback crawlerCallback = new CrawlerCallback(mockRepo, string -> {return true;});
        
        ListenableFutureCallback<ResponseEntity<String>> promise = crawlerCallback.callback(URL);
        promise.onSuccess(ResponseEntity.status(HttpStatus.IM_USED).build());
        
        verify(mockRepo, times(1)).save(new CrawlResult(URL, CrawlResult.RESULT.ERROR, CrawlerCallback.ERROR_CODE_MESSAGE + HttpStatus.IM_USED));
    }

    @Test
    public void testCallbackResponseOKAndPredicateKO() {
        CrawlResultRepository mockRepo = mock(CrawlResultRepository.class);
        CrawlerCallback crawlerCallback = new CrawlerCallback(mockRepo, string -> {return false;});
        
        ListenableFutureCallback<ResponseEntity<String>> promise = crawlerCallback.callback(URL);
        promise.onSuccess(ResponseEntity.ok().build());
        
        verify(mockRepo, times(1)).save(new CrawlResult(URL, CrawlResult.RESULT.NOT_MARFEELIZABLE));
    }
}