package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
    private String id;
    private String name;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
