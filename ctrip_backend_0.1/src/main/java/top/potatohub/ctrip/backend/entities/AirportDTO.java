package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportDTO {
    private String code;
    private String name;
    
    public AirportDTO(Airport airport) {
        this.code = airport.getCode();
        this.name = airport.getName();
    }
}
