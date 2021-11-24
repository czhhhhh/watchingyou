export default{
    path: '/realTimeAnalysis',
    name: 'RealTimeAnalysis',
    component: ()=>import('@/views/RealTimeAnalysis.vue'),
    meta:{//需要登录
        token:true
    }
}