package com.example.nest_java.validate.validator;

import com.example.nest_java.repository.UserRepository;
import com.example.nest_java.validate.anotationValidate.UniqueUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    @Autowired
    private UserRepository userRepository;
    // kiểm tra khi register username đã được sử dụng chưa.
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return true; // Không kiểm tra null nếu bạn có annotation @NotNull
        }
        return !userRepository.existsByUsername(username); // Kiểm tra nếu username đã tồn tại
    }
}
