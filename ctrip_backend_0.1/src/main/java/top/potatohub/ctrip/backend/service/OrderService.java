package top.potatohub.ctrip.backend.service;

import top.potatohub.ctrip.backend.entities.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order getOrder(String id, String userId);
    List<Order> getUserOrders(String userId);
    void updateStatus(String id, String status);
    boolean updateStatusForUser(String id, String userId, String status);
    void cancelOrder(String id, String userId);
}
