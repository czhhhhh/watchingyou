export default{
    path: '/userManage',
    name: 'UserManage',
    component: ()=>import('@/views/UserManage.vue'),
    meta:{//需要登录
        token:true
    }
}