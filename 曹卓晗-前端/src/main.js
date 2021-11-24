import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './router/routerRight.js'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/global.css'
import echarts from 'echarts'
import china from 'echarts/map/json/china.json'
import axios from 'axios'
import VueAxios from 'vue-axios'
import {Drawer} from 'element-ui'
//配置请求根路径
axios.defaults.baseURL = 'http://192.168.43.32:8081'
// Vue.use(path)
Vue.prototype.$http = axios
Vue.use(VueAxios,axios)
Vue.use(ElementUI)
Vue.config.productionTip = false
Vue.prototype.$echarts = echarts
echarts.registerMap('china',china)
Vue.use(Drawer)
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
