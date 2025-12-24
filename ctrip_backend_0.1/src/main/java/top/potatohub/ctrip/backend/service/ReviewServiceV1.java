package top.potatohub.ctrip.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.potatohub.ctrip.backend.entities.Order;
import top.potatohub.ctrip.backend.entities.Review;
import top.potatohub.ctrip.backend.mapper.OrderMapper;
import top.potatohub.ctrip.backend.mapper.ReviewMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewServiceV1 implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Review submitReview(String userId, String orderId, Double score, String content) {
        // 1. Check if review already exists
        Review existingReview = reviewMapper.getByOrderId(orderId);
        if (existingReview != null) {
            throw new RuntimeException("Review already exists for this order");
        }

        // 2. Get order to find productId
        Order order = orderMapper.getOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to review this order");
        }

        // 3. Create review
        Review review = new Review();
        review.setId(UUID.randomUUID().toString());
        review.setUserId(userId);
        review.setOrderId(orderId);
        review.setProductId(order.getProductId());
        review.setScore(score);
        review.setContent(content);
        review.setCreatedAt(LocalDateTime.now());

        reviewMapper.insert(review);
        return review;
    }

    @Override
    public List<Review> getUserReviews(String userId) {
        return reviewMapper.getByUserId(userId);
    }

    @Override
    public List<Review> getProductReviews(String productId) {
        return reviewMapper.getByProductId(productId);
    }

    @Override
    public Review getReviewByOrder(String orderId) {
        return reviewMapper.getByOrderId(orderId);
    }
}
