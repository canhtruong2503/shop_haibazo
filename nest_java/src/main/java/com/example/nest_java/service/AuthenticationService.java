package com.example.nest_java.service;

import com.example.nest_java.dto.ApiResponse;
import com.example.nest_java.dto.request.auth.UserLogin;
import com.example.nest_java.dto.request.auth.UserRegistration;
import com.example.nest_java.dto.response.auth.AuthenticationResponse;
import com.example.nest_java.dto.response.auth.UserDTO;
import com.example.nest_java.mapper.UserMapper;
import com.example.nest_java.model.entity.User;
import com.example.nest_java.model.enums.Role;
import com.example.nest_java.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthenticationService extends BaseService<User, UserDTO, UUID> {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    protected AuthenticationService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService , JpaSpecificationExecutor<User> jpaSpecificationExecutor) {

        super(userRepository, userMapper,jpaSpecificationExecutor);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse register(UserRegistration userRegistration) {

        User user = userMapper.convertForRegister(userRegistration);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return userMapper.convertToAuthenticationResponse(user, jwtToken);
    }

    public ResponseEntity<ApiResponse> login(UserLogin userLogin) {

        var user = userRepository.findByEmail(userLogin.getEmail()).orElse(null);

        if (user == null) {
            return ResponseEntity.ok(ApiResponse.unauthorized());
        }
        if (!passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(ApiResponse.unauthorized());
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        userLogin.getPassword()
                )
        );
        var jwtToken = jwtService.generateToken(user);

        UserDTO userDto = userMapper.convertToDTO(user);

        return ResponseEntity.ok(ApiResponse.success(new AuthenticationResponse(userDto, jwtToken)));
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepository.findByUsername(userEmail).orElseThrow();
    }
}
