package com.jason.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jason.model.request.order.CreateOrderRequestDTO;
import com.jason.model.InsuranceKafkaDTO;
import com.jason.model.InsuranceKafkaDTO.InsuranceKafkaDTOBuilder;
import com.jason.model.OrderKafkaDTO;
import com.jason.model.entity.InsuranceEntity;
import com.jason.model.entity.order.OrderEntity;
import com.jason.model.request.order.CancelOrderRequestDTO;
import com.jason.model.response.OrderListResponseDTO;
import com.jason.model.response.ResponseCode;
import com.jason.model.response.GeneralHttpResponseDTO;
import com.jason.service.KafkaOrderMessagingService;
import com.jason.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {

    @Autowired
	OrderService orderService;

    @Autowired
    KafkaOrderMessagingService kafkaOrderMessagingService;

    @Operation(summary = "Create an order", description = "Create an order")
    @RequestMapping("/create")
    public ResponseEntity<GeneralHttpResponseDTO> createOrder(@RequestBody CreateOrderRequestDTO orderRequestDTO, Authentication authentication){
        // data validation
        orderRequestDTO.CheckRequest();
        // ---
        if (!orderRequestDTO.getUserId().equals(authentication.getName())){
            return ResponseEntity.ok(GeneralHttpResponseDTO.builder().status("unautherized").build());
        }

        OrderEntity orderEntity = this.orderService.CreateOrder(orderRequestDTO);
        InsuranceEntity insuranceEntity = orderEntity.getInsurance();

        InsuranceKafkaDTO insuranceKafkaDTO = InsuranceKafkaDTO.builder()
        .id(insuranceEntity.getId())
        .name(insuranceEntity.getName())
        .startTime(insuranceEntity.getStartTime())
        .endTime(insuranceEntity.getStartTime())
        .mainＣontract(insuranceEntity.getMainＣontract())
        .subＣontract(insuranceEntity.getSubＣontract())
        .additionalDic(insuranceEntity.getAdditionalDic())
        .userId(insuranceEntity.getUserId())
        .build();

        // build kafka msg and send order to kafka cluster
        OrderKafkaDTO orderKafkaDTO = OrderKafkaDTO.builder()
        .id(orderEntity.getId())
        .date(orderEntity.getDate())
        .userId(orderEntity.getUserId())
        .insuranceDTO(insuranceKafkaDTO)
        .status(orderEntity.getStatus())
        .build();

        this.kafkaOrderMessagingService.sendOrder(orderKafkaDTO);

        return ResponseEntity.ok(GeneralHttpResponseDTO.builder()
        .status(ResponseCode.SUCCESS.get())
        .build());
    }


    @Operation(summary = "Cancel an order", description = "Cancel an order")
    @RequestMapping("/cancel")
    public ResponseEntity<GeneralHttpResponseDTO>  cancelOrder(@RequestBody CancelOrderRequestDTO cancelOrderRequest,Authentication authentication){
        if (!cancelOrderRequest.getUserId().equals(authentication.getName())){
            return ResponseEntity.ok(GeneralHttpResponseDTO.builder().status("unautherized").build());
        }
        this.orderService.CancelOrder(cancelOrderRequest.userId, cancelOrderRequest.orderId);
        return ResponseEntity.ok(GeneralHttpResponseDTO.builder()
        .status(ResponseCode.SUCCESS.get())
        .build());
    }


    @Operation(summary = "list user's orders", description = "list user's orders")
    @GetMapping("list")
    public ResponseEntity<OrderListResponseDTO> getOrderList(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "10") int size, Authentication authentication) {
        //UserEntity user = this.userService.getUserByAccount(authentication.getName());
        List<OrderEntity> list = this.orderService.ListOrders(authentication.getName(), page, size);

        return ResponseEntity.ok(OrderListResponseDTO.builder()
        .status(ResponseCode.SUCCESS.get())
        .list(list)
        .build());
    }
}
