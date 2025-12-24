package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {
    private String id;
    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;
    private Integer rating;
    private String amenities;
    private String city;

    public HotelDTO(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.address = hotel.getAddress();
        this.latitude = hotel.getLatitude();
        this.longitude = hotel.getLongitude();
        this.description = hotel.getDescription();
        this.rating = hotel.getRating();
        this.amenities = hotel.getAmenities();
        this.city = hotel.getCity();
    }
}
