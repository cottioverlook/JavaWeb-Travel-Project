package top.potatohub.ctrip.backend.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.potatohub.ctrip.backend.common.Result;
import top.potatohub.ctrip.backend.entities.Order;
import top.potatohub.ctrip.backend.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Result<Order> createOrder(@RequestBody Order order, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        order.setUserId(userId);
        return Result.success(orderService.createOrder(order));
    }

    @GetMapping
    public Result<List<Order>> getUserOrders(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        return Result.success(orderService.getUserOrders(userId));
    }
    
    @GetMapping("/{id}")
    public Result<Order> getOrder(@PathVariable String id, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        Order order = orderService.getOrder(id, userId);
        if (order == null) {
            return Result.error(404, "Order not found");
        }
        return Result.success(order);
    }

    @PostMapping("/{id}/pay")
    public Result<String> payOrder(@PathVariable String id, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (!orderService.updateStatusForUser(id, userId, "Paid")) {
            return Result.error(404, "Order not found");
        }
        return Result.success("Payment successful");
    }

    @PostMapping("/{id}/cancel")
    public Result<String> cancelOrder(@PathVariable String id, HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            orderService.cancelOrder(id, userId);
            return Result.success("Order cancelled successfully");
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}
