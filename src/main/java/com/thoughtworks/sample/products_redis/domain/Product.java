package com.thoughtworks.sample.products_redis.domain;

import com.thoughtworks.sample.products_redis.listener.GenericCacheInvalidationListener;
import com.thoughtworks.sample.products_redis.listener.ProductCacheInvalidationListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(ProductCacheInvalidationListener.class)
public class Product implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Double price;

    private String seller;

    @Transient
    public static final String
            allItemCacheName = "allProductsCache",
            allItemCacheKey = "allProducts",
            singleItemCacheName= "product",
            singleItemCacheKeyPrefix= "product-";
}
