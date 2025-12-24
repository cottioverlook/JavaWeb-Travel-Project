package top.potatohub.ctrip.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.potatohub.ctrip.backend.entities.Review;
import java.util.List;

@Mapper
public interface ReviewMapper {
    int insert(Review review);
    List<Review> getByUserId(String userId);
    List<Review> getByProductId(String productId);
    Review getByOrderId(String orderId);
}
