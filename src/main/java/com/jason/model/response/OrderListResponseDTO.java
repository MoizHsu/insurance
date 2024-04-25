package com.jason.model.response;

import java.util.List;

import com.jason.model.entity.order.OrderEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderListResponseDTO {
    protected String status;
    List<OrderEntity> list;
}
