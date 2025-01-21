package com.ehcache.controller;

import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;

@Controller("/cache")
public class CacheController {

    @Cacheable("default-cache")
    @Get("/get")
    public String getFromCache() {
        return "Hello ehcache, ";
    }
}