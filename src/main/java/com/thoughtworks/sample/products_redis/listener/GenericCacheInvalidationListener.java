package com.thoughtworks.sample.products_redis.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Slf4j
@Component
public class GenericCacheInvalidationListener {

    @Autowired
    private CacheManager cacheManager;

    @PostPersist
    public void afterCreate(Object entity) throws NoSuchFieldException, IllegalAccessException {
        String cacheName = getEntityFieldValue(entity, "allItemCacheName").toString();
        String cacheKey = getEntityFieldValue(entity, "allItemCacheKey").toString();

        log.info("Evicting caches: {} - {}", cacheName, cacheKey);
        evictCacheByName(cacheName, cacheKey);
    }

    @PostRemove
    public void afterDelete(Object entity) throws NoSuchFieldException, IllegalAccessException {
        String entityId = getEntityFieldValue(entity, "id").toString();

        String allItemCacheName = getEntityFieldValue(entity, "allItemCacheName").toString();
        String allItemCacheKey = getEntityFieldValue(entity, "allItemCacheKey").toString();

        String itemCacheName = getEntityFieldValue(entity, "singleItemCacheName").toString();
        String itemCacheKey =
                getEntityFieldValue(entity, "singleItemCacheKeyPrefix").toString() + entityId;

        log.info("Evicting caches: {} - {}", allItemCacheName, allItemCacheKey);
        evictCacheByName(allItemCacheName, allItemCacheKey);

        log.info("Evicting cache item: {} - {}", itemCacheName, itemCacheKey);
        evictCacheByName(itemCacheName, itemCacheKey);
    }

    private void evictCacheByName(String cacheName, String cacheKey) {
        Cache productsCache = cacheManager.getCache(cacheName);
        if(productsCache!=null) {
            productsCache.evict(cacheKey);
        }
    }

    private Object getEntityFieldValue(Object entity, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = entity.getClass().getDeclaredField(fieldName);
        declaredField.setAccessible(true);
        return declaredField.get(entity);
    }
}
