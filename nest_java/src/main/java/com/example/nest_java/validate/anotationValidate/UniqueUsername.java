package com.example.nest_java.validate.anotationValidate;

import com.example.nest_java.validate.validator.UniqueUsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


// Chỉ định rằng đây là một constraint cho việc validate
@Constraint(validatedBy = UniqueUsernameValidator.class)
// Định nghĩa annotation có thể được sử dụng trên các field và method
@Target({ElementType.FIELD, ElementType.METHOD})
// Chỉ định thông tin giữ lại khi chạy
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {

    String message() default "Username already exists"; // Thông báo lỗi mặc định

    Class<?>[] groups() default {}; // Nhóm thông báo

    Class<? extends Payload>[] payload() default {}; // Thông tin tùy chọn
}