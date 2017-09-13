/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.config;

import com.jl.sample.model.CrawlResultRepository;
import com.jl.sample.service.CrawlerCallback;
import com.jl.sample.service.CrawlerService;
import com.jl.sample.service.FetchService;
import java.util.List;
import java.util.function.Predicate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

/**
 *
 * @author jllach
 */
@Configuration
public class ServicesConfiguration {
    
    @Autowired
    private DefaultsConfiguration   defaultConfig;
    @Autowired
    private ThreadPoolTaskExecutor  taskExecutor;
    
    @Bean @Autowired
    public CrawlerService       crawlerService(CrawlResultRepository crawlRepo) {
        return new CrawlerService(fetchService(defaultClientHttpRequestFactory()), crawlerCallback(crawlRepo));
    }

    protected CrawlerCallback   crawlerCallback(CrawlResultRepository crawlRepo) {
        return new CrawlerCallback(crawlRepo, isMarfeelizable(defaultConfig.getWords()));
    }
    
    protected FetchService      fetchService(AsyncClientHttpRequestFactory requestFactory) {
        return new FetchService(requestFactory);
    }

    protected Predicate<String> isMarfeelizable(final List<String> enclosing) {
        // JLL : works but when predicate is executed enclosing can not be evaluated as it is out of lambda scope, using the second one as it is equivalent and can be debugged
//        return (String html) -> {
//                final Document doc = html != null ? Jsoup.parse(html) : null;
//                this.toString();
//                return doc != null && !StringUtils.isEmpty(doc.title()) ? 
//                              enclosing.stream().filter(word -> doc.title().contains(word)).findFirst().isPresent() : false;
//        };
        return new Predicate<String>() {
            
            private List<String> words;

            // trick : http://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html#jls-8.6
            {
                this.words = enclosing;
            }
            
            @Override
            public boolean test(String html) {
                final Document doc = html != null ? Jsoup.parse(html) : null;
                return doc != null && !StringUtils.isEmpty(doc.title()) ? 
                              this.words.stream().filter(word -> logic(word, doc.title())).findFirst().isPresent() : false;
            }

            private boolean logic(String word, String title) {
                return title.contains(word);
            }
        };
    }

    protected AsyncClientHttpRequestFactory     defaultClientHttpRequestFactory() {
        // TODO impl based on JDK facilities only ... no pooling! use apache commons instead, but I can not use any other external dependency :(
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setTaskExecutor(taskExecutor);
        clientHttpRequestFactory.setConnectTimeout(defaultConfig.getFetchTimeOut());
        return clientHttpRequestFactory;
    }
}