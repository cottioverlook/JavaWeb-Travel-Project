package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelRoomDTO {
    private String id;
    private String type;
    private Integer occupancy;
    private BigDecimal price;
    private Integer availableCount;
    private String amenities;
    private String policy;

    public HotelRoomDTO(HotelRoom room) {
        this.id = room.getId();
        this.type = room.getType();
        this.occupancy = room.getOccupancy();
        this.price = room.getPrice();
        this.availableCount = room.getAvailableCount();
        this.amenities = room.getAmenities();
        this.policy = room.getPolicy();
    }
}
