package com.example.nest_java.mapper;

import com.example.nest_java.dto.ImageDTO;
import com.example.nest_java.dto.ProductDTO;
import com.example.nest_java.dto.request.auth.UserRegistration;
import com.example.nest_java.dto.response.auth.AuthenticationResponse;
import com.example.nest_java.dto.response.auth.UserDTO;
import com.example.nest_java.model.entity.Image;
import com.example.nest_java.model.entity.Product;
import com.example.nest_java.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", // tạo ra dưới dạng 1 bean trong spring
        // thuộc tính obj nguồn = null, thì thuộc tính obj đích = gt hiện tại or null
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper extends BaseMapper<Product, ProductDTO> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    @Override
    ProductDTO convertToDTO(Product entity);

    @Override
    default Optional<ProductDTO> convertOptional(Optional<Product> product) {

        return product.map(e -> convertToDTO(e));
    }

    @Override
    @Mapping(target = "images", source = "images")
    @Mapping(target = "sizeProducts", source = "sizeProducts")
    Product convertToEntity(ProductDTO dto);

//    @Override
//    @Mapping(target = "images", source = "images")
//    @Mapping(target = "sizeProducts", source = "sizeProducts")
//    Product updateEntity(ProductDTO dto, @MappingTarget Product entity);

}
