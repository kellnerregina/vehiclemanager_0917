package com.example.vehiclemanager.config;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public javax.cache.CacheManager customCacheManager() {
        // Programozott Ehcache 3.x CacheManager létrehozása
        CachingProvider provider = Caching.getCachingProvider();
        javax.cache.CacheManager cacheManager = provider.getCacheManager();

        // Ehcache cache programozott konfigurációval
        CacheManager ehCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("vehiclesCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                java.util.UUID.class,
                                java.util.List.class,
                                ResourcePoolsBuilder.heap(1000)))
                .build();

        ehCacheManager.init();

        return cacheManager;
    }
}