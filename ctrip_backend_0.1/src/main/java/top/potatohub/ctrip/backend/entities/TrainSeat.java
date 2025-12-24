package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainSeat {
    private String id;
    private String trainId;
    private String type;
    private BigDecimal price;
    private Integer availableSeats;
    private Date createdAt;
    private Date updatedAt;
}
