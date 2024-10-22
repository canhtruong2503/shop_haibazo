package com.example.nest_java.validate.validator;

import com.example.nest_java.repository.UserRepository;
import com.example.nest_java.validate.anotationValidate.UniqueEmail;
import com.example.nest_java.validate.anotationValidate.UniqueUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private UserRepository userRepository;
    // kiểm tra khi register Email đã được sử dụng chưa.
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true;
        }
        return !userRepository.existsByEmail(email); // Kiểm tra nếu email đã tồn tại
    }
}
