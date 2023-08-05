import Vue from 'vue'
import Router from 'vue-router'
/* Layout */
import Layout from '@/layout'

Vue.use(Router)

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/register',
    name: 'registerOrLogin',
    component: () => import('@/views/regesiter/index'),
    hidden: true
  },
  {
    path: '/register/success',
    component: () => import('@/views/regesiter/RegisterSuccess/index'),
    hidden: true
  },
  {
    path: '/active/error',
    component: () => import('@/views/active/error'),
    hidden: true
  },
  {
    path: '/active/success',
    component: () => import('@/views/active/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: () => import('@/views/home/index'),
    hidden: true
  },
  {
    path: '/apiview/',
    name: 'ApiView',
    component: () => import('@/views/home/ApiDetail/index'),
    hidden: true
  },
  {
    path: '/apiList',
    name: 'ApiList',
    component: () => import('@/views/home/apiList/index'),
    hidden: true
  },
  {
    path: '/apiSearch',
    name: 'ApiSearch',
    component: () => import('@/views/home/search/index'),
    hidden: true
  },
  {
    path: '/sign-explain',
    component: () => import('@/views/home/info/index'),
    hidden: true
  }
]

export const asyncRoutes = [

  {
    path: '/home',
    component: Layout,
    redirect: '/home/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: 'Dashboard', icon: 'dashboard', roles: ['admin', 'normal'] }
    }]
  },

  {
    path: '/user-manage',
    component: Layout,
    redirect: '/user/table',
    name: '用户管理',
    meta: { title: '用户管理', icon: 'el-icon-user-solid', roles: ['admin'] },
    children: [
      {
        path: 'user',
        name: '用户',
        component: () => import('@/views/user/index'),
        meta: { title: '用户管理', icon: 'el-icon-user-solid', roles: ['admin'] }
      },
      {
        path: 'role',
        name: '角色',
        component: () => import('@/views/user/role/index'),
        meta: { title: '角色', icon: 'el-icon-s-promotion', roles: ['admin'] }
      }
    ]
  },

  {
    path: '/api-manage',
    component: Layout,
    redirect: '/api/table',
    name: '开放API管理',
    meta: { title: '开放API管理', icon: 'el-icon-share', roles: ['admin'] },
    children: [
      {
        path: 'classify',
        name: 'API分类管理',
        component: () => import('@/views/api/classify/index'),
        meta: { title: 'API分类管理', icon: 'el-icon-s-fold', roles: ['admin'] }
      },
      {
        path: 'api',
        name: 'APIMANAGE',
        component: () => import('@/views/api/index'),
        meta: { title: 'API管理', icon: 'el-icon-share', roles: ['admin'], keepAlive: true }
      },
      {
        path: 'api-dev',
        name: 'devApi',
        component: () => import('@/views/devconsole/index'),
        meta: { title: 'API管理', icon: 'el-icon-share', roles: ['admin'] },
        hidden: true
      }
    ]
  },

  {
    path: '/data-manage',
    component: Layout,
    redirect: '/data/table',
    name: '数据管理',
    meta: { title: '数据管理', icon: 'el-icon-share', roles: ['normal', 'admin'] },
    children: [
      {
        path: 'secret',
        name: '我的秘钥KEY',
        component: () => import('@/views/secret/index'),
        meta: { title: '我的秘钥KEY', icon: 'el-icon-s-fold', roles: ['normal'] }
      },
      {
        path: 'my-api',
        name: '我申请的接口',
        component: () => import('@/views/subscribe/index'),
        meta: { title: '我申请的接口', icon: 'el-icon-share', roles: ['normal'] }
      },
      {
        path: 'approval-api',
        name: '申请审核',
        component: () => import('@/views/subscribe/approval/index'),
        meta: { title: '申请审核', icon: 'el-icon-share', roles: ['admin'] }
      },
      {
        path: 'logging',
        name: '请求日志',
        component: () => import('@/views/logs/index'),
        meta: { title: '请求日志', icon: 'el-icon-share', roles: ['admin'] }
      },
      {
        path: 'logging-user',
        name: '请求日志',
        component: () => import('@/views/logs/user/index'),
        meta: { title: '请求日志', icon: 'el-icon-share', roles: ['normal'] }
      }
    ]
  },

  {
    path: '/data-source',
    component: Layout,
    redirect: '/data/table',
    name: '数据源管理',
    meta: { title: '数据源管理', icon: 'el-icon-share', roles: ['admin'] },
    children: [
      {
        path: 'datasource',
        name: '数据源管理',
        component: () => import('@/views/datasource/index'),
        meta: { title: '数据源管理', icon: 'el-icon-s-fold', roles: ['admin'] }
      }
    ]
  },

  {
    path: '/data-utils',
    component: Layout,
    redirect: '/data/table',
    name: '小工具',
    meta: { title: '小工具', icon: 'el-icon-share', roles: ['admin', 'normal'] },
    children: [
      {
        path: 'sign-utils',
        name: '签名测试',
        component: () => import('@/views/utils/sign/index'),
        meta: { title: '签名测试', icon: 'el-icon-s-fold', roles: ['normal'] }
      },
      {
        path: 'udf-manage',
        name: 'UDF函数',
        component: () => import('@/views/udf/index'),
        meta: { title: 'UDF函数说明', icon: 'el-icon-s-fold', roles: ['admin'] }
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
