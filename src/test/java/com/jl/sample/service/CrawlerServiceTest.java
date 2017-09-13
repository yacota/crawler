/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.service;

import org.junit.Test;
import static org.mockito.Mockito.*;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 *
 * @author jllach
 */
public class CrawlerServiceTest {
    
    @Test
    public void testCrawl() {
        final String    url = "someurl";

        FetchService    fetchServiceMock     = mock(FetchService.class);
        CrawlerCallback callbackMock         = mock(CrawlerCallback.class);
        ListenableFutureCallback promiseMock = mock(ListenableFutureCallback.class);

        when(callbackMock.callback(url)).thenReturn(promiseMock);
        
        CrawlerService service = new CrawlerService(fetchServiceMock, callbackMock);
        service.crawl(url);
        
        verify(fetchServiceMock, times(1)).fetch(url, promiseMock);
        verifyNoMoreInteractions(fetchServiceMock);
    }
}