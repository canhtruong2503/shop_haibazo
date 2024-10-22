package com.example.nest_java.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Đăng ký module JSR310 để hỗ trợ các kiểu thời gian Java 8 như Instant
        objectMapper.registerModule(new JavaTimeModule());
        // Đăng ký module Jdk8Module để hỗ trợ các kiểu optional Java 8
        objectMapper.registerModule(new Jdk8Module());
        return objectMapper;
    }

}