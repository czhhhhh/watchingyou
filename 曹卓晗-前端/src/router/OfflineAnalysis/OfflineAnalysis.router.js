export default{
    path: '/offlineAnalysis',
    name: 'OfflineAnalysis',
    component: ()=>import('@/views/OfflineAnalysis.vue'),
    meta:{//需要登录
        token:true
    }
}