import Vue from 'vue'
import VueRouter from 'vue-router'

let routerList = []
function importAll(r){
  console.log(r.keys())
  r.keys().forEach(item=>{
    routerList.push(r(item).default)
  })
}
importAll(require.context('./',true,/\.router\.js/))
Vue.use(VueRouter)

const routes = [
  ...routerList
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
