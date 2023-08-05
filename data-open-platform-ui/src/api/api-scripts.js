import request from '@/utils/request'

export function addApiScript(data) {
  return request({
    url: '/api-script/add',
    method: 'post',
    data
  })
}

export function updateApiScript(data) {
  return request({
    url: `/api-script/update`,
    method: 'post',
    data
  })
}

export function getApiScriptByApiId(apiId) {
  return request({
    url: `/api-script/get-by-apiId/${apiId}`,
    method: 'get'
  })
}

export function updateApiScriptStatus(id, status) {
  return request({
    url: `/api-script/${id}/${status}`,
    method: 'put'
  })
}

export function executeApiScript(data) {
  return request({
    url: '/api-script/execute',
    method: 'post',
    data
  })
}

export function apiScriptSmokeTest(id) {
  return request({
    url: `/api-script/smoke-test`,
    method: 'get',
    params: {
      id: id
    }
  })
}

export function publishApiScript(id) {
  return request({
    url: `/api-script/publish/${id}`,
    method: 'put'
  })
}

export function publishHistoryList(apiId) {
  return request({
    url: `/api-script/publish-history-list/${apiId}`,
    method: 'get'
  })
}

export function publishHistory(publishId) {
  return request({
    url: `/api-script/publish-history/${publishId}`,
    method: 'get'
  })
}
