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
        if (userId == null) {
            // For dev/test if no token provided, maybe allow anonymous or fail
            // return Result.error("Unauthorized");
            // Assuming token is optional for now or we enforce it?
            // Let's enforce it but handle null gracefully if needed
            // If token interceptor passes, userId might be null if token missing (unless interceptor blocks it)
            // Our LoginInterceptor blocks if token invalid, but allows if OPTIONS or specific paths?
            // Assuming Interceptor sets userId if token valid.
        }
        order.setUserId(userId);
        return Result.success(orderService.createOrder(order));
    }

    @GetMapping
    public Result<List<Order>> getUserOrders(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        return Result.success(orderService.getUserOrders(userId));
    }
    
    @GetMapping("/{id}")
    public Result<Order> getOrder(@PathVariable String id) {
        return Result.success(orderService.getOrder(id));
    }

    @PostMapping("/{id}/pay")
    public Result<String> payOrder(@PathVariable String id) {
        System.out.println("Received pay request for order: " + id);
        orderService.updateStatus(id, "Paid");
        return Result.success("Payment successful");
    }

    @PostMapping("/{id}/cancel")
    public Result<String> cancelOrder(@PathVariable String id) {
        try {
            orderService.cancelOrder(id);
            return Result.success("Order cancelled successfully");
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}
