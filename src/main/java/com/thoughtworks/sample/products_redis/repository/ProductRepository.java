package com.thoughtworks.sample.products_redis.repository;

import com.thoughtworks.sample.products_redis.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByName(String name);
}
