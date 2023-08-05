import request from '@/utils/request'

export function baseInfo() {
  return request({
    url: '/api-statistic/base',
    method: 'get'
  })
}

export function requireInfo() {
  return request({
    url: '/api-statistic/require',
    method: 'get'
  })
}
