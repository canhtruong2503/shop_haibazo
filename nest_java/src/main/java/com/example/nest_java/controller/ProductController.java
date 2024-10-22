package com.example.nest_java.controller;

import com.example.nest_java.dto.ApiResponse;
import com.example.nest_java.dto.ImageDTO;
import com.example.nest_java.dto.ProductDTO;
import com.example.nest_java.dto.response.auth.UserDTO;
import com.example.nest_java.mapper.ProductMapper;
import com.example.nest_java.model.entity.Image;
import com.example.nest_java.model.entity.Product;
import com.example.nest_java.model.entity.User;
import com.example.nest_java.repository.ProductRepository;
import com.example.nest_java.service.AuthenticationService;
import com.example.nest_java.service.BaseService;
import com.example.nest_java.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController extends BaseController<Product, ProductDTO, UUID> {
    private final ProductMapper productMapper;

    private final AuthenticationService authenticationService;

    private final ProductRepository productRepository;

    private final ProductService productService;

    public ProductController(BaseService<Product, ProductDTO, UUID> baseService, ProductMapper productMapper, AuthenticationService authenticationService, ProductRepository productRepository, ProductService productService) {

        super(baseService);
        this.productMapper = productMapper;
        this.authenticationService = authenticationService;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @Override
    @PostMapping
    @Operation(summary = "tạo product", description = "Phương thức này tạo mới một product ")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ProductDTO productDTO) {

        return ResponseEntity.ok(ApiResponse.created(productService.create(productDTO)));
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "sửa product", description = "Phương thức này sửa product và thêm các field giảm giá.")
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") UUID id,
            @RequestBody @Valid ProductDTO dto
    ) {

        return ResponseEntity.ok(ApiResponse.success(productService.update(id, dto)));
    }
}
