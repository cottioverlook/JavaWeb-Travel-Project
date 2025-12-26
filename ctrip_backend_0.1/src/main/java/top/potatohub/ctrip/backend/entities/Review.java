package top.potatohub.ctrip.backend.entities;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Review {
    private String id;
    private String userId;
    private String orderId;
    private String productId; // 可以是酒店ID、景点ID等
    private Double score;
    private String content;
    private LocalDateTime createdAt;
    
    // Transient field for display
    private String userName;
}
