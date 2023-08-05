import request from '@/utils/request'

export function list(data) {
  return request({
    url: '/data-source/list-page',
    method: 'post',
    data
  })
}

export function listALL() {
  return request({
    url: '/data-source/list-all',
    method: 'get'
  })
}

export function supportList() {
  return request({
    url: '/data-source/support-list',
    method: 'get'
  })
}

export function addDatasource(data) {
  return request({
    url: '/data-source/add',
    method: 'post',
    data
  })
}

export function updateDatasource(data) {
  return request({
    url: '/data-source/update',
    method: 'post',
    data
  })
}

export function testConnection(data) {
  return request({
    url: `/data-source/test-connect`,
    method: 'post',
    data
  })
}

export function testConnectionById(id) {
  return request({
    url: `/data-source/test-connect-by-id/${id}`,
    method: 'get'
  })
}

export function getDatasourceById(id) {
  return request({
    url: `/data-source/get/${id}`,
    method: 'get'
  })
}

export function updateStatus(id, status) {
  return request({
    url: `/data-source/change-status/${id}/${status}`,
    method: 'put'
  })
}
