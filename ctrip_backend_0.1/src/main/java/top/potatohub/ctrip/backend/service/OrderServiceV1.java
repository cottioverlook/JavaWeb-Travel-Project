package top.potatohub.ctrip.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.potatohub.ctrip.backend.entities.Order;
import top.potatohub.ctrip.backend.mapper.OrderMapper;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceV1 implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order createOrder(Order order) {
        if (order.getId() == null) {
            order.setId(UUID.randomUUID().toString());
        }
        if (order.getStatus() == null) {
            order.setStatus("Pending");
        }
        orderMapper.createOrder(order);
        return order;
    }

    @Override
    public Order getOrder(String id) {
        return orderMapper.getOrderById(id);
    }

    @Override
    public List<Order> getUserOrders(String userId) {
        return orderMapper.getOrdersByUserId(userId);
    }

    @Override
    public void updateStatus(String id, String status) {
        System.out.println("Updating order " + id + " to status " + status);
        orderMapper.updateOrderStatus(id, status);
    }

    @Override
    public void cancelOrder(String id) {
        Order order = orderMapper.getOrderById(id);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        
        String status = order.getStatus();
        if ("Pending".equals(status)) {
            orderMapper.updateOrderStatus(id, "Cancelled");
        } else if ("Paid".equals(status)) {
            // Mock refund logic
            orderMapper.updateOrderStatus(id, "Refunded");
        } else {
            throw new RuntimeException("Cannot cancel order with status: " + status);
        }
    }
}
