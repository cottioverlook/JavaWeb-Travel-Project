package top.potatohub.ctrip.backend.service;

import top.potatohub.ctrip.backend.entities.*;

import java.util.List;

public interface TrainService {

    List<Train> searchTrains(String departure_id, String departure_type, String arrival_id, String arrival_type, String date, String sort, int page, int size);
    List<TrainSeat> getTrainSeats(String trainId);

}
