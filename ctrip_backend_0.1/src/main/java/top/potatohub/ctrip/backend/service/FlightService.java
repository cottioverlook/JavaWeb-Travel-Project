package top.potatohub.ctrip.backend.service;

import top.potatohub.ctrip.backend.entities.FlightCabinDTO;
import top.potatohub.ctrip.backend.entities.FlightDTO;

import java.util.List;

public interface FlightService {

    List<FlightDTO> getFlights(String departure, String departure_type, String arrival, String arrival_type, String date, String sort, int page, int size);
    List<FlightCabinDTO> getFlightCabins(String flightId);

}
