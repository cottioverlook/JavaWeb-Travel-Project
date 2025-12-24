import request from '@/utils/request'

export function getRecommendations(limit) {
  return request({
    url: '/attractions/recommend',
    method: 'get',
    params: { limit }
  })
}

export function getAttractionDetail(id) {
  return request({
    url: `/attractions/${id}`,
    method: 'get'
  })
}

export function searchAttractions(params) {
  return request({
    url: '/attractions',
    method: 'get',
    params
  })
}
