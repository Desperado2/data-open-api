import Vue from 'vue'

/***/
const statusTagInfo = (status) => {
  if (status === 0) {
    return { 'tagColor': '#909399', 'css': 'info', 'title': 'Editor' }
  }
  if (status === 1) {
    return { 'tagColor': '#67c23a', 'css': 'success', 'title': 'Published' }
  }
  if (status === 2) {
    return { 'tagColor': '#e6a23c', 'css': 'warning', 'title': 'Changes' }
  }
  if (status === 3) {
    return { 'tagColor': '#f56c6c', 'css': 'danger', 'title': 'Disable' }
  }
  return { 'tagColor': '', 'css': '', 'title': '' }
}

/***/
const methodTagInfo = (httpMethod) => {
  if (httpMethod === 'GET') {
    return { 'css': '', 'title': 'GET' }
  }
  if (httpMethod === 'POST') {
    return { 'css': 'success', 'title': 'POST' }
  }
  if (httpMethod === 'PUT') {
    return { 'css': 'warning', 'title': 'PUT' }
  }
  if (httpMethod === 'DELETE') {
    return { 'css': 'danger', 'title': 'DELETE' }
  }
  return { 'css': '', 'title': '' }
}

const errorBox = (content) => {
  Vue.prototype.$alert(content, 'Error', { confirmButtonText: 'OK' })
}

const fixGetRequestBody = (httpMethod, requestBody) => {
  const doRunParam = JSON.parse(requestBody)
  if (httpMethod !== 'GET') {
    return doRunParam
  }
  //
  const newRunParam = {}
  for (const key in doRunParam) {
    if (doRunParam[key] !== null) {
      newRunParam[key] = doRunParam[key].toString()
    }
  }
  return newRunParam
}

const checkRequestBody = (httpMethod, codeType, requestBody, testDatasourceCode, preDatasourceCode, prodDatasourceCode, apiRunEnvironment, responseFormat) => {
  if (apiRunEnvironment === undefined || apiRunEnvironment === null || apiRunEnvironment === 'null' || apiRunEnvironment.trim().length === 0) {
    errorBox('未选择运行环境。')
    return false
  }
  if (testDatasourceCode === undefined || testDatasourceCode === null || testDatasourceCode === 'null' || testDatasourceCode.trim().length === 0) {
    errorBox('未选择测试数据源。')
    return false
  }
  if (preDatasourceCode === undefined || preDatasourceCode === null || preDatasourceCode === 'null' || preDatasourceCode.trim().length === 0) {
    errorBox('未选择预发数据源。')
    return false
  }
  if (prodDatasourceCode === undefined || prodDatasourceCode === null || prodDatasourceCode === 'null' || prodDatasourceCode.trim().length === 0) {
    errorBox('未选择正式数据源。')
    return false
  }
  let doRunParam = {}
  try {
    doRunParam = JSON.parse(requestBody)
  } catch (e) {
    errorBox('请求参数格式错误 : ' + e)
    return false
  }
  try {
    JSON.parse(responseFormat)
  } catch (e) {
    errorBox('自定义响应体格式错误 : ' + e)
    return false
  }
  if (httpMethod === 'GET') {
    if (Object.prototype.toString.call(doRunParam) !== '[object Object]') {
      errorBox('GET 请求参数必须为MAP结构。')
      return false
    }
    for (const key in doRunParam) {
      const typeStr = Object.prototype.toString.call(doRunParam[key])
      if (typeStr === '[object Object]' || typeStr === '[object Array]') {
        errorBox('在GET中不能有复杂的结构参数。')
        return false
      }
    }
  }
  return true
}

const headerData = (oriData) => {
  const requestHeaderData = {}
  for (let i = 0; i < oriData.length; i++) {
    if (oriData[i].checked && oriData[i].name !== '') {
      requestHeaderData[oriData[i].name] = encodeURIComponent(oriData[i].value)
    }
  }
  return requestHeaderData
}

const formatDate = (date, fmt = 'yyyyMMdd-hhmmss.S') => {
  if (typeof (date) === 'number') {
    date = new Date(date)
  }
  const o = {
    'M+': date.getMonth() + 1, // 月份
    'd+': date.getDate(), // 日
    'h+': date.getHours(), // 小时
    'm+': date.getMinutes(), // 分
    's+': date.getSeconds(), // 秒
    'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
    'S': date.getMilliseconds() // 毫秒
  }
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  for (const k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
    }
  }
  return fmt
}

export {
  methodTagInfo, statusTagInfo, errorBox, checkRequestBody, fixGetRequestBody, headerData, formatDate
}
