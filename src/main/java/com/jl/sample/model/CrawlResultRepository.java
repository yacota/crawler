/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.model;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jllach
 */
@Repository
public interface CrawlResultRepository 
extends          CrudRepository<CrawlResult, Serializable> {
    
    public List<CrawlResult> findByResult(CrawlResult.RESULT result);
    
    public CrawlResult       findByUrl(String url);
}