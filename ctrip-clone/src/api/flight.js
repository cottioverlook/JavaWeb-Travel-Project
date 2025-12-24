import request from '@/utils/request'

export function getFlights(params) {
  return request({
    url: '/api/flights',
    method: 'get',
    params
  })
}

export function getFlightCabins(flightId) {
  return request({
    url: `/api/flights/${flightId}/cabins`,
    method: 'get'
  })
}
