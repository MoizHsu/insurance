package com.jason.service;

import java.util.List;

import com.jason.model.OrderKafkaCheckedDTO;
import com.jason.model.entity.order.OrderEntity;
import com.jason.model.request.order.CreateOrderRequestDTO;

public interface OrderService {
    /**
     * Create an order
     *
     * @param insuranceEntity 保單內容
     * @param userId 保單編號
     * @author jason
     * @CreateTime 2024/3/x
     * @Return OrderID 訂單編號
     */
    OrderEntity CreateOrder(CreateOrderRequestDTO CreateOrderRequestDTO);

     /**
     * Cancel an order
     *
     * @param orderId 訂單編號
     * @author jason
     * @CreateTime 2024/3/x
     * @Return OrderID 訂單編號
     */
    OrderEntity CancelOrder(String userId,String orderId);

     /**
     * List all orders with specific user
     *
     * @param userID 會員編號
     * @author jason
     * @CreateTime 2024/3/x
     * @Return OrderID 訂單編號
     */
     List<OrderEntity> ListOrders(String userId, int page, int size);


      /**
     * 訂單確認狀態更新
     *
     * @param userID 會員編號
     * @author jason
     * @CreateTime 2024/3/x
     * @Return OrderID 訂單編號
     */
     void UpdateOrderCheckedStatus(OrderKafkaCheckedDTO orderKafkaCheckedDTO);
}
