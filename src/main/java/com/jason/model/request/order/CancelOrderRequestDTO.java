package com.jason.model.request.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CancelOrderRequestDTO {
    @NotBlank
    public String userId;

    @NotBlank
    public String orderId;
}
