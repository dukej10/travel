package com.dukez.best_travel.config;

import java.util.Map;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import com.dukez.best_travel.util.consts.CacheConstants;

import lombok.extern.slf4j.Slf4j;

@Configuration

@EnableCaching
@Slf4j
public class RedisConfig {

    @Value("${cache.redis.address}")
    private String serverAddres;

    @Value("${cache.redis.password}")
    private String serverPassword;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(serverAddres)
                .setPassword(serverPassword);
        return Redisson.create(config);
    }

    @Bean
    @Autowired // Para poder inyectar el RedissonClient
    public CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> configs = Map.of(
                CacheConstants.FLY_CACHE_NAME, new CacheConfig(),
                CacheConstants.HOTEL_CACHE_NAME, new CacheConfig());
        return new RedissonSpringCacheManager(redissonClient, configs);
    }

    /**
     * Programación para limpiar la caché
     */
    @CacheEvict(cacheNames = {
            CacheConstants.FLY_CACHE_NAME,
            CacheConstants.HOTEL_CACHE_NAME
    }, allEntries = true) // Limpiar los caches indicados
    @Scheduled(cron = CacheConstants.SCHEDULED_RESET_CACHE)
    @Async
    public void deleteCache() {
        log.info("Clean cache");
    }
}
