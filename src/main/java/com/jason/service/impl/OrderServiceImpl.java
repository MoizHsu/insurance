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


import com.jason.model.OrderStatus;
import com.jason.model.entity.InsuranceEntity;
import com.jason.model.entity.OrderEntity;
import com.jason.model.entity.UserEntity;
import com.jason.model.request.order.CreateOrderRequestDTO;
import com.jason.model.request.order.InsuranceRequestDTO;
import com.jason.repository.InsuranceRepository;
import com.jason.repository.OrderRepository;
import com.jason.repository.UserRepository;
import com.jason.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
	UserRepository userRepository;

    @Autowired
    InsuranceRepository insuranceRepository;

    @Autowired
    OrderRepository orderRepository;

    //@Transactional
    @Override
    public String CreateOrder(CreateOrderRequestDTO createOrderRequestDTO) {      
        // find user by userID
        Optional<UserEntity> user = this.userRepository.findById(createOrderRequestDTO.getUserId());

        InsuranceRequestDTO insuranceDTO = createOrderRequestDTO.getInsurance();
        InsuranceEntity insuranceEntity = InsuranceEntity.builder()
        .mainＣontract(insuranceDTO.getMainＣontract())
        .subＣontract(insuranceDTO.getSubContract())
        .startTime(insuranceDTO.getStartTime())
        .endTime(insuranceDTO.getEndTime())
        .user(user.get())
        .build();

        if (user.get() == null) {
            log.info("i am null");
        }
        this.insuranceRepository.save(insuranceEntity);

        // transcation part 2: create order combine insuranceEntity and the user
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setInsurance(insuranceEntity);
        orderEntity.setStatus(OrderStatus.PENDING_PAYMENT);
        orderEntity.setUser(user.get());
        orderEntity.setDate(LocalDateTime.now());
        OrderEntity no = this.orderRepository.save(orderEntity);

        log.info("orderEntity saved");
        return no.getId();
    }

    @Override
    public void CancelOrder(String userId, String orderID) {
        // find user by userID
        Optional<OrderEntity> order = this.orderRepository.findById(orderID);
        order.get().setStatus(OrderStatus.CANCELED);
        this.orderRepository.save(order.get());
    }

    @Override
    public List<OrderEntity> ListOrders(UserEntity user, int page, int size) {
        Page<OrderEntity> pageResult = this.orderRepository.findByUser(user,
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
}
