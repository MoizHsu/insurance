package com.jason.jason;

import com.jason.controller.OrderController;
import com.jason.model.entity.InsuranceEntity;
import com.jason.model.entity.order.OrderEntity;
import com.jason.model.entity.order.OrderStatus;
import com.jason.model.request.order.CreateOrderRequestDTO;
import com.jason.model.request.order.InsuranceRequestDTO;
import com.jason.repository.InsuranceRepository;
import com.jason.repository.OrderRepository;
import com.jason.service.OrderService;
import com.jason.service.impl.OrderServiceImpl;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 測試OrderService
 *
 * @author Jason
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest (classes = OrderServiceImpl.class)
//@AutoConfigureMockMvc
@Slf4j
public class OrderServiceTest {
    @Autowired
    OrderService orderService;

    @MockBean 
    OrderRepository orderRepository;

    @MockBean
    InsuranceRepository insuranceRepository;

    @Test
    @WithMockUser(username = "jason")
    public void createOrderTest() throws Exception 
    {
        InsuranceRequestDTO mockInsuranceDTO = InsuranceRequestDTO.builder()
                                            .name("insurance")
                                            .mainＣontract("123")
                                            .subContract("321")
                                            .startTime(LocalDateTime.now())
                                            .endTime(LocalDateTime.now())
                                            .build();

        CreateOrderRequestDTO createOrderRequestDTO = CreateOrderRequestDTO.builder()
                                            .userId("jason")
                                            .insurance(mockInsuranceDTO)
                                            .build();

        InsuranceEntity expectedInsuranceEntity = InsuranceEntity.builder()
        .mainＣontract(mockInsuranceDTO.getMainＣontract())
        .subＣontract(mockInsuranceDTO.getSubContract())
        .startTime(mockInsuranceDTO.getStartTime())
        .endTime(mockInsuranceDTO.getEndTime())
        .build();

        OrderEntity expectedOrderEntity = new OrderEntity();
        expectedOrderEntity.setId("123");
        expectedOrderEntity.setInsurance(expectedInsuranceEntity);
        expectedOrderEntity.setStatus(OrderStatus.PENDING_PAYMENT);
        expectedOrderEntity.setUserId(createOrderRequestDTO.getUserId());
        expectedOrderEntity.setDate(LocalDateTime.now());

        // Mockito.when(todoDao.save(todo)).thenReturn(todo);
        // mocking save() return expected value
        Mockito.when(orderRepository.save(any(OrderEntity.class))).thenReturn(expectedOrderEntity);
        Mockito.when(insuranceRepository.save(any(InsuranceEntity.class))).thenReturn(expectedInsuranceEntity);

        // [Act] CreateOrder
        OrderEntity order = orderService.CreateOrder(createOrderRequestDTO);

        // [Assert] expected data & actual data
        assertEquals(order.getId(), expectedOrderEntity.getId());
    }

    @Test
    @WithMockUser(username = "jason")
    public void listOrdersTest() throws Exception {
        // setting data expected list
        List<OrderEntity> orderslist = new ArrayList<OrderEntity>();
        OrderEntity testOrder = OrderEntity.builder()
        .id("testOrderId")
        .userId("jason")
        .status(OrderStatus.PENDING_PAYMENT).build();
        orderslist.add(testOrder);

        Page<OrderEntity> expectedPage = new PageImpl<OrderEntity>(orderslist);

        // mocking todoService.ListOrders
        Mockito.when(orderRepository.findByUserId("jason",
        PageRequest.of(0, 5, Sort.by("date").descending()))).thenReturn(expectedPage);

        List<OrderEntity> actualTodoList = orderService.ListOrders("jason", 0, 5);

        assertEquals(actualTodoList, orderslist);
    }

    @Test
    @WithMockUser(username = "jason")
    public void cancelOrderTest() throws Exception {
        List<OrderEntity> orderslist = new ArrayList<OrderEntity>();

        OrderEntity testOrder = OrderEntity.builder()
        .id("testOrderId")
        .userId("jason")
        .status(OrderStatus.PENDING_PAYMENT).build();
        orderslist.add(testOrder);

        Mockito.when(orderRepository.save(testOrder)).thenReturn(testOrder);

        OrderEntity updatedOrder = OrderEntity.builder()
        .id("testOrderId")
        .userId("jason")
        .status(OrderStatus.CANCELED).build();
        orderslist.add(testOrder);

        Mockito.when(orderRepository.save(updatedOrder)).thenReturn(updatedOrder);
        Optional<OrderEntity> order = Optional.of(testOrder);

        Mockito.when(orderRepository.findById(eq("testOrderId"))).thenReturn(order);

        // mock cancel order [from status = OrderStatus.PENDING_PAYMENT]
        // [Act] CancelOrder
        OrderEntity od = orderService.CancelOrder("jason","testOrderId");
        
        assertEquals(od.getStatus(), OrderStatus.CANCELED);
    }
}