<template>

  <div class="navbar">
    <el-row style="width:100%">
      <el-col :span="24">
        <div class="right-menu">
          <div style="color: #fff">
            <a href="/"> 回到首页 </a>
          </div>
          <el-dropdown class="avatar-container" trigger="click">
            <div class="avatar-wrapper" style="color:#fff;">
              {{ username }}
              <i class="el-icon-caret-bottom" />
            </div>
            <el-dropdown-menu slot="dropdown" class="user-dropdown dropdownPop">
              <router-link to="/home">
                <el-dropdown-item>
                  个人中心
                </el-dropdown-item>
              </router-link>
              <el-dropdown-item divided @click.native="logout">
                <span style="display:block;">退出</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div></el-col>
    </el-row>

  </div>
</template>

<script>
import { getToken } from '@/utils/auth'
import store from '@/store'
export default {
  data() {
    return {
      logined: false,
      username: ''
    }
  },
  created() {
    const token = getToken()
    if (token) {
      this.logined = true
      this.username = store.getters.name
    }
  },
  methods: {
    toIndex() {
      this.$router.push('/')
    },
    login() {
      this.$router.push({
        name: 'registerOrLogin',
        params: {
          isLogin: true
        }
      })
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.logined = false
      this.$router.push(`/`)
    },
    register() {
      this.$router.push({
        name: 'registerOrLogin',
        params: {
          isLogin: false
        }
      })
    }
  }
}
</script>

<style lang="scss">
.navbar {
  width:100%;
  height: 80px;
  line-height: 80px;
  overflow: hidden;
  position: relative;
  background: #fff;
  background-color: rgb(28, 158, 239);
  display: flex;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}
  .logo {
    height: 100%;
    line-height: 80px;
    margin-left: 60px;
  }
  .menu1{
    line-height: 80px;
    display: flex;
    div {
      margin-left: 40px;
      font-size: 16pt;
      color: #fff;
    }
  }

  .right-menu {
    height: 100%;
    margin-right: 60px;
    line-height: 80px;
    display: flex;
    justify-content: flex-end;

    div{
      margin-right: 20px;
    }

    &:focus {
      outline: none;
    }
  }

.el-popper.dropdownPop {
  background-color: #0f93ee;  // 背景颜色
  border: 1px solid #0f93ee;
  .el-dropdown-menu__item { // 下拉菜单文字颜色
    color: #fff !important;
  }
  .popper__arrow::after {
    border-bottom-color: #0f93ee !important;
    border-top-color: #0f93ee !important;
  }
  .el-dropdown-menu__item:not(.is-disabled):hover {
    background: #40a8ee !important;
  }
  .el-dropdown-menu__item--divided:before{
     background-color: #0f93ee;
  }
}
</style>
