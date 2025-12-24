package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainDTO {
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

    public TrainDTO(Train train) {
        this.id = train.getId();
        this.trainNumber = train.getTrainNumber();
        this.trainType = train.getTrainType();
        this.departureStation = train.getDepartureStation();
        this.departureStationId = train.getDepartureStationId();
        this.departureTime = train.getDepartureTime();
        this.arrivalStation = train.getArrivalStation();
        this.arrivalStationId = train.getArrivalStationId();
        this.arrivalTime = train.getArrivalTime();
        this.status = train.getStatus();
    }
}
