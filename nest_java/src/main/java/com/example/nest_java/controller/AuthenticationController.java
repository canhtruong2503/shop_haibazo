package com.example.nest_java.controller;

import com.example.nest_java.dto.ApiResponse;
import com.example.nest_java.dto.request.auth.UserLogin;
import com.example.nest_java.dto.request.auth.UserRegistration;
import com.example.nest_java.dto.response.auth.AuthenticationResponse;
import com.example.nest_java.dto.response.auth.UserDTO;
import com.example.nest_java.model.entity.User;
import com.example.nest_java.service.AuthenticationService;
import com.example.nest_java.service.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController extends BaseController<User ,UserDTO ,UUID> {
    private final AuthenticationService authenticationService;

    protected AuthenticationController(BaseService<User, UserDTO, UUID> baseService, AuthenticationService authenticationService) {
        super(baseService);
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @Operation(summary = "register", description = "Phương thức này để đăng ký một user mới.")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserRegistration userRegistration) {

        return ResponseEntity.ok(ApiResponse.created(authenticationService.register(userRegistration)));

    }

    @PostMapping("/login")
    @Operation(summary = "login", description = "Phương thức này quản lý authen author user.")

    public ResponseEntity<ApiResponse> login(@Valid @RequestBody UserLogin userLogin) {

        return authenticationService.login(userLogin);
    }
}
