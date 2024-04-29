package com.thoughtworks.sample.products_redis.listener;

import com.thoughtworks.sample.products_redis.domain.Product;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

@Slf4j
public class ProductCacheInvalidationListener {

    @PostPersist
    @CacheEvict(value = "allProductsCache", key = "'allProducts'")
    public void afterProductCreate(Product product) {
        log.info("Cache invalidation after creating product: {} - {}",
                product.getName(), product.getId());
    }

    @PostRemove
    @Caching(evict = {
            @CacheEvict(value = "allProductsCache", key = "'allProducts'"),
            @CacheEvict(value = "product", key = "'product-' + #product.getId()")
    })
    public void afterProductDeleted(Product product) {
        log.info("Cache invalidation after deleting product: {} - {}",
                product.getName(), product.getId());
    }

}
