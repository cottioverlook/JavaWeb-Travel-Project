package top.potatohub.ctrip.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.potatohub.ctrip.backend.common.Result;
import top.potatohub.ctrip.backend.entities.*;
import top.potatohub.ctrip.backend.service.TrainService;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TrainController {

    @Autowired
    private TrainService trainService;

    @GetMapping("/trains")
    public Result<List<TrainDTO>> searchTrains(@RequestParam(required = false) String departure_id,
                                               @RequestParam(required = false) String departure_type,
                                               @RequestParam(required = false) String arrival_id,
                                               @RequestParam(required = false) String arrival_type,
                                               @RequestParam(name = "date") String date_string,
                                               @RequestParam(required = false, defaultValue = "asc") String sort,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        List<Train> trains = trainService.searchTrains(departure_id, departure_type, arrival_id, arrival_type, date_string, sort, page, size);
        if(trains == null){
            return Result.error(405,"Date format invalid!");
        }
        List<TrainDTO> trainDTOS = trains.stream().map(TrainDTO::new).toList();
        return Result.success(trainDTOS);
    }

    @GetMapping("/trains/{trainId}/seats")
    public Result<List<TrainSeatDTO>> getTrainSeats(@PathVariable String trainId) {
        List<TrainSeat> trainSeats = trainService.getTrainSeats(trainId);
        List<TrainSeatDTO> trainSeatDTOS = trainSeats.stream().map(TrainSeatDTO::new).toList();
        return Result.success(trainSeatDTOS);
    }

}
