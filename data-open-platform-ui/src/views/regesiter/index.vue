<template>
  <el-container>

    <home-index />

    <el-main style="margin:40px" class="login-class">
      <div id="dowebok" class="dowebok" :class="{'right-panel-active' : isLogin==false }">
        <div v-if="isLogin==false" class="form-container sign-up-container">
          <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="login-form" auto-complete="on" label-position="left">
            <div class="title-container">
              <h1 class="title">注册</h1>
            </div>

            <el-form-item prop="email" style="width:100%">
              <el-input
                ref="email"
                v-model="registerForm.email"
                placeholder="请输入邮箱"
                name="email"
                type="text"
                tabindex="1"
                auto-complete="on"
              />
            </el-form-item>

            <el-form-item prop="password" style="width:100%">
              <el-input
                :key="passwordType"
                ref="password"
                v-model="registerForm.password"
                :type="passwordType"
                placeholder="请输入密码"
                name="password"
                tabindex="2"
                auto-complete="on"
                show-password
              />
            </el-form-item>

            <el-form-item prop="name" style="width:100%">
              <el-input
                ref="name"
                v-model="registerForm.name"
                placeholder="请输入项目名称"
                name="name"
                type="text"
                tabindex="1"
                auto-complete="on"
              />
            </el-form-item>
            <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleRegister">注册</el-button>
          </el-form>
        </div>
        <div v-if="isLogin" class="form-container sign-in-container">
          <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
            <div class="title-container">
              <h1 class="title">登录</h1>
            </div>

            <el-form-item prop="email" style="width:100%">
              <el-input
                ref="email"
                v-model="loginForm.email"
                placeholder="请输入邮箱"
                name="email"
                type="text"
                tabindex="1"
                auto-complete="on"
              />
            </el-form-item>

            <el-form-item prop="password" style="width:100%">
              <el-input
                :key="passwordType"
                ref="password"
                v-model="loginForm.password"
                :type="passwordType"
                placeholder="请输入密码"
                name="password"
                tabindex="2"
                auto-complete="on"
                show-password
              />
            </el-form-item>

            <el-row v-show="isShowVerificationCode" :gutter="20" style="width:100%">
              <el-col :span="16">
                <el-form-item>
                  <el-input
                    ref="codeResult"
                    v-model="loginForm.codeResult"
                    placeholder="请输入验证码"
                    name="codeResult"
                    type="text"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <img :src="verificationCodeImage" style="height:47px" @click="refreshCode">
              </el-col>
            </el-row>
            <a href="#" class="forget-password" @click="forgetPassword">忘记密码？</a>
            <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登录</el-button>
          </el-form>
        </div>
        <div class="overlay-container">
          <div class="overlay">
            <div class="overlay-panel overlay-left">
              <h1>已有帐号？</h1>
              <p>请使用您的帐号进行登录</p>
              <button class="ghost" @click="toLogin">登录</button>
            </div>
            <div class="overlay-panel overlay-right">
              <h1>没有帐号？</h1>
              <p>立即注册加入我们，和我们一起开始旅程吧</p>
              <button class="ghost" @click="toRegister">注册</button>
            </div>
          </div>
        </div>
      </div>
    </el-main>
<!--    <el-footer style="padding:0; float:bottom">-->
<!--      <home-footer />-->
<!--    </el-footer>-->
  </el-container>
</template>

<script>
import HomeIndex from '@/views/home/component/HomeHeader'
import HomeFooter from '@/views/home/component/HomeFooter'
import { validUsername, validAllowEmailSuffix, validatorPassword } from '@/utils/validate'
import { verificationCodeToken, verificationCode, register } from '@/api/user'
import { md5Encrypt } from '@/utils/md5'
import common_data from "@/views/common/index"

export default {
  name: 'Regesiter',
  components: { HomeIndex, HomeFooter },
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('请输入正确的邮箱'))
      } else if(!validAllowEmailSuffix(value, common_data.allow_email_suffix)){
        callback(new Error('不支持的邮箱地址,支持的邮箱后缀为:' + common_data.allow_email_suffix.join()))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      const {statu, msg} = validatorPassword(value)
      if (!statu) {
        callback(new Error(msg))
      } else {
        callback()
      }
    }
    return {
      isLogin: true,
      loginForm: {
        email: '',
        password: '',
        codeToken: '',
        codeResult: ''
      },
      registerForm: {
        email: '',
        password: '',
        name: ''
      },
      verificationCodeImage: '',
      isShowVerificationCode: false,
      loginRules: {
        email: [{ required: true, trigger: 'change', validator: validateUsername }],
        password: [{ required: true, trigger: 'change', validator: validatePassword }]
      },
      registerRules: {
        name: [{  required: true, pattern: '[^ \x22]+', message: '项目名称不能为空', trigger: 'change'}],
        email: [{ required: true, trigger: 'change', validator: validateUsername }],
        password: [{ required: true, trigger: 'change', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  mounted() {
    this.isLogin = this.$route.params.isLogin || true
    this.getCodeToken()
  },
  methods: {
    forgetPassword() {
      this.$message({
              message: '暂不支持找回,请联系管理员',
              type: 'success'
        })
    },
    toRegister() {
      this.$message({
              message: '暂不支持注册,请联系管理员',
              type: 'success'
        })
    },
    toLogin() {
      this.isLogin = true
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleRegister() {
      this.$message({
              message: '暂不支持注册,请联系管理员',
              type: 'success'
        })
      // this.$refs.registerForm.validate(valid => {
      //   if (valid) {
      //     this.loading = true
      //     const { email, password, name } = this.registerForm
      //     register({ email: email.trim(), password: md5Encrypt(password.trim()).toUpperCase(), name: name.trim() }).then(response => {
      //       this.$router.push({ path: '/register/success' })
      //     }).finally(() => {
      //       this.loading = false
      //     })
      //   }
      // })
    },
    handleLogin() {
      var that = this
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.loading = false
            this.$router.push({ path: this.redirect || '/home' })
          }).catch((error) => {
            if (error.toString() === 'Error: 帐号密码错误超过最大次数') {
              that.isShowVerificationCode = true
              that.getCodeToken()
            } else {
              if (that.isShowVerificationCode) {
                that.refreshCode()
              }
            }
            this.loading = false
          })
        } else {
          return false
        }
      })
    },
    getCodeToken() {
      if (this.isShowVerificationCode) {
        verificationCodeToken().then(response => {
          this.loginForm.codeToken = response.data
          this.getVerificationCode(this.loginForm.codeToken)
        })
      }
    },
    getVerificationCode() {
      verificationCode(this.loginForm.codeToken).then(response => {
        this.verificationCodeImage = 'data:image/jpg;base64,' + btoa(new Uint8Array(response).reduce((data, byte) => data + String.fromCharCode(byte), ''))
      })
    },
    refreshCode() {
      if (this.isShowVerificationCode) {
        if (this.loginForm.codeToken) {
          this.getVerificationCode()
        } else {
          this.getCodeToken()
        }
      }
    }
  }
}
</script>

<style>
* {
    box-sizing: border-box;
}

.login-class {
    font-family: 'Montserrat', sans-serif;
    background: #f6f5f7;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin: -20px 0 50px;
}

h1 {
    font-weight: bold;
    margin: 0;
}

p {
    font-size: 14px;
    line-height: 20px;
    letter-spacing: .5px;
    margin: 20px 0 30px;
}

span {
    font-size: 12px;
}

.forget-password {
    color: #333;
    font-size: 14px;
    text-decoration: none;
    margin: 15px 0;
}

.dowebok {
    background: #fff;
    border-radius: 10px;
    box-shadow: 0 14px 28px rgba(0, 0, 0, .25), 0 10px 10px rgba(0, 0, 0, .22);
    position: relative;
    overflow: hidden;
    width: 768px;
    max-width: 100%;
    min-height: 540px;
}

.form-container form {
    background: #fff;
    display: flex;
    flex-direction: column;
    padding:  0 50px;
    height: 100%;
    justify-content: center;
    align-items: center;
    text-align: center;
}

.social-container {
    margin: 20px 0;
}

.social-container a {
    border: 1px solid #ddd;
    border-radius: 50%;
    display: inline-flex;
    justify-content: center;
    align-items: center;
    margin: 0 5px;
    height: 40px;
    width: 40px;
}

.social-container a:hover {
    background-color: #eee;
}

.form-container input {
    background: #eee;
    border: none;
    padding: 12px 15px;
    margin: 8px 0;
    width: 100%;
    outline: none;
}

button {
    border-radius: 20px;
    border: 1px solid #ff4b2b;
    background: #ff4b2b;
    color: #fff;
    font-size: 12px;
    font-weight: bold;
    padding: 12px 45px;
    letter-spacing: 1px;
    text-transform: uppercase;
    transition: transform 80ms ease-in;
    cursor: pointer;
}

.button {
    border-radius: 20px;
    border: 1px solid #ff4b2b;
    background: #ff4b2b;
    color: #fff;
    font-size: 12px;
    font-weight: bold;
    padding: 12px 45px;
    letter-spacing: 1px;
    text-transform: uppercase;
    transition: transform 80ms ease-in;
    cursor: pointer;
}

button:active {
    transform: scale(.95);
}

button:focus {
    outline: none;
}

button.ghost {
    background: transparent;
    border-color: #fff;
}

.form-container {
    position: absolute;
    top: 0;
    height: 100%;
    transition: all .6s ease-in-out;
}

.sign-in-container {
    left: 0;
    width: 50%;
    z-index: 2;
}

.sign-up-container {
    left: 0;
    width: 50%;
    z-index: 1;
    opacity: 0;
}

.overlay-container {
    position: absolute;
    top: 0;
    left: 50%;
    width: 50%;
    height: 100%;
    overflow: hidden;
    transition: transform .6s ease-in-out;
    z-index: 100;
}

.overlay {
    background: #ff416c;
    background: linear-gradient(to right, #ff4b2b, #ff416c) no-repeat 0 0 / cover;
    color: #fff;
    position: relative;
    left: -100%;
    height: 100%;
    width: 200%;
    transform: translateY(0);
    transition: transform .6s ease-in-out;
}

.overlay-panel {
    position: absolute;
    top: 0;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 0 40px;
    height: 100%;
    width: 50%;
    text-align: center;
    transform: translateY(0);
    transition: transform .6s ease-in-out;
}

.overlay-right {
    right: 0;
    transform: translateY(0);
}

.overlay-left {
    transform: translateY(-20%);
}

/* Move signin to right */
.dowebok.right-panel-active .sign-in-container {
    transform: translateY(100%);
}

/* Move overlay to left */
.dowebok.right-panel-active .overlay-container {
    transform: translateX(-100%);
}

/* Bring signup over signin */
.dowebok.right-panel-active .sign-up-container {
    transform: translateX(100%);
    opacity: 1;
    z-index: 5;
}

/* Move overlay back to right */
.dowebok.right-panel-active .overlay {
    transform: translateX(50%);
}

/* Bring back the text to center */
.dowebok.right-panel-active .overlay-left {
    transform: translateY(0);
}

/* Same effect for right */
.dowebok.right-panel-active .overlay-right {
    transform: translateY(20%);
}

</style>
