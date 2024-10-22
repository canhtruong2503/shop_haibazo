package com.example.nest_java.dto.response.auth;

import com.example.nest_java.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO extends BaseDTO<UUID> {
    @NotBlank(message = "Name not blank")
    @Size(min = 1, max = 30, message = "size name error")
    @Pattern(regexp = "^[^\\d\\s]\\D*$", message = "format error for name")
    private String name;
    @Size(min = 3, max = 30, message = "size username error")
    @Pattern(regexp = "[a-zA-Z0-9\\._\\-]", message = "format error for username")
    private String username;
    @JsonIgnore
    private String password;
    @Email(message = "Email invalid")
    private String email;
    private LocalDate dob;
    @Size(min = 10, max = 11, message = "size phone number error")
    @Pattern(regexp = "/(84|0[3|5|7|8|9])+([0-9]{8})\\b/g", message = "format error for phone number")
    private String phoneNumber;
    private String avatar;
}
