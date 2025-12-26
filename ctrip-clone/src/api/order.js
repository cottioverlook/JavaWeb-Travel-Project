import request from '@/utils/request'

export function createOrder(data) {
  return request({
    url: '/api/orders',
    method: 'post',
    data
  })
}

export function getUserOrders() {
  return request({
    url: '/api/orders',
    method: 'get'
  })
}

export function getOrder(id) {
  return request({
    url: `/api/orders/${id}`,
    method: 'get'
  })
}

export function payOrder(id) {
  return request({
    url: `/api/orders/${id}/pay`,
    method: 'post'
  })
}

export function cancelOrder(id) {
  return request({
    url: `/api/orders/${id}/cancel`,
    method: 'post'
  })
}
