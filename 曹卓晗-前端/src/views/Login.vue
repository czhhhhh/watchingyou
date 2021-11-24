<template>
  <div class="Login">
    <body>
      <div class="login_box">
        <!-- 标题 -->
        <div class="title">
          <img src="@/assets/images/title.png">
        </div>
        <div class="copyright">
          Copyright © 2021 whuers from 18_BWS All rights reserved.
        </div>
      </div>
      <!-- 用户登录框 -->
        <div class="log_context">
          <!-- 输入框 -->
          <el-form ref="loginFormRef" :model="user" :rules="loginRules" label-width="80px" class="el_form">
            <el-form-item label="账号" class="el_form_item" prop="account">
              <el-input placeholder="请输入账号" v-model="user.account" clearable></el-input>
            </el-form-item>
            <el-form-item label="密码" class="el_form_item" prop="password">
              <el-input placeholder="请输入密码" v-model="user.password" show-password></el-input>
            </el-form-item>
          </el-form>
          <!-- 按钮 -->
          <el-form class="button">
            <el-form-item>
              <el-button type="primary" @click="onSubmit">登录</el-button>
              <el-button @click="unSet">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
    </body>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
      return {
        user: {
          account: '',
          password: '',
          name:'',
          roleType: 0
        },
        //验证规则
        loginRules:{
          account:[
            {required:true, message:'请输入账号',trigger:"blur"},
            {min:11,max:11,message:'长度为11个数字', trigger:"blur"}
          ],
          password:[
            {required:true, message:'请输入密码', trigger:"blur"}
          ]
        },
      }
  },

  methods: {
    // 提交登录申请
      onSubmit() {
        var me=this;
        this. axios({
          url: '/user/login',
          method: 'post',
          data: {phone: this.user.account, password: this.user.password,roleType:this.user.roleType,alert:this.alert},
          transformRequest: [function (data) {
            var formData = new FormData();
            formData.append("phone", data.phone);
            formData.append("password", data.password);
            return formData;
          }]
        })
        .then(function (response) {
          console.log(response.data);
          if(response.data.status==200){
            me.roleType=JSON.stringify(response.data.data.role_type);
            window.sessionStorage.setItem('token', me.roleType)
            me.$router.push({
              path:'/offlineAnalysis',
              query: {
                roleType:me.roleType,
                day:1,
              }
            });
          }else{
            alert(response.data.msg, '失败', {
              confirmButtonText: '确定',
              callback: action => {
                this.$message({
                  type: 'info',
                  message: `action: ${ action }`
                });
              }
            });
          }
        })
        .catch(function (error) {
          console.log(error);
        });
      },
      // 重置文本框
      unSet(){
        this.$refs.loginFormRef.resetFields()
      }
  }
  
}
</script>

<style lang="less">
body 
{
	background-image:url('../assets/images/bg02.png');
	height: 100%; 
  width: 100%;
  position: fixed;
  background-size: cover;
  background-position: center;
}

.login_box{
  height: 600px;
  width: 1000px;
  background-color: #eee;
  border-radius: 20px;
  opacity: 80%;
  position: absolute;
  left: 50%;
  top:50%;
  transform: translate(-50%,-50%);
}

.title{
  position: relative;
  right: 3%;
}

.log_context{
  height: 350px;
  width: 700px;
  background-color: #eee;
  border-radius: 10px;
  position: absolute;
  left: 50%;
  top:50%;
  transform: translate(-50%,-40%);
  box-sizing: border-box;
}

.el_form{
  background-color: #eee;
  position:absolute;
  border-radius: 10px;
  left: 30px;
  bottom:40%;
  width:80%;
  margin: 20px, 20px;
}
.el_form_item{
  background-color: #eee;
  border-radius: 10px;
  width:100%;
}

.button{
  position: absolute;
  bottom: 50px;
  right:110px
}
.copyright{
  position: absolute;
  bottom: 30px;
  left:330px;
}
</style>