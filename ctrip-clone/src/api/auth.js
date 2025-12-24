import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}

export function getProfile() {
  return request({
    url: '/api/users/self/profile',
    method: 'get'
  })
}

export function updateProfile(data) {
  return request({
    url: '/api/users/self/profile',
    method: 'put',
    data
  })
}

export function updatePassword(data) {
  return request({
    url: '/auth/changePassword',
    method: 'put',
    data
  })
}

export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}
