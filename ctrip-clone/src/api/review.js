import request from '@/utils/request'

export function submitReview(data) {
  return request({
    url: '/api/reviews',
    method: 'post',
    data
  })
}

export function getMyReviews() {
  return request({
    url: '/api/reviews/user/self',
    method: 'get'
  })
}

export function getReviewByOrder(orderId) {
  return request({
    url: `/api/reviews/order/${orderId}`,
    method: 'get'
  })
}

export function getProductReviews(productId) {
  return request({
    url: `/api/reviews/product/${productId}`,
    method: 'get'
  })
}
