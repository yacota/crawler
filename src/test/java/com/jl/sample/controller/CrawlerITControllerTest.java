/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.controller;

import com.jl.sample.model.CrawlResult;
import com.jl.sample.model.CrawlResultRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.Before;
import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.After;
import org.springframework.test.context.TestPropertySource;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 *
 * @author jllach
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CrawlerITControllerTestConfiguration.class})
@TestPropertySource(properties = {"jl.defaults.words=news,noticias,notícies"})
@WebAppConfiguration
public class CrawlerITControllerTest {
    
    private final String MARFFELIZABLE_URL          = "www.elperiodico.com";
    private final String MARFFELIZABLE_URL_REDIRECT = "www.elperiodico.cat";
    private final String HTTPS_URL_REDIRECT         = "c-and-a.com";
    private final String NOT_MARFEELIZABLE_URL      = "www.revistacuore.com";

    private final String SINGLE_SITE            = "{\"url\":\""+MARFFELIZABLE_URL+"\", \"rank\": 62738}";
    private final String SINGLE_SITE_REDIRECT   = "{\"url\":\""+MARFFELIZABLE_URL_REDIRECT+"\", \"rank\": 62738}";
    private final String HTTPS_SITE_REDIRECT    = "{\"url\":\""+HTTPS_URL_REDIRECT+"\", \"rank\": 62738}";
    private final String MULTIPLE_SITE          = "[{\"url\":\""+MARFFELIZABLE_URL+"\", \"rank\": 62738}, {\"url\":\""+NOT_MARFEELIZABLE_URL+"\", \"rank\": 2323}]";
    
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CrawlResultRepository repository;
    
    private MockMvc mockMvc;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() {
        repository.deleteAll(); //remove all 
    }

    //
    // Tests
    //
    
    @Test
    public void testCrawlSingle() throws Exception {
        long numberBefore = repository.count();
        mockMvc.perform(post("/crawl/single").content(SINGLE_SITE).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isAccepted());

        Thread.sleep(5000); // its async so we have to wait a little

        assertThat(repository.count(), is(equalTo(numberBefore+1)));
        CrawlResult result = repository.findByUrl(MARFFELIZABLE_URL);
        assertThat(result.getResult(), is(CrawlResult.RESULT.MARFEELIZABLE));
        assertThat(result.getOpDate(), is(notNullValue()));
        assertThat(result.getReason(), is(nullValue()));
    }
    
    /**
     * www.elperiodico.cat -> www.elperiodico.cat/ca
     * @throws Exception 
     */
    @Test
    public void testCrawlSingleWithRedirect() throws Exception {
        long numberBefore = repository.count();
        mockMvc.perform(post("/crawl/single").content(SINGLE_SITE_REDIRECT).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isAccepted());

        Thread.sleep(15000); // its async so we have to wait a little

        assertThat(repository.count(), is(equalTo(numberBefore+1)));
        CrawlResult result = repository.findByUrl(MARFFELIZABLE_URL_REDIRECT);
        assertThat(result.getResult(), is(CrawlResult.RESULT.MARFEELIZABLE));
        assertThat(result.getOpDate(), is(notNullValue()));
        assertThat(result.getReason(), is(nullValue()));
    }
    
    /**
     * http://www.c-and-a.com -> https://www.c-and-a.com/es/es/shop
     * @throws Exception 
     */
    @Test
    public void testCrawlRedirectToHttps() throws Exception {
        //http://www.c-and-a.com
        mockMvc.perform(post("/crawl/single").content(HTTPS_SITE_REDIRECT).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isAccepted());

        Thread.sleep(15000); // its async so we have to wait a little

        CrawlResult result = repository.findByUrl(HTTPS_URL_REDIRECT); // we store the original url
        assertThat(result.getResult(), is(CrawlResult.RESULT.NOT_MARFEELIZABLE));
        assertThat(result.getOpDate(), is(notNullValue()));
        assertThat(result.getReason(), is(nullValue()));
    }
    
    @Test
    public void testCrawlMultiple() throws Exception {
        long numberBefore = repository.count();
        mockMvc.perform(post("/crawl/multiple").content(MULTIPLE_SITE).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isAccepted());
        
        Thread.sleep(5000); // its async so we have to wait a little

        assertThat(repository.count(), is(equalTo(numberBefore+2)));
        CrawlResult result = repository.findByUrl(MARFFELIZABLE_URL);
        assertThat(result.getResult(), is(CrawlResult.RESULT.MARFEELIZABLE));
        assertThat(result.getOpDate(), is(notNullValue()));
        assertThat(result.getReason(), is(nullValue()));
        
        result = repository.findByUrl(NOT_MARFEELIZABLE_URL);
        assertThat(result.getResult(), is(CrawlResult.RESULT.NOT_MARFEELIZABLE));
        assertThat(result.getOpDate(), is(notNullValue()));
        assertThat(result.getReason(), is(nullValue()));
    }
}