/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jl.sample.config;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jllach
 */
public class MarfeelizableTest {
    
    private static final List<String> WORDS = Arrays.asList("noticias","news");
    
    @Test
    public void testIsMarfeelizableNoticias() {
        ServicesConfiguration instance = new ServicesConfiguration();
        Predicate<String> result = instance.isMarfeelizable(WORDS);
        assertTrue(result.test("<html><head><title>noticias</title></head></html>"));
    }
    
    @Test
    public void testIsMarfeelizableDeportes() {
        ServicesConfiguration instance = new ServicesConfiguration();
        Predicate<String> result = instance.isMarfeelizable(Arrays.asList("deporte"));
        assertTrue(result.test("<html><head><title>noticias de deporte</title></head></html>"));
    }

    @Test
    public void testIsMarfeelizable() {
        ServicesConfiguration instance = new ServicesConfiguration();
        Predicate<String> result = instance.isMarfeelizable(WORDS);
        assertTrue(result.test("<html><head><title>noticias news deportes</title></head></html>"));
    }
    
    @Test
    public void testIsNotMarfeelizable() {
        ServicesConfiguration instance = new ServicesConfiguration();
        Predicate<String> result = instance.isMarfeelizable(WORDS);
        assertFalse(result.test("<html><head><title>deportes</title></head></html>"));
    }

    @Test
    public void testIsNotMarfeelizableBecauseOfEmpty() {
        ServicesConfiguration instance = new ServicesConfiguration();
        Predicate<String> result = instance.isMarfeelizable(WORDS);
        assertFalse(result.test(""));
    }
}