import request from '@/utils/request'

export function searchTrains(params) {
  return request({
    url: '/api/trains',
    method: 'get',
    params
  })
}

export function getTrainSeats(trainId) {
  return request({
    url: `/api/trains/${trainId}/seats`,
    method: 'get'
  })
}
