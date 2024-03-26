package com.jason.model.request.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequestDTO {
    @NotBlank
    private String userId;

    @NotNull
    private InsuranceRequestDTO insurance;
}
