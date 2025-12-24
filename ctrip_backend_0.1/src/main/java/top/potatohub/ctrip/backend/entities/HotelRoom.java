package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelRoom {
    private String id;
    private String hotelId;
    private String type;
    private Integer occupancy;
    private BigDecimal price;
    private Integer availableCount;
    private String amenities;
    private String policy;
    private Date createdAt;
    private Date updatedAt;
}
