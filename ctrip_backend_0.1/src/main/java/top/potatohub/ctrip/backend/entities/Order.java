package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String id;
    private String userId;
    private String productId;
    private String productName;
    private BigDecimal amount;
    private String status; // Pending, Paid, Cancelled
    private Date createdAt;
    private Date updatedAt;
}
