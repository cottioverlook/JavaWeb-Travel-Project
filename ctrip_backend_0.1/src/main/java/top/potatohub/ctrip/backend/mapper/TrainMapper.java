package top.potatohub.ctrip.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.potatohub.ctrip.backend.entities.Train;
import top.potatohub.ctrip.backend.entities.TrainSeat;

import java.util.Date;
import java.util.List;

@Mapper
public interface TrainMapper {

    List<Train> searchTrains(@Param("departure_id") String departure_id,
                             @Param("departure_type") String departure_type,
                             @Param("arrival_id") String arrival_id,
                             @Param("arrival_type") String arrival_type,
                             @Param("date") Date date,
                             @Param("sort") String sort,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    List<TrainSeat> getSeatsByTrainId(@Param("trainId") String trainId);

    TrainSeat getSeatById(@Param("id") String id);

}
