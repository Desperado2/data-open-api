import request from '@/utils/request'

export function classifyList() {
  return request({
    url: '/classify/index-list',
    method: 'get'
  })
}

export function apiList() {
  return request({
    url: '/api/index-list',
    method: 'get'
  })
}

export function searchApi(searchWord) {
  return request({
    url: `/api/search/${searchWord}`,
    method: 'get'
  })
}

export function apiListByClassify(classifyId, page) {
  return request({
    url: `/api/index-list/${classifyId}/${page}`,
    method: 'get'
  })
}

export function apiDetail(id) {
  return request({
    url: `/api/index/${id}`,
    method: 'get'
  })
}

