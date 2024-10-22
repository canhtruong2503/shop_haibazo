package com.example.nest_java.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BaseMapper<E, DTO> {
    DTO convertToDTO(E entity);

    E convertToEntity(DTO dto);

    List<DTO> convertListToDTO(List<E> listE);

    @Mapping(target = "content", expression = "java(page.map(this::convertToDTO))")
    default Page<DTO> convertListToDTO(Page<E> page) {
        return page.map(this::convertToDTO);
    }
    List<E> convertListToEntity(List<DTO> dtoList);

    Optional<DTO> convertOptional(Optional<E> optionalE);

    // ánh xạ dữ liệu từ DTO vào entity này
    E updateEntity(DTO dto, @MappingTarget E entity);
}

