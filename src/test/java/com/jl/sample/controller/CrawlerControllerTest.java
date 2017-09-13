/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.controller;

import com.jl.sample.service.CrawlerService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.Before;
import static org.mockito.Mockito.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author jllach
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CrawlerControllerTestConfiguration.class})
@WebAppConfiguration
public class CrawlerControllerTest {
    
    private final String SINGLE_SITE            = "{\"url\":\"www.elperiodico.com\", \"rank\": 62738}";
    private final String MULTIPLE_SITE          = "[{\"url\":\"www.elperiodico.com\", \"rank\": 62738}, {\"url\":\"www.sport.es\", \"rank\": 2323}]";
    
    private final String MULTIPLE_CRAFTED_SITE  = "[{\"url\":\"\", \"rank\": 62738}, {\"url\":\"www.sport.es\", \"rank\": 2323}]";
    
    @Autowired
    private CrawlerService serviceMock;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    
    
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() {
        reset(serviceMock);
    }

    //
    // Tests
    //
    
    @Test
    public void testCrawlSingleNoContent() throws Exception {
        mockMvc.perform(post("/crawl/single").content("{}").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        verify(serviceMock, never()).crawl(any(String.class)); 
    }

    @Test
    public void testCrawlSingle() throws Exception {
        mockMvc.perform(post("/crawl/single").content(SINGLE_SITE).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isAccepted());
        verify(serviceMock, times(1)).crawl("www.elperiodico.com");
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testCrawlMultipleNoContent() throws Exception {
        mockMvc.perform(post("/crawl/multiple").content("[]").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        verify(serviceMock, never()).crawl(any(String.class)); 
    }
    
    @Test
    public void testCrawlMultiple() throws Exception {
        mockMvc.perform(post("/crawl/multiple").content(MULTIPLE_SITE).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isAccepted());
        verify(serviceMock, times(2)).crawl(any(String.class)); 
        verify(serviceMock, times(1)).crawl("www.elperiodico.com");
        verify(serviceMock, times(1)).crawl("www.sport.es");
        verifyNoMoreInteractions(serviceMock);
    }
    
    @Test
    public void testCrawlMultipleCrafted() throws Exception {
        mockMvc.perform(post("/crawl/multiple").content(MULTIPLE_CRAFTED_SITE).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isAccepted());
        verify(serviceMock, times(1)).crawl("www.sport.es");
        verifyNoMoreInteractions(serviceMock);
    }
}