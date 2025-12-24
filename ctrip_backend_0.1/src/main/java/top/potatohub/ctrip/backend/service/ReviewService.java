package top.potatohub.ctrip.backend.service;

import top.potatohub.ctrip.backend.entities.Review;
import java.util.List;

public interface ReviewService {
    Review submitReview(String userId, String orderId, Double score, String content);
    List<Review> getUserReviews(String userId);
    List<Review> getProductReviews(String productId);
    Review getReviewByOrder(String orderId);
}
