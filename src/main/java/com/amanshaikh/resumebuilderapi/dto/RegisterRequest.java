package com.amanshaikh.resumebuilderapi.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class RegisterRequest {
    
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters")
    private String password;

    private String profileImageUrl;
    
}
