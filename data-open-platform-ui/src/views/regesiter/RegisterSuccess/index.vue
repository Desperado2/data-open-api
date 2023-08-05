<template>
  <el-container style="min-height:100%;position: relative;">

    <home-index />

    <el-main style="height:400px">
      <div style="width:100%;heigth:400px;margin-top: 180px;">
        <div style="display: flex;justify-content: center;">
          <img>
        </div>
        <div style="display: flex;justify-content: center;font-size:20pt; padding:10px">
          注册成功
        </div>
        <div style="display: flex;justify-content: center; padding:10px">
          请注意查收激活邮件
        </div>
        <div style="display: flex;justify-content: center;  padding:10px">
          <el-button type="primary" size="medium" @click="toIndex">去首页</el-button>
          <el-button type="primary" size="medium" @click="toLogin">去登录</el-button>
        </div>
      </div>
    </el-main>
<!--    <el-footer style="padding:0; float:bottom">-->
<!--      <home-footer style="position: absolute;bottom: 0;width: 100%;" />-->
<!--    </el-footer>-->
  </el-container>
</template>

<script>
import HomeIndex from '@/views/home/component/HomeHeader'
import HomeFooter from '@/views/home/component/HomeFooter'

export default {
  name: 'RegesiterSuccess',
  components: { HomeIndex, HomeFooter },
  data() {
    return {

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

  },
  methods: {
    toIndex() {
      this.$router.push('/')
    },
    toLogin() {
      this.$router.push({
        name: 'registerOrLogin',
        params: {
          isLogin: true
        }
      })
    }
  }
}
</script>
