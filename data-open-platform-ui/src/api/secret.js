import request from '@/utils/request'

export function listByUser(data) {
  return request({
    url: '/authentication/list-by-user',
    method: 'post',
    data
  })
}

export function selectSecretByKey(key) {
  return request({
    url: `/authentication/secret/${key}`,
    method: 'get'
  })
}

export function selectSecretByUserId(userId) {
  return request({
    url: `/authentication/list-by-user/${userId}`,
    method: 'get'
  })
}

export function reset() {
  return request({
    url: `/authentication/reset`,
    method: 'put'
  })
}

export function resetByUserId(userId) {
  return request({
    url: `/authentication/reset/${userId}`,
    method: 'put'
  })
}

export function list(data) {
  return request({
    url: '/authentication/list-all',
    method: 'post',
    data
  })
}
