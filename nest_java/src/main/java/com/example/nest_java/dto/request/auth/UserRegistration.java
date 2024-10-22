package com.example.nest_java.dto.request.auth;

import com.example.nest_java.dto.BaseDTO;
import com.example.nest_java.validate.anotationValidate.UniqueEmail;
import com.example.nest_java.validate.anotationValidate.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegistration extends BaseDTO<UUID> {
    @NotBlank(message = "Name not blank")
    @Size(min = 1, max = 30, message = "Size name error")
    @Pattern(regexp = "^[^\\d\\s]\\D*$", message = "Format error for name")
    private String name;
    @UniqueUsername
    @Pattern(regexp = "^[^\\d\\s]\\D*$", message = "Format error for userName")
    private String username;
    @UniqueEmail
    @Email(message = "Email invalid")
    private String email;
    @Pattern(regexp = "^(84|0[3|5|7|8|9])[0-9]{8}$", message = "Format error for phoneNumber")
    private String phoneNumber;
    private String password;
}
