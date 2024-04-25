package com.jason.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jason.model.OrderKafkaCheckedDTO;
import com.jason.model.entity.InsuranceEntity;
import com.jason.model.entity.order.OrderEntity;
import com.jason.model.entity.order.OrderStatus;
import com.jason.model.request.order.CreateOrderRequestDTO;
import com.jason.model.request.order.InsuranceRequestDTO;
import com.jason.repository.InsuranceRepository;
import com.jason.repository.OrderRepository;
import com.jason.service.OrderService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    InsuranceRepository insuranceRepository;

    @Autowired
    OrderRepository orderRepository;

    @Transactional
    @Override
    public OrderEntity CreateOrder(CreateOrderRequestDTO createOrderRequestDTO) {      
        InsuranceRequestDTO insuranceDTO = createOrderRequestDTO.getInsurance();
        InsuranceEntity insuranceEntity = InsuranceEntity.builder()
        .mainＣontract(insuranceDTO.getMainＣontract())
        .subＣontract(insuranceDTO.getSubContract())
        .startTime(insuranceDTO.getStartTime())
        .endTime(insuranceDTO.getEndTime())
        .userId(createOrderRequestDTO.getUserId())
        .build();
        insuranceRepository.save(insuranceEntity);

        // transcation part 2: create order combine insuranceEntity and the user
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setInsurance(insuranceEntity);
        orderEntity.setStatus(OrderStatus.PENDING_PAYMENT);
        orderEntity.setUserId(createOrderRequestDTO.getUserId());
        orderEntity.setDate(LocalDateTime.now());
        return orderRepository.save(orderEntity);
    }

    @Override
    public OrderEntity CancelOrder(String userId, String orderID) {
        // find user by userID
        Optional<OrderEntity> order = this.orderRepository.findById(orderID);
        OrderEntity orderE = order.get();
        orderE.setStatus(OrderStatus.CANCELED);
        return this.orderRepository.save(orderE);
    }

    @Override
    public List<OrderEntity> ListOrders(String userId, int page, int size) {
        Page<OrderEntity> pageResult = this.orderRepository.findByUserId(userId,
            PageRequest.of(page, size,Sort.by("date").descending()));
        //     pageResult.getNumberOfElements();
        //     pageResult.getSize();
        //     pageResult.getTotalElements();
        //     pageResult.getTotalPages();
        // Page<OrderEntity> pageResult = this.orderRepository.findAll(
        // PageRequest.of(page, size,Sort.by("date").descending()));
        // pageResult.getNumberOfElements();
        // pageResult.getSize();
        // pageResult.getTotalElements();
        // pageResult.getTotalPages();

        List<OrderEntity> orderList = pageResult.getContent();
        return orderList;
    }

    @Override
    public void UpdateOrderCheckedStatus(OrderKafkaCheckedDTO orderKafkaCheckedDTO) {
        // TODO Auto-generated method stub

        Optional<OrderEntity> order = this.orderRepository.findById(orderKafkaCheckedDTO.getId());
        OrderEntity orderE = order.get();
        if (orderKafkaCheckedDTO.getChecked()) {
            orderE.setStatus(OrderStatus.COMPLETED);
        }else{
            orderE.setStatus(OrderStatus.CANCELED);
        }
    }
}
