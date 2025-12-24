package top.potatohub.ctrip.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.potatohub.ctrip.backend.common.Result;
import top.potatohub.ctrip.backend.entities.Review;
import top.potatohub.ctrip.backend.service.ReviewService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public Result<Review> submitReview(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        String orderId = (String) payload.get("orderId");
        Double score = Double.valueOf(payload.get("score").toString());
        String content = (String) payload.get("content");

        try {
            Review review = reviewService.submitReview(userId, orderId, score, content);
            return Result.success(review);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/user/self")
    public Result<List<Review>> getMyReviews(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        return Result.success(reviewService.getUserReviews(userId));
    }

    @GetMapping("/order/{orderId}")
    public Result<Review> getReviewByOrder(@PathVariable String orderId) {
        return Result.success(reviewService.getReviewByOrder(orderId));
    }
}
