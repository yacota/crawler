/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.model;

import java.util.Date;
import java.util.Objects;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.Assert;

/**
 *
 * @author jllach
 */
@Document(collection = "CrawlResults")
public class CrawlResult {
    
    public enum RESULT {
        MARFEELIZABLE, NOT_MARFEELIZABLE, ERROR;
    }

    @Field("URL") @Indexed // findByUrl
    private final String    url;
    @Field("RESULT")  @Indexed // findByResult
    private final RESULT    result;
    @Field("REASON")
    private final String    reason;
    @Field("OP_DATE")
    private final Date      opDate;
    
    //TODO we could store http status code

    public CrawlResult(String url, RESULT result) {
        this(url, result, null);
    }
    
    @PersistenceConstructor
    public CrawlResult(String url, RESULT result, String reason) {
        Assert.notNull(url, "CrawlResult can not be null!");
        Assert.notNull(result, "Result can not be null!");
        this.url    = url;
        this.result = result;
        this.reason = reason;
        this.opDate = new Date();
    }

    public String getUrl() {
        return this.url;
    }

    public RESULT getResult() {
        return this.result;
    }

    public String getReason() {
        return this.reason;
    }

    public Date   getOpDate() {
        return this.opDate;
    }
    
    @Override
    public int      hashCode() {
        return Objects.hash(this.url, this.result, this.reason);
    }
    
    @Override
    public boolean  equals(Object obj) {
        if (obj != null && obj instanceof CrawlResult) {
            CrawlResult inc = (CrawlResult)obj;
            return this.hashCode() == inc.hashCode();
        }
        return false;
    }
}