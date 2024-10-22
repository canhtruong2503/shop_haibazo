package com.example.nest_java.service;

import com.example.nest_java.dto.ProductDTO;
import com.example.nest_java.mapper.ProductMapper;
import com.example.nest_java.model.entity.Product;
import com.example.nest_java.model.entity.User;
import com.example.nest_java.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService extends BaseService<Product, ProductDTO, UUID> {
    private final ProductMapper productMapper;

    private final ProductRepository productRepository;
    private final AuthenticationService authenticationService;
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, AuthenticationService authenticationService) {

        super(productRepository, productMapper);
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.authenticationService = authenticationService;
    }
    public ProductDTO create(ProductDTO productDTO) {

        User user = authenticationService.getCurrentUser();

        Product product = productMapper.convertToEntity(productDTO);
        if (product.getImages() != null) {
            product.getImages().forEach(image -> image.setProduct(product));
        }
        if (product.getSizeProducts() != null) {
            product.getSizeProducts().forEach(sizeProduct -> sizeProduct.setProduct(product));
        }
        product.setUser(user);

        productRepository.save(product);

        return productMapper.convertToDTO(product);
    }
    @Transactional
    public ProductDTO update(UUID id, ProductDTO productDTO) {

        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + id));

        Product updated = productMapper.updateEntity(productDTO, product);

        updated.setId(id);
        if (product.getImages() != null) {
            product.getImages().forEach(image -> image.setProduct(product));
        }
        if (product.getSizeProducts() != null) {
            product.getSizeProducts().forEach(sizeProduct -> sizeProduct.setProduct(product));
        }
        productRepository.save(updated);

        return productMapper.convertToDTO(updated);
    }
}
