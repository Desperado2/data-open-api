<template>
  <el-container style="min-height:100%;position: relative;">

    <home-index />

    <el-main style="height:400px">
      <div style="width:100%;heigth:400px;margin-top: 180px;">
        <div style="display: flex;justify-content: center;">
          <img>
        </div>
        <div style="display: flex;justify-content: center;font-size:20pt; padding:10px">
          激活失败
        </div>
      </div>
    </el-main>
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
