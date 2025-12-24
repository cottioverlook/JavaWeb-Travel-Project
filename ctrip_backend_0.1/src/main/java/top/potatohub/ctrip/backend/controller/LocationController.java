package top.potatohub.ctrip.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.potatohub.ctrip.backend.common.Result;
import top.potatohub.ctrip.backend.entities.*;
import top.potatohub.ctrip.backend.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/locations")
    public Result<LocationsDTO> getLocations(@RequestParam(required = false) String keyword,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        LocationsDTO locations = new LocationsDTO();
        List<City> cities = locationService.getCities(keyword, page, size);
        List<CityDTO> cityDTOs = cities.stream().map(CityDTO::new).toList();
        locations.setCities(cityDTOs);

        List<Airport> airports = locationService.getAirports(keyword, page, size);
        List<AirportDTO> airportDTOs = airports.stream().map(AirportDTO::new).toList();
        locations.setAirports(airportDTOs);

        List<TrainStation> trainStations = locationService.getTrainStations(keyword, page, size);
        List<TrainStationDTO> trainStationDTOs = trainStations.stream().map(TrainStationDTO::new).toList();
        locations.setStations(trainStationDTOs);
        return Result.success(locations);
    }

    @GetMapping("/locations/cities")
    public Result<List<CityDTO>> getCities(@RequestParam String keyword,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        List<City> cities = locationService.getCities(keyword, page, size);
        List<CityDTO> cityDTOS = cities.stream().map(CityDTO::new).toList();
        return Result.success(cityDTOS);
    }

    @GetMapping("/locations/airports")
    public Result<List<AirportDTO>> getAirports(@RequestParam String keyword,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        List<Airport> airports = locationService.getAirports(keyword, page, size);
        List<AirportDTO> airportDTOs = airports.stream().map(AirportDTO::new).toList();
        return Result.success(airportDTOs);
    }

    @GetMapping("/locations/stations")
    public Result<List<TrainStationDTO>> getTrainStations(@RequestParam String keyword,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        List<TrainStation> trainStations = locationService.getTrainStations(keyword, page, size);
        List<TrainStationDTO> trainStationDTOS = trainStations.stream().map(TrainStationDTO::new).toList();
        return Result.success(trainStationDTOS);
    }
}
