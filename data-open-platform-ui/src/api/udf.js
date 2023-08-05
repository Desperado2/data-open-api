import request from '@/utils/request'

export function typeList() {
  return request({
    url: '/udf-definition/type-list',
    method: 'get'
  })
}

export function udfInfo(id) {
  return request({
    url: `/udf-definition/${id}/id`,
    method: 'get'
  })
}

