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
import com.jason.model.entity.OrderEntity;
import com.jason.model.entity.UserEntity;
import com.jason.model.request.order.CancelOrderRequestDTO;
import com.jason.model.response.OrderListResponseDTO;
import com.jason.model.response.GeneralHttpResponseDTO;
import com.jason.service.OrderService;
import com.jason.service.UserService;

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
    UserService userService;

    @Operation(summary = "Create an order", description = "Retrieves a list of all books")
    @RequestMapping("/create")
    public ResponseEntity<GeneralHttpResponseDTO> createOrder(@RequestBody CreateOrderRequestDTO orderRequestDTO, Authentication authentication){

        String userId = this.userService.getUserIdByAccount(authentication.getName());

        log.info("id:"+userId+"....."+orderRequestDTO.getUserId());

        if (!userId.equals(orderRequestDTO.getUserId())) {
            return ResponseEntity.ok(GeneralHttpResponseDTO.builder().status("unautherized").build());
        }

        this.orderService.CreateOrder(orderRequestDTO);
        return ResponseEntity.ok(GeneralHttpResponseDTO.builder()
        .status("success")
        .build());
    }


    @Operation(summary = "Cancel an order", description = "Cancel an order")
    @RequestMapping("/cancel")
    public ResponseEntity<GeneralHttpResponseDTO>  cancelOrder(@RequestBody CancelOrderRequestDTO cancelOrderRequest,Authentication authentication){
        String checkedUserId = this.userService.getUserIdByAccount(authentication.getName());
        log.info(checkedUserId, cancelOrderRequest.userId);
        if (checkedUserId != cancelOrderRequest.userId) {
            return ResponseEntity.ok(GeneralHttpResponseDTO.builder().status("wrong user id").build());
        }
        this.orderService.CancelOrder(cancelOrderRequest.userId, cancelOrderRequest.orderId);
        return ResponseEntity.ok(GeneralHttpResponseDTO.builder()
        .status("success")
        .build());
    }

    @Operation(summary = "list user's orders", description = "list user's orders")
    @GetMapping("list")
    public ResponseEntity<OrderListResponseDTO> getOrderList(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "10") int size, Authentication authentication) {
        UserEntity user = this.userService.getUserByAccount(authentication.getName());
        List<OrderEntity> list = this.orderService.ListOrders(user, page, size);

        return ResponseEntity.ok(OrderListResponseDTO.builder()
        .status("success")
        .list(list)
        .build());
    }
}
