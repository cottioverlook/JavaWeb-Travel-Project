package top.potatohub.ctrip.backend.service;

import top.potatohub.ctrip.backend.entities.Hotel;
import top.potatohub.ctrip.backend.entities.HotelRoom;

import java.util.List;

public interface HotelService {
    List<Hotel> searchHotels(String name_keyword, String address_keyword, String city, Integer rating, int page, int size);
    List<HotelRoom> getHotelRooms(String hotelId);
}
