import router from './index'

function getToken(){
    return sessionStorage.getItem('token')
}

//登录权限设置
//进入页面之前进行拦截判断
router.beforeEach((to,from,next)=>{
    if(!getToken()){
        //未登录
        if(to.meta.token){
            //页面需要登录
            next({
                path:'/login'
            })
        }else{
            //页面不需要登录，直接进入
            next()
        }
    }else{
        //已经登录
        next()
    }
})