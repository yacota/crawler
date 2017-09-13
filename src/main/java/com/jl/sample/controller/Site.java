/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.controller;

import org.springframework.util.StringUtils;

/**
 *
 * @author jllach
 */
class Site {
    
    private String    url;
    private long      rank;

    // jackson sucks
    public Site() {}
    
    public Site(String url, long rank) {
        this.url = url;
        this.rank = rank;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }

    public String getUrl() {
        return this.url;
    }

    public long getRank() {
        return rank;
    }

    public boolean isValid() {
        // TODO http://commons.apache.org/proper/commons-validator/apidocs/org/apache/commons/validator/routines/UrlValidator.html would be handy
        return !StringUtils.isEmpty(this.url);
    }
}