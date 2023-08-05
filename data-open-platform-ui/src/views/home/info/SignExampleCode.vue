<template>
  <div v-highlight class="highlight">
    <pre style="margin-top:0">
    <code v-show="activeLanguage==='first'">
# -*- coding: utf-8 -*-
import hashlib

def sign(data: dict, app_secret: str) -> str:
    """
    签名
    :param data: 待签名数据
    :param app_secret: 签名秘钥
    :return: 签名
    """
    # 字典序排序
    sort_data = sorted(data.items(), key=lambda x: x[0])
    # 拼接字符串
    param = "&".join([(str(data[0]) + "=" + str(data[1])) for data in sort_data])
    # md5加密
    md5 = hashlib.md5()
    md5.update(str(param + app_secret).encode('utf-8'))
    signResult = str(md5.hexdigest()).lower()
    # 输出结果
    print(signResult)
    return signResult
    </code>

    <code v-show="activeLanguage==='second'">
//java环境中文传值时，需特别注意字符编码问题

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 签名工具
 * @author tu nan
 * @date 2023/3/17
 **/
public class SignUtils {

    private static final char[] DIGITS_DATA = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /**
     * 签名
     * @param data 待签名数据
     * @param secret appSecret
     * @return 签名结果
     */
    public static String sign(Map&lt;String, object> data, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Map&lt;String, object> result = new LinkedHashMap&lt;>();
        // 按照key的字典序升序排列
        data.entrySet().stream().filter(it -> it.getValue() != null)
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
        // 拼接字符串
        String param = StringUtils.join(Arrays.asList(result.entrySet().stream()
                .map(it -> it.getKey() + "=" + String.valueOf(it.getValue())).toArray()),"&");
        // MD5加密
        MessageDigest msgDigest = MessageDigest.getInstance("MD5");
        msgDigest.update((param + secret).getBytes(StandardCharsets.UTF_8));
        // 转为16进制字符串
        byte[] digest = msgDigest.digest();
        int l = digest.length;
        char[] out = new char[l &lt;&lt; 1];
        for (int i = 0, j = 0; i &lt; l; i++) {
            out[j++] = DIGITS_DATA[(0xF0 & digest[i]) >>> 4];
            out[j++] = DIGITS_DATA[0x0F & digest[i]];
        }
        // 小写
        return new String(out).toLowerCase();
    }
}

    </code>

    <code v-show="activeLanguage==='third'">
import org.apache.commons.lang3.StringUtils
import kotlin.Throws
import java.security.NoSuchAlgorithmException
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

/**
 * 签名工具
 * @author tu nan
 * @date 2023/3/17
 */
object SignUtils {
    private val DIGITS_DATA = charArrayOf(
        '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    )

    /**
     * 签名
     * @param data 待签名数据
     * @param secret appSecret
     * @return 签名结果
     */
    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    fun sign(data: Map&lt;String, any>, secret: String): String {
        val result: MutableMap&lt;String, any> =
            LinkedHashMap()
        // 按照key的字典序升序排列
        data.entries.stream()
            .filter { (_, value): Map.Entry&lt;String, any> -> value != null }
            .sorted(java.util.Map.Entry.comparingByKey())
            .forEachOrdered { (key, value): Map.Entry&lt;String, any> ->
                result[key] = value
            }
        // 拼接字符串
        val param = StringUtils.join(
            Arrays.asList(*result.entries.stream()
                .map { (key, value): Map.Entry&lt;String, any> -> key + "=" + value.toString() }
                .toArray()
            ), "&"
        )
        // MD5加密
        val msgDigest = MessageDigest.getInstance("MD5")
        msgDigest.update((param + secret).toByteArray(StandardCharsets.UTF_8))
        // 转为16进制字符串
        val digest = msgDigest.digest();
        val l = digest.size
        val out = CharArray(l shl 1)
        var i = 0
        var j = 0
        while (i &lt; l) {
            out[j++] = DIGITS_DATA[0xF0 and digest[i]
                .toInt() ushr 4]
            out[j++] = DIGITS_DATA[0x0F and digest[i]
                .toInt()]
            i++
        }
        // 小写
        return String(out).lowercase(Locale.getDefault())
    }
}
    </code>

    <code v-show="activeLanguage==='fourth'">
//页面需引入md5库  https://cdn.bootcdn.net/ajax/libs/blueimp-md5/2.19.0/js/md5.js
function sign(data, app_secret){
    // 按key升序排列 拼接字符串
    var param = Object.keys(data).sort().map(function(key){return key + '=' + data[key]}).join('&');
    // md5加密
    var signResult = md5(param + secret);
    // 小写
    signResult = signResult.toLowerCase();
    return signResult
}
    </code>

    <code v-show="activeLanguage==='sixth'">
//导入相关包
package main

import (
	"crypto/md5"
	"encoding/hex"
	"fmt"
	"io"
	"sort"
	"strings"
)

func sign(data map[string]string, app_secret string) string {

	// 1. 获取map中的key,放进切片中
	var keys []string
	for k, _ := range data {
		keys = append(keys, k)
	}

	// 2. 利用切片sort方法排序
	sort.Strings(keys)

	// 3. 将排序好的数组回写到map， 再转换成list
	var resultMap = make(map[string]string)
	for _, key := range keys {
		resultMap[key] = data[key]
	}

	var list []string
	for i := range resultMap {
		var result = i + "=" + resultMap[i]
		list = append(list, result)
	}

	// 4. 中间加入 & 并加上 app_secrte
	var resultStr = strings.Join(list, "&") + app_secret

	// 5. 调用md5加密
        Md5 := md5.New()
	_, _ = io.WriteString(Md5, resultStr)
	var restltMD5 =  hex.EncodeToString(Md5.Sum(nil))
	fmt.Println(restltMD5)
  return restltMD5
}
   </code>
  </pre>
  </div>
</template>

<script>
export default {
  name: 'SignExampleCode',
  props: {
    activeLanguage: {
      type: String,
      default: 'first'
    }
  }
}
</script>
<style lang="scss">
.highlight {
    overflow-y: scroll;
    max-height: 300px;
    pre {
      display: flex;
      padding: 9.5px;
      margin: 0 0 10px;
      font-size: 13px;
      line-height: 1.42857143;
      color: #333;
      word-break: break-all;
      word-wrap: break-word;
      background: #fff;
      overflow: auto;
      white-space: pre;
  }
}

</style>
