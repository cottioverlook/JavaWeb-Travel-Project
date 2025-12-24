package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {
    private String id;
    private String name;
    private String country;

    public CityDTO(City city) {
        this.id = city.getId();
        this.name = city.getName();
        this.country = city.getCountry();
    }
}
