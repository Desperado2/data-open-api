import axios from 'axios'
import {Message, MessageBox} from 'element-ui'
import store from '@/store'
import {getToken} from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 60000 // request timeout
})
const whiteUrlList = ['/api-script/execute', '/api-script/smoke-test']
// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['token'] = getToken()
    }
    if (config.params) {
      isTrim(config.params)
    }
    if (config.data) {
      isTrim(config.data)
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data
    const path = response.request.responseURL.split(process.env.VUE_APP_BASE_API)[1].split('?')[0]
    // if the custom code is not 20000, it is judged as an error.
    if (res.statusCode !== 200 && response.request.responseType !== 'arraybuffer' && !whiteUrlList.includes(path)) {
      Message({
        message: res.msg || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.statusCode === 50008 || res.statusCode === 50012 || res.statusCode === 50014) {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.msg || 'Network Error',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

/**
 * @description: 2021-05-14 请求参数首尾去空格
 * @param {*} data
 * @return {*}
 */
function isTrim(data) {
  // 首先需要判断当前的config中是否存在data值
  if (data && data instanceof Object) {
    for (const key in data) {
      if (Object.hasOwnProperty.call(data, key)) {
        // 此处我们不要使用   let element = data[key] 注意  如果采用这种方式的话对应trim改变的值和data[key]将不再会是一个同一个内存地址
        // 在需要判断一下当前数据是否是数组
        if (Array.isArray(data[key])) {
          // 就将数组放进去
          data[key] = isTrim(data[key])
        } else if (data[key] && data[key] instanceof Object) {
          // 如果对象里面套对象的话
          data[key] = isTrim(data[key])
        } else if (data[key] && Object.prototype.toString.call(data[key]) === '[object String]') {
          // 如果对象里面的数据是String的话那么就直接trim只对String进行操作
          data[key] = data[key].trim()
        }
      }
    }
    return data
  } else if (data && Object.prototype.toString.call(data) === '[object String]') {
    // 如果是字符串说明是JSON.parse需要转换
    let dataObj = JSON.parse(data)
    // 转成对象之后在抛出去
    dataObj = isTrim(dataObj)
    return JSON.stringify(dataObj)
  } else if (data && data instanceof Array) {
    // 如果是数组  那就forin一下  判断里面的数据类型
    for (const key in data) {
      if (Object.hasOwnProperty.call(data, key)) {
        if ((data && data instanceof Object) || (data && data instanceof Array)) {
          data[key] = isTrim(data[key])
        }
      }
    }
    return data
  }
}

export default service
