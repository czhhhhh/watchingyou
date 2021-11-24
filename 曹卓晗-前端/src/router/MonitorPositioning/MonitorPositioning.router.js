export default{
    path: '/monitorPositioning',
    name: 'MonitorPositioning',
    component: ()=>import('@/views/MonitorPositioning.vue'),
    meta:{//需要登录
        token:true
    }
}