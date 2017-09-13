/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.misc;

import com.jl.sample.service.FetchService;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author jllach
 */
public class MiscTest {
    
    //private static final String URL  = "http://chulojuegos.com";
    private static final String URL = "http://planetminecraft.com";
    // ajedrezonline.com planetminecraft.com mo2o.com forocoches.com
    
    @Test(expected = HttpClientErrorException.class)
    public void requestWithoutUserAgent_EndsInForbidden() throws IOException {
        requestWithOrWitoutUserAgent(URL, null);
    }
    
    @Test
    public void requestWithUserAgent_WorksFine() throws IOException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("User-Agent", FetchService.MARFEEL_BOT_UA);
        HttpEntity<?> httpEntity = new HttpEntity<Object>(requestHeaders);
        
        requestWithOrWitoutUserAgent(URL, httpEntity);
    }

    //
    // Internal ones
    //
    
    private void requestWithOrWitoutUserAgent(String url, HttpEntity<?> httpEntity) throws IOException {
        Document parse = Jsoup.parse(new URL(url), 5000);
        assertThat(parse, is((notNullValue())));

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        assertThat(exchange, is((notNullValue())));
        if (exchange.getStatusCodeValue() == HttpStatus.MOVED_PERMANENTLY.value() || 
            exchange.getStatusCodeValue() == HttpStatus.MOVED_TEMPORARILY.value()) {
            String location = exchange.getHeaders().getLocation().toString();
            assertThat(location, is((notNullValue())));
            requestWithOrWitoutUserAgent(location, httpEntity);
        } else {
            assertThat(exchange.getBody(), is((notNullValue())));
        }
    }
}