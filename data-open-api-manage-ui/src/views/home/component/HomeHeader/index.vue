<template>

  <div class="navbar">
    <el-row style="width:100%">
      <a href="/">
        <el-col :span="6">
          <div class="logo">
            <span style="font-size:30pt;font-weight:900;color:#fff">数据开放平台</span>
          </div>
        </el-col>
      </a>

      <el-col :span="6">
        <div class="menu1">
          <router-link :to="{ path:'/'}" style="color:#fff">
            <div>首页</div>
          </router-link>
          <router-link :to="{ path:'/apiList' }" style="color:#fff">
            <div>菜单</div>
          </router-link>
          <a href="#" @click="tips">
            <div>关于</div>
          </a>
        </div></el-col>
      <el-col :span="12">
        <div class="right-menu">
          <div>
            <el-input
              v-model="searchWord"
              placeholder="请输入内容"
              suffix-icon="el-icon-search"
              @keyup.native.enter="searchApiByKeyword"
            />
          </div>
          <div v-if="logined===false" style="display:flex">
            <div>
              <el-button type="primary" @click="login">登录</el-button>
            </div>
          </div>
          <div v-if="logined===true" style="display:flex">
            <div>
              <router-link to="/home" style="color:#fff">
                控制台
              </router-link>
            </div>
            <el-dropdown class="avatar-container" trigger="click">
              <div class="avatar-wrapper" style="color:#fff">
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
          </div>

        </div></el-col>
    </el-row>

  </div>
</template>

<script>
import { getToken } from '@/utils/auth'
import store from '@/store'
export default {
  name: 'HomeHeader',
  data() {
    return {
      searchWord: '',
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
    searchApiByKeyword() {
      if (this.searchWord === '' || this.searchWord === undefined || this.searchWord.trim() === '') {
        this.$message('搜索关键词不能为空')
      } else {
        this.$router.push({
          path: '/apiSearch',
          query: {
            searchWord: this.searchWord.trim()
          }
        })
      }
    },
    tips() {
      this.$message('开发中......')
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
  background-image: linear-gradient(180deg, #2b8ef1, #17a8ed);
  display: flex;
  -webkit-box-shadow: 0 2px 1px 0 rgba(0, 0, 0, 0.1);
  box-shadow: 0 2px 1px 0 rgba(0, 0, 0, 0.1);
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
