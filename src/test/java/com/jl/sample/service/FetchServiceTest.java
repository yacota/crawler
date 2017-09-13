/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.service;

import org.junit.Test;
import static org.mockito.Mockito.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

/**
 *
 * @author jllach
 */
public class FetchServiceTest {
    
    @Test
    public void testFetch() {
        final String url = "someurl";

        AsyncClientHttpRequestFactory mockFactory  = mock(SimpleClientHttpRequestFactory.class); //there's a cast in the asyncresttemplate to this class
        AsyncRestTemplate             mockTemplate = mock(AsyncRestTemplate.class);
        ListenableFuture<ResponseEntity<String>> asyncReponse = mock(ListenableFuture.class);
        ListenableFutureCallback                 promiseMock  = mock(ListenableFutureCallback.class);

        FetchService service = new FetchService(mockFactory);
        service.setRestTemplate(mockTemplate);
        
        when(mockTemplate.exchange("http://"+url, HttpMethod.GET, service.getHttpEntity(), String.class)).thenReturn(asyncReponse);

        service.fetch(url, promiseMock);
        
        verify(mockTemplate, times(1)).exchange("http://"+url, HttpMethod.GET, service.getHttpEntity(), String.class);
        verify(asyncReponse, times(1)).addCallback(promiseMock);
        
        verifyZeroInteractions(mockFactory);
        verifyZeroInteractions(promiseMock);
    }
}
