<template>
  <div v-highlight class="highlight">
    <pre style="margin-top:0">
    <code v-show="activeLanguage==='first'">
# -*- coding: utf-8 -*-
import http.client, urllib, json
conn = http.client.HTTPSConnection('{{ basePath }}')  #接口域名
params = urllib.parse.urlencode({'appKey':'你的APIKEY', 'signTime': '20230316185500', 'sign':'签名'})
headers = {'Content-type':'application/x-www-form-urlencoded'}
conn.request('POST','{{ requestUrl }}',params,headers)
api = conn.getresponse()
result = api.read()
data = result.decode('utf-8')
dict_data = json.loads(data)
print(dict_data)
    </code>
    <code v-show="activeLanguage==='second'">
//java环境中文传值时，需特别注意字符编码问题
import java.net.*;
import java.io.*;
public class DemoAPI{
public static void main(String args[]) throws Exception {
    String api_data = "";
    try {
        URL url = new URL("{{ basePath + requestUrl }}");
        HttpURLConnection  conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setDoOutput(true);
        conn.setRequestProperty("content-type", "application/json");
        OutputStream outputStream = conn.getOutputStream();
        String content = "{\"appKey\":\"你的APIKEY\", \"signTime\": \"20230316185500\", \"sign\":\"签名\"}";
        outputStream.write(content.getBytes("utf-8"));
        outputStream.flush();
        outputStream.close();
        InputStream inputStream = conn.getInputStream();
        byte[] data = new byte[1024];
        StringBuilder api = new StringBuilder();
        while (inputStream.read(data) != -1) {
            String t = new String(data);
            api.append(t);
        }
        api_data = api.toString();
        inputStream.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    System.out.println(api_data);
}
    </code>

    <code v-show="activeLanguage==='third'">
import kotlin.Throws
import kotlin.jvm.JvmStatic
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

//环境中文传值时，需特别注意字符编码问题
object DemoAPI {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array&lt;String>) {
        var api_data = ""
        try {
            val url = URL("{{ basePath + requestUrl }}")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            conn.doOutput = true
            conn.setRequestProperty("content-type", "application/json")
            val outputStream = conn.outputStream
            val content = "{\"appKey\":\"你的APIKEY\", \"signTime\": \"20230316185500\", \"sign\":\"签名\"}"
            outputStream.write(content.toByteArray(charset("utf-8")))
            outputStream.flush()
            outputStream.close()
            val inputStream = conn.inputStream
            val data = ByteArray(1024)
            val api = StringBuilder()
            while (inputStream.read(data) != -1) {
                val t = String(data)
                api.append(t)
            }
            api_data = api.toString()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        println(api_data)
    }
}
</code>

    <code v-show="activeLanguage==='fourth'">
//页面需引入jquery库  https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js
$(document).ready(function(){
  $('#button').click(function(){  //点击页面上id为button的按钮发送请求
    $.post('{{ basePath + requestUrl }}',
      {
        'appKey':'你的APIKEY',
        'signTime': '20230316185500',
        'sign':'签名'
      },
      function(data,status){
        console.log(data);
      });
    });
});
    </code>

    <code v-show="activeLanguage==='fifth'">
//需要安装request模块
var request = require('request');
     request.post({
        url:'{{ basePath + requestUrl }}',
            form:{
                'appKey':'你的APIKEY',
                'signTime': '20230316185500',
                'sign':'签名'
        }
    },function(err,response,api_data ){
        console.log(api_data)
});
    </code>

    <code v-show="activeLanguage==='sixth'">
//导入相关包
package main
import (
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
)
func main() {
    postValue :=  url.Values{"appKey":"你的APIKEY", "signTime": "20230316185500", "sign":"签名"}
    res, _ := http.PostForm("{{ basePath + requestUrl }}",postValue)
    defer res.Body.Close()
    api_data, _ := ioutil.ReadAll(res.Body)
    fmt.Println(string(api_data))
}
    </code>

    <code v-show="activeLanguage==='seventh'">
//需安装axios、QS库，例如npm install axios并导入import axios from 'axios'
 new Vue({
            el: '#app',
            data: {
              api_data:''
            },
            methods:{
                send:function (){
                    axios({
                        method:'post',
                        url:'{{ basePath + requestUrl }}',
                        data:Qs.stringify({'appKey':'你的APIKEY', 'signTime': '20230316185500', 'sign':'签名'}),
                        headers:{'Content-Type': 'application/x-www-form-urlencoded'}
                    }).then(res => {
                        this.api_data = res.data
                    })
                }
            }
})
    </code>

    <code v-show="activeLanguage==='eighth'">
 #Linux curl命令
 curl  -X POST -H "Content-Type:application/x-www-form-urlencoded"
 -d ('appKey':'你的APIKEY', 'signTime': '20230316185500', 'sign':'签名') "{{ basePath + requestUrl }}"
    </code>
  </pre>
  </div>
</template>

<script>
export default {
  name: 'ExampleCode',
  props: {
    activeLanguage: {
      type: String,
      default: 'first'
    },
    requestUrl: {
      type: String,
      default: 'first'
    },
    basePath: {
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
