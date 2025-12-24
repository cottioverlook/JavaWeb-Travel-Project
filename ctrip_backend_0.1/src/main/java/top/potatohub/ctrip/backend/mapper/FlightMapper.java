package top.potatohub.ctrip.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.potatohub.ctrip.backend.entities.Flight;
import top.potatohub.ctrip.backend.entities.FlightCabin;

import java.util.Date;
import java.util.List;

@Mapper
public interface FlightMapper {

    List<Flight> searchFlights(@Param("departure") String departure,
                               @Param("departure_type") String departure_type,
                               @Param("arrival") String arrival, 
                               @Param("arrival_type") String arrival_type,
                               @Param("date") Date date,
                               @Param("sort") String sort,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    List<FlightCabin> getCabinsByFlightId(@Param("flightId") String flightId);

    FlightCabin getCabinById(@Param("id") String id);

}
