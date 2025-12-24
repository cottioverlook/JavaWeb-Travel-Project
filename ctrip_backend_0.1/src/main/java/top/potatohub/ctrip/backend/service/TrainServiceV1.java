package top.potatohub.ctrip.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.potatohub.ctrip.backend.entities.Train;
import top.potatohub.ctrip.backend.entities.TrainSeat;
import top.potatohub.ctrip.backend.entities.TrainSeatDTO;
import top.potatohub.ctrip.backend.entities.*;
import top.potatohub.ctrip.backend.mapper.FlightMapper;
import top.potatohub.ctrip.backend.mapper.TrainMapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainServiceV1 implements TrainService {

    @Autowired
    private TrainMapper trainMapper;

    @Override
    public List<Train> searchTrains(String departure_id,
                                    String departure_type,
                                    String arrival_id,
                                    String arrival_type,
                                    String date_string,
                                    String sort,
                                    int page, int size) {
        int offset = (page - 1) * size;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if (date_string != null && !date_string.isEmpty()) {
            try {
                date = dateFormat.parse(date_string);
            } catch (ParseException e) {
                // Ignore invalid date format, search without date filter
                date = null;
            }
        }
        String validSort = "asc".equalsIgnoreCase(sort) ? "ASC" : "DESC";
        return trainMapper.searchTrains(departure_id, departure_type, arrival_id, arrival_type, date, validSort, offset, size);
    }

    @Override
    public List<TrainSeat> getTrainSeats(String trainId) {
        return trainMapper.getSeatsByTrainId(trainId);
    }
}
