package top.potatohub.ctrip.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.potatohub.ctrip.backend.entities.Hotel;
import top.potatohub.ctrip.backend.entities.HotelDTO;
import top.potatohub.ctrip.backend.entities.HotelRoom;
import top.potatohub.ctrip.backend.entities.HotelRoomDTO;
import top.potatohub.ctrip.backend.mapper.HotelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceV1 implements HotelService {

    @Autowired
    private HotelMapper hotelMapper;

    @Override
    public List<Hotel> searchHotels(String name_keyword,
                                       String address_keyword,
                                       String city_id,
                                       Integer rating,
                                       int page,
                                       int size) {
        int offset = (page - 1) * size;
        return hotelMapper.getHotels(name_keyword, address_keyword, city_id, rating, offset, size);
    }

    @Override
    public List<HotelRoom> getHotelRooms(String hotelId) {
        return hotelMapper.getRoomsByHotelId(hotelId);
    }

}
