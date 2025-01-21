package com.ehcache;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Factory
public class CacheConfig {

    @Singleton
    public PersistentCacheManager cacheManager() {
        File storageDir = new File("C:/Git-Repo/eh-micronut/eh-cache-ide/src/test/cache");

        PersistentCacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .with(new CacheManagerPersistenceConfiguration(storageDir))
                .withCache("default-cache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String.class,
                                byte[].class,
                                ResourcePoolsBuilder.heap(100)
                        ).withExpiry(Expirations.timeToLiveExpiration(Duration.of(60, TimeUnit.SECONDS)))
                ).build(true);

        return cacheManager;
    }

    @Singleton
    public Cache<String, byte[]> defaultCache(PersistentCacheManager cacheManager) {
        return cacheManager.getCache("default-cache", String.class, byte[].class);
    }
}
