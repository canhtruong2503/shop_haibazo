package com.example.nest_java.repository;

import com.example.nest_java.model.entity.Product;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.tokens.Token;

import java.util.UUID;
@Repository
public interface ProductRepository extends BaseRepository<Product, UUID>{

    Product findByName(String name);
}
