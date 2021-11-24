export default{
    path: '/login',
    name: 'Login',
    component: ()=>import('@/views/Login.vue'),
    meta:{//不需要登录
        token:false
    }
}