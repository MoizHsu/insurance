package com.jason.model.request.order;

import com.jason.exception.RestfulException;
import com.jason.model.response.ResponseCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateOrderRequestDTO {
    @NotBlank
    private String userId;

    @NotNull
    private InsuranceRequestDTO insurance;

    public void CheckRequest() {
        // check if userId 's length < 5
        if (userId.length() < 5) {
            throw new RestfulException(ResponseCode.QUERY_PARAMETER_ERROR, "userId's length shoud > 5");
        }
    }
}
