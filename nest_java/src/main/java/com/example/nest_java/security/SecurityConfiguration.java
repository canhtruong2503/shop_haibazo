package com.example.nest_java.security;

import com.example.nest_java.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Data
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())  // Vô hiệu hóa CSRF theo cú pháp mới
                .authorizeHttpRequests(authorize -> authorize
                               // .anyRequest().permitAll()
                      //  .requestMatchers("/product/**").hasAuthority("ADMIN")
                        .requestMatchers("/auth/**", "/product/**","/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/webjars/**","/product/filter/**").permitAll()
                     .anyRequest().authenticated() // Yêu cầu xác thực cho các yêu cầu còn lại
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)  // Thêm authentication provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)  // Thêm JWT filter trước UsernamePasswordAuthenticationFilter
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // Xử lý khi không có hoặc token không hợp lệ
                        .authenticationEntryPoint(unauthorizedHandler(new ObjectMapper()))
                        // Xử lý khi người dùng không có quyền truy cập
                        .accessDeniedHandler(accessDeniedHandler(new ObjectMapper()))
                );

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedHandler(ObjectMapper objectMapper) {

        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ApiResponse apiResponse = ApiResponse.unauthorized();

            // Đăng ký JavaTimeModule cho ObjectMapper (nếu chưa đăng ký trước)
            objectMapper.registerModule(new JavaTimeModule());

            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper) {

        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ApiResponse apiResponse = ApiResponse.forbidden();
            objectMapper.registerModule(new JavaTimeModule());

            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        };
    }

}