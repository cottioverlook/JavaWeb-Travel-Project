import request from '@/utils/request'

export function getHotels(params) {
  return request({
    url: '/api/hotels',
    method: 'get',
    params
  })
}

export function getHotelRooms(hotelId) {
  return request({
    url: `/api/hotels/${hotelId}/rooms`,
    method: 'get'
  })
}

export function getHotelDetail(hotelId) {
  return request({
    url: `/api/hotels/${hotelId}`,
    method: 'get'
  })
}
