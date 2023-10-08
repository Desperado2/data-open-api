<template>
  <div style="background: #f7f7f7;">
    <el-container>
      <el-header class="header" style="padding: 0;">
        <home-index />
      </el-header>
      <el-main style="margin:80px; background:#fff; padding:30px">
        <div style="width:80%">
          <div>
            <div style="display:flex">
              <div class="api-item">
                <img :src="apiData.imageUrl">
                <strong>{{ apiData.name }}</strong>
                <span>{{ apiData.description }}</span>
                <p>
                  <small>
                    <i class="el-icon-user-solid" />
                    <span class="o">244</span>
                  </small>
                  <small>
                    <i class="el-icon-star-on" />
                    <span class="o">281</span>
                  </small>
                </p>
              </div>
              <div>
                <div style="font-size:20pt; padding:30px">{{ apiData.name }}</div>
                <div style="padding-left:30px; padding-bottom:30px">{{ apiData.description }}</div>
                <div style="padding-left:30px">
                  <el-button type="primary" @click="applyApi(apiData.id, apiData.openApplyStatus, apiData.notOpenApplyReason)">申请接口</el-button>
                  <el-button type="primary" @click="applyTestApi(apiData.id, apiData.openApplyStatus, apiData.notOpenApplyReason)">在线测试</el-button>
                </div>
              </div>
            </div>
          </div>
          <div style="display:flex; margin-top:20px">
            <div style="padding-right:40px">▼ 接口文档</div>
            <div style="padding-right:40px">请求参数</div>
            <div style="padding-right:40px">返回示例</div>
            <div style="padding-right:40px">参考代码</div>
            <div style="padding-right:40px">帮助</div>
          </div>
          <el-divider />
          <div style="margin-top: 20px" v-if="apiData.openApiDetailsRequestModel">
            {{ apiData.openApiDetailsRequestModel.detailDescription }}
          </div>
          <div style="margin-top: 20px">
            <div style="font-size:15pt; font-weight:700;color:#777; margin-bottom:10px">▼ 接口信息</div>
            <div style="display: inline-grid;background-color:#f7f7f7;border:1px solid #cecece;width:100%">
              <span style="margin: 6px 0px">接口地址：{{ apiData.apiAddressPrefix + apiData.openApiDetailsRequestModel.apiAddress }} </span>
              <span style="margin: 6px 0px">支持协议：{{ apiData.openApiDetailsRequestModel.protocol }}</span>
              <span style="margin: 6px 0px">请求方式：{{ apiData.openApiDetailsRequestModel.requestMode }}</span>
              <span style="margin: 6px 0px">返回格式：{{ apiData.openApiDetailsRequestModel.returnFormat }}</span>
            </div>
          </div>

          <div style="margin-top: 20px">
            <div style="font-size:15pt;font-weight:700;color:#777; margin-bottom:10px">▼ 请求参数</div>
            <div style="display: inline-grid; width:100%">
              <span style="margin: 6px 0px">post方式请求时，enctype应为application/x-www-form-urlencoded </span>
              <span style="margin: 6px 0px">上传文件二进制数据流方式，enctype必须为multipart/form-data</span>
              <span style="margin: 6px 0px">参数url、base64中有特殊字符时，建议对值urlencode编码后传递</span>

              <table class="tbparm">
                <thead>
                  <tr class="firstRow">
                    <th style="width:100px">名称</th>
                    <th style="width:80px">类型</th>
                    <th style="width:50px">必填</th>
                    <th style="width:300px">示例值/默认值</th>
                    <th style="width:235px">说明</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td colspan="5" style="text-align: center;">公共请求头</td>
                  </tr>
                  <tr>
                    <td>execute-env</td>
                    <td>String</td>
                    <td>是</td>
                    <td>test</td>
                    <td>请求环境,测试环境-test,预发布环境-pre,正式环境-prod</td>
                  </tr>
                  <tr>
                    <td colspan="5" style="text-align: center;">公共参数</td>
                  </tr>
                  <tr>
                    <td>appKey</td>
                    <td>String</td>
                    <td>是</td>
                    <td>4f894499d10eafa62973b4e32a07a5ca</td>
                    <td>请求apiKey</td>
                  </tr>
                  <tr>
                    <td>signTime</td>
                    <td>String</td>
                    <td>是</td>
                    <td>20230101131313</td>
                    <td>请求时间,格式为yyyyMMddHHmmss</td>
                  </tr>
                  <tr>
                    <td>sign</td>
                    <td>String</td>
                    <td>是</td>
                    <td>f7f8c16f708b1e3f63e104317c0698f9</td>
                    <td>签名, 参见
                      <router-link to="/sign-explain" style="color:#409eff">
                        签名算法
                      </router-link>
                    </td>
                  </tr>
                  <tr><td colspan="5" style="text-align: center;">应用参数</td></tr>
                  <tr v-for="param in apiData.openApiRequestParamsRequestModelList" :key="param.id">
                    <td>{{ param.name }}</td>
                    <td>{{ param.type }}</td>
                    <td>{{ param.required === 0 ? '否' : '是' }}</td>
                    <td id="urlhh">{{ param.exampleValue }}</td>
                    <td>{{ param.description }}</td>
                  </tr>

                </tbody>
              </table>
            </div>
          </div>

          <div style="margin-top: 20px">
            <div style="font-size:15pt; font-weight:700;color:#777; margin-bottom:10px">▼ 返回示例</div>
            <div style="display:inline-grid;width:100%;">
              <span style="margin: 6px 0px">接口数据样例仅作为预览参考，请以实际测试结果为准</span>
              <pre style="width:100%;border:1px solid #ccc" v-html="highlight(apiData.openApiDetailsRequestModel.returnExample)" />
            </div>
          </div>

          <div style="margin-top: 20px">
            <div style="font-size:15pt; font-weight:700;color:#777; margin-bottom:10px">▼ 返回参数</div>
            <div style="width: 100%;">
              <span style="margin: 6px 0px">公共参数指所有接口都会返回的参数，其中返回码=E00000>表示成功（查看全部状态码）</span>
              <table class="tbparm">
                <thead>
                  <tr class="firstRow">
                    <th style="width:100px">名称</th>
                    <th style="width:80px">类型</th>
                    <th style="width:300px">示例值</th>
                    <th style="width:235px">说明</th>
                  </tr>
                </thead>
                <tbody>
                  <tr><td colspan="4" style="text-align: center;">公共参数</td></tr>
                  <tr v-for="params in apiData.publicResponseModel" :key="params.name">
                    <td>{{ params.name }}</td>
                    <td>{{ params.type }}</td>
                    <td>{{ params.exampleValue }}</td>
                    <td>{{ params.description }}</td>
                  </tr>
                  <tr><td colspan="4" style="text-align: center;">应用参数</td></tr>
                  <tr v-for="params in apiData.openApiResponseRequestModelList" :key="params.name">
                    <td>{{ params.name }}</td>
                    <td>{{ params.type }}</td>
                    <td>{{ params.exampleValue }}</td>
                    <td>{{ params.description }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <div style="margin-top: 20px">
            <div style="font-size:15pt; font-weight:700;color:#777; margin-bottom:10px">▼ 返回状态码</div>
            <div>
              <span style="margin: 6px 0px">错误信息可能会有所调整，请根据错误状态码进行流程判断</span>
              <table class="tbparm">
                <thead>
                  <tr class="firstRow">
                    <th style="width:100px">错误状态码</th>
                    <th style="width:200px">错误信息</th>
                    <th style="width:500px">解释帮助</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>E99999</td>
                    <td>内部服务器错误</td>
                    <td>报此错误码请及时反馈或等待官方修复</td>
                  </tr>
                  <tr>
                    <td>E01001</td>
                    <td>参数值不符合要求</td>
                    <td>参数值不符合基本格式要求</td>
                  </tr>
                  <tr>
                    <td>E01002</td>
                    <td>签名错误</td>
                    <td>请求签名错误，点此
                      <router-link to="/sign-explain" style="color:#409eff">
                        查看签名说明
                      </router-link>
                    </td>
                  </tr>
                  <tr>
                    <td>E01003</td>
                    <td>apikey错误或为空</td>
                    <td>请检查apikey是否填写错误</td>
                  </tr>
                  <tr>
                    <td>E01004</td>
                    <td>签名已过期</td>
                    <td>签名有效期2分钟，请重新签名</td>
                  </tr>
                  <tr>
                    <td>E01005</td>
                    <td>请求时间戳不合法</td>
                    <td>请求时间戳参数格式必须为yyyyMMddHHmmss</td>
                  </tr>
                  <tr>
                    <td>E01006</td>
                    <td>您没有权限调用本API</td>
                    <td>请确认您是否已经申请本API,如未申请,请先申请</td>
                  </tr>
                  <tr>
                    <td>E01007</td>
                    <td>请求API不存在</td>
                    <td>请检查API地址是否正确</td>
                  </tr>
                  <tr>
                    <td>E02001</td>
                    <td>API暂时维护中</td>
                    <td>口暂时关闭维护中，请注意相关公告</td>
                  </tr>
                  <tr>
                    <td>E02002</td>
                    <td>API依赖数据源不存在</td>
                    <td>API不可用，请联系管理员</td>
                  </tr>
                  <tr>
                    <td colspan="3">E9开头的是系统级错误，E0开头的是用户级错误，其中E00000代码表示请求成功处理。</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <div style="margin-top: 20px">
            <div style="font-size:15pt; font-weight:700;color:#777; margin-bottom:10px">▼ 参考代码</div>
            <div>
              <p style="margin: 6px 0px">此处代码并非完整项目，仅供参考，实际使用请根据本地开发环境修改。</p>
              <el-tabs v-model="activeName" type="card">
                <el-tab-pane label="Python" name="first">
                  <example-code active-language="first" :base-path="apiData.apiAddressPrefix" :request-url="apiData.openApiDetailsRequestModel.apiAddress" />
                </el-tab-pane>
                <el-tab-pane label="Java" name="second">
                  <example-code active-language="second" />
                </el-tab-pane>
                <el-tab-pane label="Kotlin" name="third">
                  <example-code active-language="third" :base-path="apiData.apiAddressPrefix" :request-url="apiData.openApiDetailsRequestModel.apiAddress" />
                </el-tab-pane>
                <el-tab-pane label="Jquery" name="fourth">
                  <example-code active-language="fourth" :base-path="apiData.apiAddressPrefix" :request-url="apiData.openApiDetailsRequestModel.apiAddress" />
                </el-tab-pane>
                <el-tab-pane label="Node" name="fifth">
                  <example-code active-language="fifth" :base-path="apiData.apiAddressPrefix" :request-url="apiData.openApiDetailsRequestModel.apiAddress" />
                </el-tab-pane>
                <el-tab-pane label="GO" name="sixth">
                  <example-code active-language="sixth" :base-path="apiData.apiAddressPrefix" :request-url="apiData.openApiDetailsRequestModel.apiAddress" />
                </el-tab-pane>
                <el-tab-pane label="Vue" name="seventh">
                  <example-code active-language="seventh" :base-path="apiData.apiAddressPrefix" :request-url="apiData.openApiDetailsRequestModel.apiAddress" />
                </el-tab-pane>
                <el-tab-pane label="Node" name="eighth">
                  <example-code active-language="eighth" :base-path="apiData.apiAddressPrefix" :request-url="apiData.openApiDetailsRequestModel.apiAddress" />
                </el-tab-pane>
              </el-tabs>
            </div>
          </div>
        </div>
      </el-main>
<!--      <el-footer style="padding:0">-->
<!--        <home-footer />-->
<!--      </el-footer>-->
    </el-container>

  </div>
</template>

<script>
import HomeIndex from '@/views/home/component/HomeHeader'
import HomeFooter from '@/views/home/component/HomeFooter'
import { syntaxHighlight } from '@/utils/json'
import ExampleCode from '@/views/home/ApiDetail/exampleCode'
import { apiDetail } from '@/api/index-api'
import { subscribe } from '@/api/subscribe'
import { getToken } from '@/utils/auth'
import store from '@/store'
import { Loading } from 'element-ui'

export default {
  name: 'Home',
  components: { HomeIndex, HomeFooter, ExampleCode },
  data() {
    return {
      apiId: -1,
      activeName: 'first',
      apiData: {
        id: null,
        name: null,
        description: null,
        classifyId: null,
        imageUrl: null,
        apiAddressPrefix: null,
        publicResponseModel: [],
        openApiDetailsRequestModel: {
          id: null,
          apiId: null,
          detailDescription: null,
          apiAddress: null,
          protocol: null,
          requestMode: null,
          returnFormat: null,
          returnExample: null
        },
        openApiRequestParamsRequestModelList: [],
        openApiResponseRequestModelList: []
      }
    }
  },
  mounted() {
    const newLoading = Loading.service({ fullscreen: true, background: 'rgb(200,200,200,0.5)' })
    this.apiId = this.$route.query.id || -1
    this.getApiDetail(newLoading)
  },
  methods: {
    highlight(str) {
      return syntaxHighlight(JSON.parse(str))
    },
    login() {
      this.$router.push(`/login`)
    },
    register() {
      this.$router.push(`/register`)
    },
    getApiDetail(newLoading) {
      apiDetail(this.apiId).then(response => {
        this.apiData = response.data
      }).finally(() => {
        newLoading.close()
      })
    },
    applyApi(id, openApplyStatus, notOpenApplyReason) {
      // 订阅
      const token = getToken()
      const roles = store.getters.roles

      if (roles.indexOf('admin') > -1) {
        this.$message({
          message: '管理员不能订阅......',
          type: 'error'
        })
      } else {
        if (openApplyStatus === 1) {
          this.$message({
            message: notOpenApplyReason || 'API维护中,暂不支持申请......',
            type: 'error'
          })
        } else if (token === null || token === undefined || token === '') {
          this.$router.push({
            name: 'registerOrLogin',
            params: {
              isLogin: true
            }
          })
        } else {
          subscribe({ apiId: id }).then(response => {
            // 成功跳转页面
            this.$router.push(`/data-manage/my-api`)
          })
        }
      }
    },
    applyTestApi(id, openApplyStatus, notOpenApplyReason) {
      if (openApplyStatus === 1) {
        this.$message({
          message: '该API暂不支持测试',
          type: 'error'
        })
      } else {
        this.$message({
          message: '实现中......',
          type: 'success'
        })
      }
    }
  }
}
</script>

<style lang="scss">

.tbparm{
    border-collapse: collapse;
    margin-top: 20px;
    width: 100%;
    display: table;
    border-color: grey;

    thead {
        display: table-header-group;
        vertical-align: middle;
        border-color: inherit;
    }

tr {
    border: 1px solid #cecece;
}
th {
    text-align: left;
    padding: 10px 0px;
    background: rgb(233, 233, 233);
    padding-left: 6px;
    font-weight: 600;
    border-color: rgb(206, 206, 206);
}
tbody {
    display: table-row-group;
    vertical-align: middle;
    border-color: inherit;
}
 tr {
    border: 1px solid #cecece;
}
td {
    background: rgb(255, 255, 255);
    border: 1px solid #cecece;
    padding: 10px 0px;
    padding-left: 6px;
    display: table-cell;
    vertical-align: inherit;
}
}
pre {
    white-space: pre-wrap;
    word-wrap: break-word;
    .string {
    color: green;
}
.key {
    color: red;
}
.number {
    color: darkorange;
}
}
.api-item{
background-color: #fff;
    overflow: hidden;
    display: inline-block;
    margin: 12px 0;
    padding: 10px 0px;
    border: 1px solid #ececec;
    width: 252px;
    height: 210px;
    text-align: center;
  strong{
    font-weight: normal;
    font-size: 16px;
    padding-bottom: 6px;
    display: block;
  }

  span{
    color: #999999;
  }

  img{
    width: 60px;
    height: 60px;
    margin: 14px 0;
  }

  p{
    margin-top: 32px;
  }
  small{
        margin: 0px 16px;
    font-size: 14px;
    color: #ccc;
  }

i{
  vertical-align: middle;
    margin-right: 0px;
    display: inline-block;
}
.o{
      margin-left: 6px;
    vertical-align: middle;
}
}
  </style>
