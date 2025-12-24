package top.potatohub.ctrip.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.potatohub.ctrip.backend.common.Result;
import top.potatohub.ctrip.backend.entities.FlightCabinDTO;
import top.potatohub.ctrip.backend.entities.FlightDTO;
import top.potatohub.ctrip.backend.service.FlightService;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/flights")
    public Result<List<FlightDTO>> searchFlights(@RequestParam(required = false) String departure_id,
                                                 @RequestParam(required = false) String departure_type,
                                                 @RequestParam(required = false) String arrival_id,
                                                 @RequestParam(required = false) String arrival_type,
                                                 @RequestParam(name = "date", required = false) String date_string,
                                                 @RequestParam(required = false, defaultValue = "asc") String sort,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return Result.success(flightService.getFlights(departure_id, departure_type, arrival_id, arrival_type, date_string, sort, page, size));
    }

    @GetMapping("/flights/{flightId}/cabins")
    public Result<List<FlightCabinDTO>> getFlightCabins(@PathVariable String flightId) {
        return Result.success(flightService.getFlightCabins(flightId));
    }
}
