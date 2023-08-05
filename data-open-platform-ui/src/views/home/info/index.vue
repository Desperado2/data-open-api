<template>
  <div class="sign_info">
    <el-container>
      <el-header class="header" style="padding: 0;">
        <home-index />
      </el-header>
      <el-main style="margin:40px">
        <div class="title_div">
          <h2>签名算法说明</h2>
        </div>
        <el-divider />
        <div>
          <div>所有接口的访问都必须对请求参数进行签名，即使没有请求参数也需要签名信息，签名的目的是解决调用者的身份认证以及确保调用安全。</div>
          <div style="margin-top:20px;font-size:16px;font-weight:700">签名算法说明如下：</div>
          <div style="display: inline-grid;width:100%; line-height:30px">
            <span>1. 请求参数集合为M。</span>
            <span>2. 获取签名者当前系统时间，格式为yyyyMMddHHmmss作为参数signTime的值加入集合M。</span>
            <span>3. 获取用户的调用凭证appKey，将其值作为参数appKey的值加入集合M。</span>
            <span>4. 将集合M内非空参数值的参数按照参数名称ASCII码从小到大排序(字典序)。</span>
            <span>5. 使用URL键值对的格式(即 key1=value1&key2=value2......)，将集合M中的数据拼接为字符串A。</span>
            <span>6. 在字符串A最后拼接上秘钥APPSECRET，得到新的字符串B。</span>
            <span>7. 对字符串B进行MD5运算，得到32位的16进制字符串，并将其转为小写，即为签名值。</span>
          </div>

          <div style="margin-top:20px;font-size:16px;font-weight:700">
            注意点：
          </div>
          <div style="display: inline-grid;width:100%; line-height:30px">
            <span>1. 参数名必须按照ASCII码从小到大排序(字典序)。</span>
            <span>2. 参与签名的必须为值非空的参数，值为空的参数不参与签名。</span>
            <span>3. 参数名区分大小写。</span>
            <span>4. 接口可能增加字段，签名时必须支持增加的扩展字段。</span>
            <span>5. 系统当前时间必须和标准时间保持一致，误差不能超过一分钟。</span>
          </div>

          <div style="margin-top:20px;font-size:20px;font-weight:700">示例</div>
          <div style="margin-top:20px">
            用户的appKey和appSecret值如下：
            <div style="display: inline-grid;width:100%;background:#eeeeee;">
              <span>appKey: dfd167aa7b284e7fa09b4a9905465cb2</span>
              <span>appSecret: 37dff6fc19ee4ab78a19a9c9a3ce7713</span>
            </div>
          </div>
          <div style="margin-top:20px">
            假设请求参数如下：
            <div style="display: inline-grid;width:100%;background:#eeeeee;">
              <span>appKey=dfd167aa7b284e7fa09b4a9905465cb2</span>
              <span>id=1</span>
              <span>provinceName=浙江省</span>
              <span>signTime=20230316185500</span>
            </div>
          </div>

          <div style="margin-top:20px">
            则集和M为
            <div style="display: inline-grid;width:100%;background:#eeeeee;">
              <span>appKey=dfd167aa7b284e7fa09b4a9905465cb2</span>
              <span>id=1</span>
              <span>provinceName=浙江省</span>
              <span>signTime=20230316185500</span>
            </div>
          </div>

          <div style="display: inline-grid;width:100%;margin-top:20px">
            <span>对其进行字典序排序得到的字符串A为</span>
            <span style="background:#eeeeee;">appKey=dfd167aa7b284e7fa09b4a9905465cb2&id=1&provinceName=浙江省&signTime=20230316185500</span>
          </div>

          <div style="display: inline-grid;width:100%;margin-top:20px">
            <span>拼接appsecret之后得到的字符串B为:</span>
            <span style="background:#eeeeee;">appKey=dfd167aa7b284e7fa09b4a9905465cb2&id=1&provinceName=浙江省&signTime=2023031618550037dff6fc19ee4ab78a19a9c9a3ce7713</span>
          </div>

          <div style="display: inline-grid;width:100%;margin-top:20px">
            <span>对其进行MD5运行并进行小写，得到的值即为签名</span>
            <span style="background:#eeeeee;">60578e2546a83189c64d13657f3adf2e</span>
          </div>
        </div>

        <div style="margin-top: 20px">
          <div style="margin-top:20px;font-size:16px;font-weight:700"> 参考代码</div>
          <div>
            <p style="margin: 6px 0px">此处代码并非完整项目，仅供参考，实际使用请根据本地开发环境修改。</p>
            <el-tabs v-model="activeName" type="card">
              <el-tab-pane label="Python" name="first">
                <sign-example-code active-language="first" />
              </el-tab-pane>
              <el-tab-pane label="Java" name="second">
                <sign-example-code active-language="second" />
              </el-tab-pane>
              <el-tab-pane label="Kotlin" name="third">
                <sign-example-code active-language="third" />
              </el-tab-pane>
              <el-tab-pane label="JavaScript" name="fourth">
                <sign-example-code active-language="fourth" />
              </el-tab-pane>
              <el-tab-pane label="GO" name="sixth">
                <sign-example-code active-language="sixth" />
              </el-tab-pane>
            </el-tabs>
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
import { classifyList, apiList } from '@/api/index-api'
import SignExampleCode from '@/views/home/info/SignExampleCode'

export default {
  name: 'Home',
  components: { HomeIndex, HomeFooter, SignExampleCode },
  data() {
    return {
      activeName: 'first',
      classifyList: [],
      classifyApiList: [],
      carouselList: [
        '@/assets/index_images/1.jpeg',
        '@/assets/index_images/2.jpeg',
        '@/assets/index_images/3.jpeg',
        '@/assets/index_images/4.jpg'
      ]
    }
  },
  mounted() {
    this.getClassifyList()
    this.getApiList()
  },
  methods: {
    toApiDetail() {
      this.$router.push({
        name: 'ApiView',
        params: {
          apiId: 123
        }
      })
    },
    getClassifyList() {
      classifyList().then(response => {
        this.classifyList = response.data
      })
    },
    getApiList() {
      apiList().then(response => {
        this.classifyApiList = response.data
      })
    }
  }
}
</script>
<style lang='scss'>
.sign_info {
    .title_div{
            widows: 100%;
            text-align: center;
            h2{
            color: grey;
            }
        }
}

</style>
