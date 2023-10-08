import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import splitPane from 'vue-splitpane'
import VueClipboard from 'vue-clipboard2'
import './assets/iconfont'
import '@/styles/index.scss' // global css
import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control
// 引入 highlight.js 代码高亮工具
import hljs from 'highlight.js'
// 使用样式，有多种样式可选
import 'highlight.js/styles/googlecode.css'

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
// if (process.env.NODE_ENV === 'production') {
//   const { mockXHR } = require('../mock')
//   mockXHR()
// }

// set ElementUI lang to EN
// Vue.use(ElementUI, { locale })
// 如果想要中文版 element-ui，按如下方式声明

// 增加自定义命令
Vue.directive('highlight', function(el) {
  const blocks = el.querySelectorAll('pre code')
  blocks.forEach(block => {
    hljs.highlightElement(block)
  })
})

// 增加组定义属性，用于在代码中预处理代码格式
Vue.prototype.$hljs = hljs

Vue.use(ElementUI)
Vue.component('SplitPane', splitPane)
Vue.use(VueClipboard)
Vue.config.productionTip = false
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
