package top.potatohub.ctrip.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.potatohub.ctrip.backend.entities.Order;
import java.util.List;

@Mapper
public interface OrderMapper {
    void createOrder(Order order);
    Order getOrderById(String id);
    List<Order> getOrdersByUserId(String userId);
    void updateOrderStatus(@Param("id") String id, @Param("status") String status);
}
