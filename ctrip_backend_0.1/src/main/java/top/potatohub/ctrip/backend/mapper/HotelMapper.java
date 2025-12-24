package top.potatohub.ctrip.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.potatohub.ctrip.backend.entities.Hotel;
import top.potatohub.ctrip.backend.entities.HotelRoom;

import java.util.List;

@Mapper
public interface HotelMapper {

    List<Hotel> getHotels(@Param("name_keyword") String name_keyword,
                          @Param("address_keyword") String address_keyword,
                          @Param("city_id") String city_id,
                          @Param("rating") Integer rating,
                          @Param("offset") int offset,
                          @Param("limit") int limit);

    List<HotelRoom> getRoomsByHotelId(@Param("hotelId") String hotelId);

    HotelRoom getRoomById(@Param("id") String id);

}
