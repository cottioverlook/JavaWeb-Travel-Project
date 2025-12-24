package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationsDTO {
    private List<CityDTO> cities;
    private List<AirportDTO> airports;
    private List<TrainStationDTO> stations;
}
