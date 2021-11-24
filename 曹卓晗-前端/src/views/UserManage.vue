<template>
  <div class="UserManage">
    <el-container class="user_container">
    <!-- 头部 -->
    <el-header>
      <div>
        <el-dropdown placement="top-start">
          <span id="menu">
            菜单
            <i class="el-icon-more" style="color:#fff"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-menu background-color="#1d425d" text-color="#fff" active-text-color="#ffd04b" default-active="4"
        @open="handleOpen"
        @close="handleClose">
          <el-submenu index="1" width="100%">
            <template slot="title">
              <i class="el-icon-s-data"></i>
              <span>离线业务</span>
            </template>
            <el-menu-item-group class="submenu" id="d0">
              <el-menu-item class="item" id="d1" index="1-1" @click="offlineSubmit_1">
                <i class="el-icon-star-on"></i>
                <span slot="title">昨天</span>
              </el-menu-item>
              <el-menu-item class="item" index="1-2" @click="offlineSubmit_2">
                <i class="el-icon-star-on"></i>
                <span slot="title">前天</span>
              </el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-menu-item index="2" @click="realtimeSubmit">
            <i class="el-icon-s-data"></i>
            <span slot="title">实时业务</span>
          </el-menu-item>
          <el-menu-item index="3" @click="monitorSubmit">
            <i class="el-icon-menu"></i>
            <span slot="title">监控指标</span>
          </el-menu-item>
          <el-menu-item index="4">
            <i class="el-icon-user-solid"></i>
            <span slot="title">用户管理</span>
          </el-menu-item>
        </el-menu>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div style="font-size:27px">Watching You</div>
      <div id="exit" @click="exitPage">
        退出
      </div>
    </el-header>
    <el-container>
      
      <!-- 具体内容 -->
      <el-main>
        <!-- 创建用户 -->
        <!-- 弹窗 -->
        <div v-show="popup">
          <div class="create">
            <div class="popHeader">
            <span>创建用户</span>
            <span class="close-btn" @click="closeByButton">X</span>
          </div>
            <el-form ref="createUserRef" :model="newUser" :rules="createRules" label-width="80px" class="createUser">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="newUser.name"></el-input>
              </el-form-item>
              <el-form-item label="电话" prop="phone">
                <el-input v-model="newUser.phone"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="createUser">立即创建</el-button>
                <el-button type="primary" @click="unSet">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
          <!-- 透明遮罩 -->
          <div class="cover"></div>
        </div>
        
        <div id="title">
          用户表单
          <el-button type='primary' @click="showPop">创建用户</el-button>
        </div>

            <!-- 用户表单 -->
        <el-table :data="tableData" border class="userTable" :header-cell-style="{background:'#296b88',color:'#fff'}">
              <el-table-column fixed prop="phone_num" label="账号"  width="200">
              </el-table-column>
              <el-table-column  prop="user_name"  label="姓名" width="150">
              </el-table-column>
              <el-table-column  prop="role_type"  label="是否为管理员"  width="150">
              </el-table-column>
              <el-table-column  prop="password"  label="密码"  width="150">
              </el-table-column>
              <el-table-column fixed="right"  label="操作"  width="100">
                <template slot-scope="scope">
                  <!-- 删除用户出发按键 -->
                  <el-button @click="deleteUser(scope.$index,scope.row)" type="text" size="medium">删除</el-button>
                </template>
              </el-table-column>
        </el-table>
        <div id="block">
          <el-pagination  @current-change="handleCurrentChange"  :current-page="currentPage"
          :page-size="5"  layout="total, prev, pager, next, jumper"  :total="total">
          </el-pagination>
        </div>
          
      </el-main>
    </el-container>
  </el-container>
  </div>
</template>

<script>

export default {
  name: 'UserManage',
  data() {
      return{
        popup: 0,
        newUser:{
          name:'',
          phone:'',
          password:'123456',
          isManager:false
        },
        activeNames: ['2'],
        createRules:{
          name:[
            {required:true, message:'请输入姓名',trigger:"blur"}
          ],
          phone:[
            {required:true, message:'请输入电话', trigger:"blur"},
            {min:11, max:11, message:'长度为11个数字', trigger:"blur"}
          ]
        },
        tableData: [],
        total:0,
        pageSize:5,
        currentPage:1,
      }
  },
  methods:{
    handleOpen() {
      
    },
    handleClose() {
      
    },
    // 弹窗
    showPop(){
      this.popup = 1
    },
    closeByButton(){
      this.popup = 0
    },
    // 分页
    handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
    },
    createPageNum(){
      this.pageNum=this.tableData.length
    },
    // 退出登录
    exitPage(){
      window.sessionStorage.clear();
      this.$router.push('/login')
    },
    // 页面跳转
    offlineSubmit_1(){
      this.$router.push({
        path:'/offlineAnalysis',
        query: {day:1}
      })
    },
    offlineSubmit_2(){
      this.$router.push({
        path:'/offlineAnalysis',
        query: {day:2}
      })
    },
    monitorSubmit(){
      this.$router.push('/monitorPositioning')
    },
    realtimeSubmit(){
      this.$router.push('/realTimeAnalysis')
    },
    // 功能函数
    handleCurrentChange: function(currentPage) {
				this.currentPage = currentPage;
				console.log(this.currentPage); //点击第几页
				this.showList(this.currentPage);

			},
      
    showList(currentPage){
      var me=this;
        this. axios({
          url: '/user/getList',
          method: 'post',
          data: {p:currentPage},
          transformRequest: [function (data) {
            var formData = new FormData();
            formData.append("p", data.p);
            return formData;
                    }]
          })
        .then(function (response) {
          console.log(response.data);
          if(response.data.status==200){
            me.total=parseInt(response.data.data.pages);
            me.tableData=response.data.data.records;
            for(let i=0;i<me.tableData.length;i++){
              me.tableData[i].role_type=me.tableData[i].role_type==1?"是":"否";
            }
            console.log(me.tableData);
            console.log(me.total);
            // window.sessionStorage.setItem('token', '001')
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
    // 创建用户
    createUser(){
      var me=this;
      this. axios({
        url: '/user/createAccount',
        method: 'post',
        data: {username:me.newUser.name,phone:me.newUser.phone},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("username", data.username);
          formData.append("phone", data.phone);
          return formData;
        }]
      }).then(function (response) {
        if(response.data.status==200){
          me.popup = 0;
          alert(response.data.msg);
          me.showList(me.currentPage);
        }else{
          alert(response.data.msg);
        }
      }).catch(function (error) {
        console.log(error);
      });
    },
    unSet(){
      this.$refs.createUserRef.resetFields()
    },
    // 删除用户
    deleteUser(index, row){
      this.$confirm('确定删除该用户数据?','提示',{
			  type:'warning'
		  }).then(()=>{
        var me=this;
        this. axios({
          url: '/user/deleteAccount',
          method: 'post',
          data: {phone:row.phone_num},
          transformRequest: [function (data) {
            var formData = new FormData();
            formData.append("phone", data.phone);
            return formData;
          }]
        }).then(function (response) {
          if(response.data.status!=-1){
            me.tableData.splice(index,1);
            console.log(me.tableData);
          }else{
            alert(response.data.msg);
          }
        }).catch(function (error) {
          console.log(error);
        });
      })
    }
  },
  mounted() {
      console.log("进入初始化");
			this.showList(this.currentPage);
	},
}
</script>

<style lang="less" scoped>
.el-header{
  background-image:url('../assets/images/header-left.png');
  background-size:100% 100%;
  color:#fff;
  font-family: Microsoft Yahei;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-menu{
  width: 100%;
  box-sizing: border-box;
}
.submenu.el-menu-item-group{
  position: relative;
  width:100%;
  overflow: hidden;
}
.el-dropdown-menu{
  background:rgba(0, 0, 0, 0);
  width:200px;
}
.el-main{
  padding: 0;
  margin:0;
  width:100%;
}

.user_container{
  background-image:url('../assets/images/bg02.png');
  background-size: cover;
  background-position: center;
  height: 100%;
  width: 100%;
  position: absolute;
  margin: 0;
  padding: 0
}
.userTable{
  background:rgba(0, 0, 0, 0);
  position:relative;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  width: 50%;
  height: 60%;
  left: 50%;
  top:50%;
  transform: translate(-50%,-60%);
  border-radius: 6px;
}
.createUser{
  position: relative;
  width:90%;
  top: 50px;
  height: 100%;
  z-index: 1000;
}

#block{
  margin-top: 40px
}
.create{
  background-image:url('../assets/images/panel.png');
  background-size:100% 100%;
  position: fixed;
  font-size: 24px;
  height: 300px;
  width: 400px;
  background-color: #fff;
  border-radius: 6px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
}
.cover{
  position: fixed;
  width: 100%;
  height: 100%;
  opacity: 0.8;//透明度为80%
  top: 0;
  left: 0;
  z-index: 999;
  background-color: #111111;
}
.popHeader {
  display: flex;
  justify-content: space-between;
  height: 45px;
  line-height: 45px;
  padding: 0 15px;
  font-size: 14px;
  font-weight: bold;
  border-bottom: 1px solid #eee;
  .close-btn {
  display: inline-block;
  cursor: pointer;
  height: 100%;
  padding-left: 20px;
  }
}
#block{
  position: absolute;
  left:50%;
  top:80%;
  transform: translate(-50%,0);
}
#title{
  color: #eee;
  font-family:'微软雅黑'; 
  font-size: large;
  position: relative;
  width: 50%;
  left: 50%;
  top:10%;
  transform: translate(-50%,0);
  display: flex;
  justify-content: space-between;
}
#exit{
  background-image:url('../assets/images/panel.png');
  background-size:100% 100%;
  position: relative;
  width:60px;
  height: 30px;
  top:30%;
  color:#fff;
  font-size: 15px;
}
#exit:hover{
  cursor: pointer;
  background-color: #35b1ad;
  border-radius: 4px;
}
#menu{
  color:#fff;
  font-size: 15px;
}
#menu:hover{
  cursor: pointer;
}
</style>