package com.jason.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.jason.model.ClaimRequestDTO;
import com.jason.model.OrderKafkaDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaOrderMessagingService {

@Autowired
private KafkaTemplate<String, OrderKafkaDTO> kafkaOrderTemplate;

@Autowired
private KafkaTemplate<String, ClaimRequestDTO> kafkaClaimTemplate;
  
  public void sendOrder(OrderKafkaDTO order) {
    kafkaOrderTemplate.send("order", order.getId(), order);
    System.out.println("Order sent: " + order);
  }

  public void sendClaimRequest(ClaimRequestDTO claimRequestDTO) {
    kafkaClaimTemplate.send("claim", claimRequestDTO); // , UUID.randomUUID().toString()
    System.out.println("Claim insurance: " + claimRequestDTO);
  }

  
}