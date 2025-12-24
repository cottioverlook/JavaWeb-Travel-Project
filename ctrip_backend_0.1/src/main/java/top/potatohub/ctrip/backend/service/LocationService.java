package top.potatohub.ctrip.backend.service;

import top.potatohub.ctrip.backend.entities.*;

import java.util.List;

public interface LocationService {

    List<Airport> getAirports(String keyword, int page, int size);
    List<City> getCities(String keyword, int page, int size);
    List<TrainStation> getTrainStations(String keyword, int page, int size);

}
