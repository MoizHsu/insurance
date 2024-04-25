package com.jason.model.response.user;

import com.jason.model.entity.Role;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetUserInfoResponseDTO {
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String account;

    @NotBlank
    private String identification;

    @Digits(fraction = 0, integer = 10)
    private String telephone;

    @NotNull
    private Role role;
}
