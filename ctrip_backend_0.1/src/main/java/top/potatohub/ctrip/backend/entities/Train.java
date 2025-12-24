package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Train {
    private String id;
    private String trainNumber;
    private String trainType;
    private String departureStation;
    private String departureStationId;
    private Date departureTime;
    private String arrivalStation;
    private String arrivalStationId;
    private Date arrivalTime;
    private String status;
    private Date createdAt;
    private Date updatedAt;
}
