package top.potatohub.ctrip.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.potatohub.ctrip.backend.entities.Airport;
import top.potatohub.ctrip.backend.entities.City;
import top.potatohub.ctrip.backend.entities.TrainStation;

import java.util.List;

@Mapper
public interface LocationMapper {

    List<City> getCities(@Param("keyword") String keyword,
                         @Param("offset") int offset,
                         @Param("limit") int limit);

    List<Airport> getAirports(@Param("keyword") String keyword,
                              @Param("offset") int offset,
                              @Param("limit") int limit);

    List<TrainStation> getStations(@Param("keyword") String keyword,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);

}
