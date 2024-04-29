package com.thoughtworks.sample.products_redis.service;

import com.thoughtworks.sample.products_redis.domain.Product;
import com.thoughtworks.sample.products_redis.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @CacheEvict(value = "allProductsCache", key = "'allProducts'")
    public Product createProduct(Product product) {
        List<Product> productsOfSameName = productRepository.findByName(product.getName());
        if(productsOfSameName.isEmpty()) {
            return productRepository.save(product);
        }
        throw new HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "A product with the same name already exists"
        );
    }

    //productsCache is the name of the cache
    //'allProducts' key - a unique identifier for the cached data, will update the cache based on the key value change
    //in Spring Expression Language single quotes are used to denote string literals
    @Cacheable(value = "allProductsCache", key = "'allProducts'")
    public List<Product> getAllProducts() {
        log.info("Fetching all product details from Database");
        return productRepository.findAll();
    }

    @Cacheable(value = "product", key = "'product-' + #id")
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            log.info("Fetching product details from Database for: id {}", id);
            return product.get();
        }
        throw new HttpClientErrorException(
                HttpStatus.NOT_FOUND,
                "The product with id: " + id + " doesn't exist"
        );
    }

    @Caching(evict = {
            @CacheEvict(value = "allProductsCache", key = "'allProducts'"),
            @CacheEvict(value = "product", key = "'product-' + #id")
    })
    public Product removeProduct(Long id) {
        Optional<Product> productEntity = productRepository.findById(id);
        if(productEntity.isPresent()){
            productRepository.delete(productEntity.get());
            return productEntity.get();
        }
        throw new HttpClientErrorException(
                HttpStatus.NOT_FOUND,
                "The product with id: " + id + " doesn't exist"
        );
    }
}
