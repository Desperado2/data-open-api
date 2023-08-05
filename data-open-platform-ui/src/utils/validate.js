/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  // 邮箱的邮箱地址
  //  验证邮箱的正则表达式
  const regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/
  return regEmail.test(str)
}


/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAllowEmailSuffix(str, allowList) {
  for (const element of allowList) {
    if(str.trim().endsWith(element.trim())){
      return true
    }
  }
  return false
}

export function validatorApiPath(str) {
  return /^\/[0-9A-Za-z/\-_]{1,50}$/.test(str)
}


export function validatorPassword(str) {
  var pwdRegex = new RegExp('(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,30}');
  if(!pwdRegex.test(str.trim())){
    return {
      "statu": false,
      "msg": "密码中必须包含大小写 字母、数字、特称字符，至少8个字符，最多30个字符"
    }
  }
   //不能连续字符（如123、abc）连续3位或3位以上
  if(!validContinuousStr(str.trim())){
    return {
      "statu": false,
      "msg": "您的密码为弱口令密码，密码中不能相同字符出现连续3位或3位以上"
    }
  }
  //不能相同字符（如111、aaa）连续3位或3位以上
  var re = /(\w)*(\w)\2{2}(\w)*/g;
  if(re.test(str.trim())){
      return {
        "statu": false,
        "msg": "您的密码为弱口令密码，密码中不能连续字符出现连续3位或3位以上"
      };
  }
  return {
    "statu": true,
    "msg": ""
  }
}

function validContinuousStr(str){
    var arr = str.split('');
    var flag = true;
    for (var i = 1; i < arr.length-1; i++) {
        var firstIndex = arr[i-1].charCodeAt();
        var secondIndex = arr[i].charCodeAt();
        var thirdIndex = arr[i+1].charCodeAt();
        if((thirdIndex - secondIndex == 1)&&(secondIndex - firstIndex==1)){
            flag =  false;
        }
    }
    return flag;
}
