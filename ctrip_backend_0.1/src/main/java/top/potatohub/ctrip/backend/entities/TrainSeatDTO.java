package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainSeatDTO {
    private String id;
    private String type;
    private BigDecimal price;
    private Integer availableSeats;

    public TrainSeatDTO(TrainSeat seat) {
        this.id = seat.getId();
        this.type = seat.getType();
        this.price = seat.getPrice();
        this.availableSeats = seat.getAvailableSeats();
    }
}
