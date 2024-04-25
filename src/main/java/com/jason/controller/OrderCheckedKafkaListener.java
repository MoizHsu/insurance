package com.jason.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.jason.model.OrderKafkaCheckedDTO;
import com.jason.model.entity.order.OrderEntity;
import com.jason.service.KafkaOrderMessagingService;
// import com.kafka.jason.kafka.model.OrderKafkaCheckedDTO;
// import com.kafka.jason.kafka.service.KafkaOrderMessagingService;
import com.jason.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderCheckedKafkaListener {
    @Autowired
    OrderService orderService;

    @KafkaListener(topics = "check1", groupId = "check-group")
    public void handle(OrderKafkaCheckedDTO orderChecked) {
        if (orderChecked.getChecked()){
            orderService.UpdateOrderCheckedStatus(orderChecked);
            log.info("abcd ",orderChecked.toString(), " updated!");
        }
    }
}
