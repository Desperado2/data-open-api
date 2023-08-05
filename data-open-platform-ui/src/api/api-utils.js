import request from '@/utils/request'

export function sign(data) {
  return request({
    url: '/sign-utils/sign-by-user',
    method: 'post',
    data
  })
}
