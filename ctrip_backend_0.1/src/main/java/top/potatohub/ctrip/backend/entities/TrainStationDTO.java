package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainStationDTO {
    private String id;
    private String name;

    public TrainStationDTO(TrainStation station) {
        this.id = station.getId();
        this.name = station.getName();
    }
}
