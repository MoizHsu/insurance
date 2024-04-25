package com.jason.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;

import com.jason.model.ClaimRequestDTO;
import com.jason.model.request.order.CreateOrderRequestDTO;
import com.jason.model.response.GeneralHttpResponseDTO;
import com.jason.model.response.ResponseCode;
import com.jason.service.KafkaOrderMessagingService;
import com.jason.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/insurance")
@Slf4j
public class ClaimInsuranceController {
    @Autowired
	OrderService orderService;

    @Autowired
    KafkaOrderMessagingService kafkaOrderMessagingService;

    @Operation(summary = "Claim an insurance", description = "Claim an insurance")
    @RequestMapping("/claim")
    public ResponseEntity<GeneralHttpResponseDTO> claimInsurance(@RequestBody ClaimRequestDTO claimRequestDTO, Authentication authentication){


        this.kafkaOrderMessagingService.sendClaimRequest(claimRequestDTO);


        return ResponseEntity.ok(GeneralHttpResponseDTO.builder()
        .status(ResponseCode.SUCCESS.get())
        .build());
        
    }
}
