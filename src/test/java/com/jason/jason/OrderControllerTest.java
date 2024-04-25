package com.jason.jason;

import com.jason.model.entity.InsuranceEntity;
import com.jason.model.entity.order.OrderEntity;
import com.jason.model.entity.order.OrderStatus;
import com.jason.model.request.order.CreateOrderRequestDTO;
import com.jason.model.request.order.InsuranceRequestDTO;
import com.jason.repository.InsuranceRepository;
import com.jason.repository.OrderRepository;
import com.jason.service.OrderService;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * 測試OrderController 對內service與外http通訊
 *
 * @author Jason
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest // (classes = OrderController.class)
@AutoConfigureMockMvc
@Slf4j
public class OrderControllerTest {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService orderService;

    @MockBean 
    OrderRepository orderRepository;

    @MockBean
    InsuranceRepository insuranceRepository;


    @Test
    @WithMockUser(username = "jasonQQ")
    public void getOrdersAPI() throws Exception 
    {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/order/list")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "jasonQQ")
    public void createOrdersAPI() throws Exception 
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

        Mockito.when(orderService.CreateOrder(createOrderRequestDTO)).thenReturn(expectedOrderEntity);

        Mockito.when(orderRepository.save(any(OrderEntity.class))).thenReturn(expectedOrderEntity);
        Mockito.when(insuranceRepository.save(any(InsuranceEntity.class))).thenReturn(expectedInsuranceEntity);
        
        String response = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createOrderRequestDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @WithMockUser(username = "jasonQQ")
    public void testListOrders() throws Exception {
        // setting data expected list
        List<OrderEntity> expectedList = new ArrayList();
        OrderEntity testOrder = OrderEntity.builder()
        .id("testOrderId")
        .userId("jason")
        .status(OrderStatus.PENDING_PAYMENT).build();
        expectedList.add(testOrder);

        // mocking todoService.ListOrders
        Mockito.when(orderService.ListOrders("jason", 0, 0)).thenReturn(expectedList);

        // mocking [GET] /api/order/list
        String returnString = mockMvc.perform(MockMvcRequestBuilders.get("/api/order/list")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        // check if the expectedList is same as the actualList
        //assertEquals(expectedList, actualList);
    }
}