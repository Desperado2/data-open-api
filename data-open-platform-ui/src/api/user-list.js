import request from '@/utils/request'

export function userList(data) {
  return request({
    url: '/user-manage/list',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/user-manage/update',
    method: 'post',
    data
  })
}

export function addUser(data) {
  return request({
    url: '/user-manage/add',
    method: 'post',
    data
  })
}

export function activeUser(userId) {
  return request({
    url: `/user/active/${userId}`,
    method: 'get'
  })
}