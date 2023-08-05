<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"

        :text-color="variables.menuText"
        :unique-opened="false"
        :collapse-transition="false"
        mode="vertical"
        @select="menuActive"
      >
        <sidebar-item v-for="route in permission_routes" :key="route.path" :item="route" :base-path="route.path" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'

export default {
  components: { SidebarItem, Logo },
  computed: {
    ...mapGetters([
      'permission_routes',
      'sidebar'
    ]),
    // routes() {
    //   return this.$router.options.routes
    // },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return true
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  },
  methods: {
    menuActive(index, indexPath) {
    }
  }
}
</script>
<style lang="scss">
  .el-menu-item.is-active { color: #409EFF;background-color: #ffffff !important; i{color: #409EFF !important;}  }
  /* .el-submenu.is-active { color: #409EFF !important;background-color: #ffffff !important;  } */
  /* #app .sidebar-container .is-active > .el-submenu__title { color: #409EFF !important;}
  #app .sidebar-container .is-active > .el-submenu__title i { color: #409EFF !important;} */
</style>
