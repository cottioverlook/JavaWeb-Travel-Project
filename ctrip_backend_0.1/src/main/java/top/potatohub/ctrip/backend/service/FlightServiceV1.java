package top.potatohub.ctrip.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.potatohub.ctrip.backend.common.Result;
import top.potatohub.ctrip.backend.entities.Flight;
import top.potatohub.ctrip.backend.entities.FlightCabin;
import top.potatohub.ctrip.backend.entities.FlightCabinDTO;
import top.potatohub.ctrip.backend.entities.FlightDTO;
import top.potatohub.ctrip.backend.mapper.FlightMapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceV1 implements FlightService {

    @Autowired
    FlightMapper flightMapper;

    @Override
    public List<FlightDTO> getFlights(String departure,
                                      String departure_type,
                                      String arrival,
                                      String arrival_type,
                                      String date_string,
                                      String sort,
                                      int page, int size) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if (date_string != null && !date_string.isEmpty()) {
            try {
                date = dateFormat.parse(date_string);
            } catch (ParseException e) {
                // 如果日期解析失败，可以忽略日期条件或返回null，这里选择忽略
                date = null;
            }
        }

        int offset = (page - 1) * size;
        String validSort = "asc".equalsIgnoreCase(sort) ? "ASC" : "DESC";
        List<Flight> flights = flightMapper.searchFlights(departure, departure_type, arrival, arrival_type, date, validSort, offset, size);
        return flights.stream().map(FlightDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<FlightCabinDTO> getFlightCabins(String flightId) {
        List<FlightCabin> cabins = flightMapper.getCabinsByFlightId(flightId);
        return cabins.stream().map(FlightCabinDTO::new).collect(Collectors.toList());
    }

}
