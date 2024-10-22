package com.example.nest_java.mapper;

import com.example.nest_java.dto.request.auth.UserRegistration;
import com.example.nest_java.dto.response.auth.AuthenticationResponse;
import com.example.nest_java.dto.response.auth.UserDTO;
import com.example.nest_java.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Mapper(componentModel = "spring", // tạo ra dưới dạng 1 bean trong spring
        // thuộc tính obj nguồn = null, thì thuộc tính obj đích = gt hiện tại or null
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper extends BaseMapper<User, UserDTO> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Override
    @Mapping(target = "password", constant = "password")
    User convertToEntity(UserDTO dto);

    @Override
    @Mapping(source = "password", target = "password", ignore = true)
    UserDTO convertToDTO(User entity);

    @Mapping(source = "username", target = "username")
    User convertForRegister(UserRegistration userRegistration);

    // Map User -> AuthenticationResponse
    default AuthenticationResponse convertToAuthenticationResponse(User user, String token) {
        UserDTO userDTO = convertToDTO(user);
        return new AuthenticationResponse(userDTO, token);
    }

    @Override
    default Optional<UserDTO> convertOptional(Optional<User> user) {
        return user.map(e -> convertToDTO(e));
    }
}
