import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'get'
  })
}

export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export function verificationCodeToken() {
  return request({
    url: '/user/verification-code-token',
    method: 'get'
  })
}

export function verificationCode(verificationCode) {
  return request({
    url: `/user/verification-code/${verificationCode}`,
    method: 'get',
    responseType: 'arraybuffer'
  })
}

export function getInfo(token) {
  return request({
    url: '/user/user-info',
    method: 'get',
    params: { token }
  })
}
