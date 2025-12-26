package top.potatohub.ctrip.backend.service;

import top.potatohub.ctrip.backend.entities.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order getOrder(String id);
    List<Order> getUserOrders(String userId);
    void updateStatus(String id, String status);
    void cancelOrder(String id);
}
