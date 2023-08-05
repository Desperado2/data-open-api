import request from '@/utils/request'

export function subscribeList(data) {
  return request({
    url: '/api-subscribe/list-by-user',
    method: 'post',
    data
  })
}

export function subscribeAllList(data) {
  return request({
    url: '/api-subscribe/list-all',
    method: 'post',
    data
  })
}

export function subscribe(data) {
  return request({
    url: '/api-subscribe/subscribe',
    method: 'post',
    data
  })
}

export function approval(data) {
  return request({
    url: '/api-subscribe/approval',
    method: 'post',
    data
  })
}

export function disable(data) {
  return request({
    url: '/api-subscribe/disable',
    method: 'post',
    data
  })
}
