import request from '@/utils/request'

export function classifyList(data) {
  return request({
    url: '/classify/page-list',
    method: 'post',
    data
  })
}

export function deleteClassify(id) {
  return request({
    url: `/classify/delete/${id}`,
    method: 'delete'
  })
}

export function addClassify(data) {
  return request({
    url: '/classify/add',
    method: 'post',
    data
  })
}

export function updateClassify(data) {
  return request({
    url: '/classify/update',
    method: 'post',
    data
  })
}

export function apiList(data) {
  return request({
    url: '/api/page-list',
    method: 'post',
    data
  })
}

export function addApi(data) {
  return request({
    url: '/api/add',
    method: 'post',
    data
  })
}

export function updateApi(data) {
  return request({
    url: '/api/update',
    method: 'post',
    data
  })
}

export function getApiById(apiId) {
  return request({
    url: `/api/get/${apiId}`,
    method: 'get'
  })
}

export function updateApiStatus(apiId, status) {
  return request({
    url: `/api/update-status/${apiId}/${status}`,
    method: 'put'
  })
}

export function updateApplyStatus(apiId, applyStatus, notOpenApplyReason) {
  return request({
    url: `/api/update-apply-status/${apiId}`,
    method: 'put',
    params: {
      applyStatus: applyStatus,
      notOpenApplyReason: notOpenApplyReason
    }
  })
}
