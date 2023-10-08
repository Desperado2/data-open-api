import request from '@/utils/request'

export function listByUser(data) {
  return request({
    url: '/logging/list-by-user',
    method: 'post',
    data
  })
}

export function listALL(data) {
  return request({
    url: '/logging/list-all',
    method: 'post',
    data
  })
}

