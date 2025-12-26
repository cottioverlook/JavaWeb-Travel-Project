package top.potatohub.ctrip.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.potatohub.ctrip.backend.common.Result;
import top.potatohub.ctrip.backend.entities.Hotel;
import top.potatohub.ctrip.backend.entities.HotelDTO;
import top.potatohub.ctrip.backend.entities.HotelRoom;
import top.potatohub.ctrip.backend.entities.HotelRoomDTO;
import top.potatohub.ctrip.backend.service.HotelService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/hotels")
    public Result<List<HotelDTO>> searchHotels(@RequestParam(required = false) String name_keyword,
                                               @RequestParam(required = false) String address_keyword,
                                               @RequestParam(required = false) String city_id,
                                               @RequestParam(required = false) Integer rating,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        List<Hotel> hotels = hotelService.searchHotels(name_keyword, address_keyword, city_id, rating, page, size);
        List<HotelDTO> hotelDTOS = hotels.stream().map(HotelDTO::new).collect(Collectors.toList());
        return Result.success(hotelDTOS);
    }

    @GetMapping("/hotels/{hotelId}/rooms")
    public Result<List<HotelRoomDTO>> getHotelRooms(@PathVariable String hotelId) {
        List<HotelRoom> hotelRooms = hotelService.getHotelRooms(hotelId);
        List<HotelRoomDTO> hotelRoomDTOS = hotelRooms.stream().map(HotelRoomDTO::new).collect(Collectors.toList());
        return Result.success(hotelRoomDTOS);
    }

    @GetMapping("/hotels/{hotelId}")
    public Result<HotelDTO> getHotelDetail(@PathVariable String hotelId) {
        // Reuse search for simplicity or add getById in service
        List<Hotel> hotels = hotelService.searchHotels(null, null, null, null, 1, 100);
        Hotel hotel = hotels.stream().filter(h -> h.getId().equals(hotelId)).findFirst().orElse(null);
        
        if (hotel != null) {
            return Result.success(new HotelDTO(hotel));
        }
        return Result.error("Hotel not found");
    }
}
