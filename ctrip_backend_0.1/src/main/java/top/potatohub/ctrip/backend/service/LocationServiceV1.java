package top.potatohub.ctrip.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.potatohub.ctrip.backend.entities.*;
import top.potatohub.ctrip.backend.mapper.LocationMapper;

import java.util.List;

@Service
public class LocationServiceV1 implements LocationService {

    @Autowired
    private LocationMapper locationMapper;

    @Override
    public List<Airport> getAirports(String keyword, int page, int size) {
        int offset = (page - 1) * size;
        return locationMapper.getAirports(keyword, offset, size);
    }

    @Override
    public List<City> getCities(String keyword, int page, int size) {
        int offset = (page - 1) * size;
        return locationMapper.getCities(keyword, offset, size);
    }

    @Override
    public List<TrainStation> getTrainStations(String keyword, int page, int size) {
        int offset = (page - 1) * size;
        return locationMapper.getStations(keyword, offset, size);
    }
}
