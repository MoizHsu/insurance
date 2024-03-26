package com.jason.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String account;

    @NotBlank 
    private String password;

    @NotBlank
    private String identification;
}
