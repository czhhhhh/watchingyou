<template>
  <div class="MonitorPositioning">
    <el-container class="monitor_container">
    <!-- 头部 -->
    <el-header>
      <div>
        <el-dropdown placement="top-start">
          <span id="menu">
            菜单
            <i class="el-icon-more" style="color:#fff"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-menu background-color="#1d425d" text-color="#fff" active-text-color="#ffd04b" default-active="3"
        @open="handleOpen"
        @close="handleClose">
          <el-submenu index="1" width="100%">
            <template slot="title">
              <i class="el-icon-s-data"></i>
              <span>离线业务</span>
            </template>
            <el-menu-item-group class="submenu" id="d0">
              <el-menu-item class="item" id="d1" index="1-1" active @click="offlineSubmit_1">
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
          <el-menu-item index="3">
            <i class="el-icon-menu"></i>
            <span slot="title">监控指标</span>
          </el-menu-item>
          <el-menu-item index="4" v-show="roleType==1" @click="manageSubmit">
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
        <!-- ATM响应时间 -->
        <div class="atmMonitor">
          <div class="entry">
            <!-- 当前响应时间 -->
            <div class="atmEntry">
              <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:block; position:relative; top:2%; float:left">ATMEntry响应时间</div>
              <div style="clear:both">
                <div :class="[this.ATMEntryResponding.status==='normal'? 'normal':'warning']">{{ATMEntryResponding.value}}</div>
                <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:inline">  ms</div>
              </div>
            </div>
            <!-- 五分钟折线图 -->
            <div class="atmEntryChart">
              <div id="ATMEntry" :style="{width:'95%',height:'100%'}">
              </div>
            </div>
          </div>
          <div class="server" >
            <!-- 当前响应时间 -->
            <div class="atmServer">
              <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:block; position:relative; top:2%; float:left">
                ATMServer响应时间
              </div>
              <div style="clear:both">
                <div :class="[this.ATMServerResponding.status==='normal'? 'normal':'warning']">{{ATMServerResponding.value}}</div>
                <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:inline">  ms</div>
              </div>
            </div>
            <!-- 五分钟折线图 -->
            <div class="atmServerChart">
              <div id='ATMServer' :style="{width:'95%',height:'100%'}"></div>
            </div>
          </div>
          <div class="core">
            <!-- 当前响应时间 -->
            <div class="coreService">
              <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:block; position:relative; top:2%; float:left">
                CoreService响应时间
              </div>
              <div style="clear:both">
                <div :class="[this.CoreServiceResponding.status==='normal'? 'normal':'warning']">{{CoreServiceResponding.value}}</div>
                <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:inline">  ms</div>
              </div>
            </div>
            <!-- 五分钟折线图 -->
            <div class="coreServiceChart">
              <div id='CoreService' :style="{width:'95%',height:'100%'}"></div>
            </div>
          </div>
        </div>
        <!-- 外汇响应时间 -->
        <div class="fxMonitor">
          <div class="entry">
            <!-- 当前响应时间 -->
            <div class="fxEntry">
              <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:block; position:relative; top:2%; float:left">
                FXEntry响应时间
              </div>
              <div style="clear:both">
                <div :class="[this.FXEntryResponding.status==='normal'? 'normal':'warning']">{{FXEntryResponding.value}}</div>
                <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:inline">  ms</div>
              </div>
            </div>
            <!-- 五分钟折线图 -->
            <div class="fxEntryChart">
              <div id='FXEntry' :style="{width:'95%',height:'100%'}"></div>
            </div>
          </div>
          <div class="server">
            <!-- 当前响应时间 -->
            <div class="fxServer">
              <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:block; position:relative; top:2%; float:left">
                FXServer响应时间
              </div>
              <div style="clear:both">
                <div :class="[this.FXServerResponding.status==='normal'? 'normal':'warning']">{{FXServerResponding.value}}</div>
                <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:inline">  ms</div>
              </div>
            </div>
            <!-- 五分钟折线图 -->
            <div class="fxServerChart">
              <div id='FXServer' :style="{width:'95%',height:'100%'}"></div>
            </div>
          </div>
          <div class="core">
            <!-- 当前响应时间 -->
            <div class="fxCoreEntry">
              <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:block; position:relative; top:2%; float:left">
                FXCoreEntry响应时间
              </div>
              <div style="clear:both">
                <div :class="[this.FXCoreEntryResponding.status==='normal'? 'normal':'warning']">{{FXCoreEntryResponding.value}}</div>
                <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:inline">  ms</div>
              </div>
            </div>
            <!-- 五分钟折线图 -->
            <div class="fxCoreEntryChart">
              <div id='FXCoreEntry' :style="{width:'95%',height:'100%'}"></div>
            </div>
          </div>
        </div>
      </el-main>
      <div @click="display = true" class="monitorList">
      </div>
      <drawer title="告警信息" :display.sync="display"  :width="drawerWidth" :mask="true" style="color:#fff">
        <template>
          <div id="drawer">
          <div class="monitorTable">
            <el-table :data="monitorData"  style="width: 100%; background: rgba(0,0,0,0)" :header-cell-style="{background:'#296b88',color:'#fff'}">
              <el-table-column fixed prop="time"  label="时间"  width="180px" style="color:#fff,background: rgba(0,0,0,0)">
              </el-table-column>
              <el-table-column  prop="nodeName"  label="节点名称" width="90px" style="color:#fff,background: rgba(0,0,0,0)">
              </el-table-column>
                <el-table-column class="alert" prop="nodeDuration"  label="响应时间/ms" width="90px" style="color:#fff">
              </el-table-column>          
              <el-table-column width="100px">
                <template scope="scope">
                  <el-button @click="handle(scope.row)" type="warning" plain>处理</el-button>
                </template>
              </el-table-column>
              </el-table>
            <el-pagination  @current-change="handleCurrentChange"  :current-page="currentPage"
             :page-size="10"  layout="total, prev, pager, next, jumper"  :total="pageNum" :pager="{background:'#000'}">
            </el-pagination>
          </div>
          <div id="comparison" :style="{width:'50%',height:'750px'}"></div>
          </div>
        </template>
      </drawer>
    </el-container>
  </el-container>
  
  </div>
</template>

<script>
import drawer from '@/components/drawer.vue'
export default {
  name: 'MonitorPositioning',
  components: { drawer },
  data(){
    return{
      flag: 0,
      roleType:window.sessionStorage.getItem('token'),
      //监控表单抽屉默认关闭
      display: false,
      drawerWidth: '1000px',
      currentPage:1,
      pageNum:0,
      monitorData: [
        // {
          // id:0
        //   time: '14:27:37',
        //   nodeName:'ATMEntry',
        //   nodeDuration:'95'
        // },
      ],
      percentData:[
        // {
        //   nodeName:'',
        //   normal:50,
        //   warning:4,
        //   percent:8
        // }
      ],
      // 最近五秒的数据
      ATMEntryResponding: {value:0, status:'normal'},
      ATMServerResponding: {value:0, status:'normal'},
      CoreServiceResponding: {value:0, status:'normal'},
      FXEntryResponding: {value:0, status:'normal'},
      FXServerResponding: {value:0, status:'normal'},
      FXCoreEntryResponding: {value:0, status:'normal'},
      //最近五分钟的数据表单
      ATMEntryRespondingList:[
        //一共60个时间点，五分钟之内每隔5秒显示一个节点
        // {time:'', value:0},
      ],
      ATMEntryWarningList:[
        // {time:'', value: 0}
      ],
      ATMServerRespondingList:[
        //一共60个时间点，五分钟之内每隔5秒显示一个节点
        // {time:'', value:0},
      ],
      ATMServerWarningList:[
        // {time:'', value: 0}
      ],
      CoreServiceRespondingList:[
        //一共60个时间点，五分钟之内每隔5秒显示一个节点
        // {time:'', value:0},
      ],
      CoreServiceWarningList:[
        // {time:'', value: 0}
      ],
      FXEntryRespondingList:[
        //一共60个时间点，五分钟之内每隔5秒显示一个节点
        // {time:'', value:0},
      ],
      FXEntryWarningList:[
        // {time:'', value: 0}
      ],
      FXServerRespondingList:[
        //一共60个时间点，五分钟之内每隔5秒显示一个节点
        // {time:'', value:0},
      ],
      FXServerWarningList:[
        // {time:'', value: 0}
      ],
      FXCoreEntryRespondingList:[
        //一共60时间点，五分钟之内每隔5秒显示一个节点
        // {time:'', value:0},
      ],
      FXCoreEntryWarningList:[
        // {time:'', value: 0}
      ]
    }
  },
  watch:{
    display(val,oldVal){
      if(val){
        this.getPercent()
        this.createPageNum()
        this.handleCurrentChange(1)
        this.drawComparisonChart()
      }
    }
  },
  methods:{
    handleOpen() {
      
    },
    handleClose() {
      
    },
    // 退出登录
    exitPage(){
      window.sessionStorage.clear();
      this.$router.push('/login')
    },
    //页面跳转
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
    realtimeSubmit(){
      this.$router.push('/realTimeAnalysis')
    },
    manageSubmit(){
      this.$router.push('/userManage')
    },
    // 告警列表分页获取数据
    handleCurrentChange(val) {
      this.currentPage = val
      console.log(`当前页: ${val}`);
      var that = this
      try{
        this. axios({
          url: '/monitor/getWarningList',
          method: 'post',
          data: {p: val},
          transformRequest: [function (data) {
            var formData = new FormData();
            formData.append("p", data.p);
            return formData;
          }]
      }).then(res=>{
        if(res.data.status==200){
          console.log(res)
          let list = []
          let data = res.data.data.records
          data.forEach(element => {
            list.push({time:element.agg_time, id: element.id, nodeName: element.node_name,
            nodeDuration: element.node_duration})
          })
          that.monitorData = list
          console.log(that.monitorData)
        }else{
          console.log(res.data.msg)}  
      })
      }catch(err){
        console.log(err)
      }
    },
    createPageNum(){
      this.pageNum=this.monitorData.length
    },
    // 处理告警
    handle(row){
      try{
        var that = this
        this. axios({
          url: '/monitor/updateWarning',
          method: 'post',
          data: {id: row.id},
          transformRequest: [function (data) {
            var formData = new FormData();
            formData.append("id", data.id);
            return formData;
          }]
        }).then(res=>{
          if(res.data.status==200){
            alert(res.data.msg+ '处理成功')
            this.handleCurrentChange(that.currentPage)
          }else{
            alert(res.data.msg+ '处理失败')
          }
        })
      }catch(err){
        alert(err)
      }
    },
    //绘制图像
    drawCharts(){
      let that = this
      //ATMEntry
      let ATMEntry = this.$echarts.init(document.getElementById('ATMEntry'))
      this. axios({
        url: '/monitor/getATMEntryDuration',
        method: 'post',
        data: {nodeName: 'ATMEntry', num: 12},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("nodeName", data.nodeName);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data
          var dataList = []
          var warningList = []
          data.forEach(element => {
            dataList.push({time:element.agg_time, value: element.node_duration})
            if(data.status=="warning"){
              warningList.push({time:element.agg_time, value: element.node_duration})
            }
            that.ATMEntryRespondingList = dataList
            console.log(that.ATMEntryRespondingList)
            that.ATMEntryWarningList = warningList
          });
          ATMEntry.setOption({
            title: {
              text: 'ATMEntry响应时间',
              left: '1%',
              textStyle:{
              color:'#fff'
              }
            },
            tooltip: {
              trigger: 'axis'
            },
            grid: {
              left: '8%',
              right: '10%',
              bottom: '10%'
            },
            xAxis: {
              type:'category',
              data: that.ATMEntryRespondingList.map(function(item){return item.time}),
              nameTextStyle:{
                color:'#fff'
              },
              axisLine:{
                lineStyle:{
                color:'#fff'
              }}
            },
            yAxis: {
              scale: true,
              nameTextStyle:{
                color:'#fff'
              },
              axisLine:{
                lineStyle:{
                color:'#fff'
              }}
            },
            dataZoom: [{
            }, {
              type: 'inside'
            }],
            visualMap: {
              show:false,
              pieces: [{
                gt: 0,
                lte: 100,
                color: '#93CE07'
              }, {
                gt: 100,
                lte: 130,
                color: '#FBDB0F'
              }],
              outOfRange: {
                color: '#d73e44'
              }
            },
            series: [{
              name: 'ATMEntry响应时间',
              type: 'line',
              data: that.ATMEntryRespondingList,
              markLine: {
                silent: true,
                lineStyle: {
                  color: '#fff'
                },
                data: [{
                    yAxis: 100
                }, {
                    yAxis: 130
                }]
              }
            },{
              name:'告警',
              type:'scatter',
              symbol:'circle',
              symbolSize:10,
              itemStyle:{
                color:'red'
              },
              data:that.ATMEntryWarningList
            }
            ]
          })
        }else{
          console.log(res)}
      })
      
      //ATMServer
      let ATMServer = this.$echarts.init(document.getElementById('ATMServer'))
      this. axios({
        url: '/monitor/getATMServerDuration',
        method: 'post',
        data: {nodeName: 'ATMServer', num: 12},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("nodeName", data.nodeName);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data
          var dataList = []
          var warningList = []
          console.log("ATMServer"+data)
          data.forEach(element => {
            dataList.push({time:element.agg_time, value: element.node_duration})
            if(data.status=="warning"){
              warningList.push({time:element.agg_time, value: element.node_duration})
            }
          });
          that.ATMServerRespondingList = dataList
          that.ATMServerWarningList = warningList
          ATMServer.setOption({
            title: {
              text: 'ATMServer响应时间',
              left: '1%',
              textStyle:{
                color:'#fff'
              }
            },
            tooltip: {
              trigger: 'axis'
            },
            grid: {
              left: '8%',
              right: '10%',
              bottom: '10%'
            },
            xAxis: {
              type:'category',
              data: that.ATMServerRespondingList.map(function(item){return item.time}),
              nameTextStyle:{
                color:'#fff'
              },
              axisLine:{
                lineStyle:{
                  color:'#fff'
                }}
            },
            yAxis: {
              scale:true,
              nameTextStyle:{
                color:'#fff'
            },
            axisLine:{
              lineStyle:{
                color:'#fff'
              }}
            },
            dataZoom: [{
            }, {
              type: 'inside'
            }],
            visualMap: {
              show:false,
              top: 50,
              right: 10,
              pieces: [{
                gt: 0,
                lte: 40,
                color: '#93CE07'
              }, {
                gt: 40,
                lte: 70,
                color: '#FBDB0F'
              }],
              outOfRange: {
                color: '#d73e44'
              }
            },
            series: [{
              name: 'ATMServer响应时间',
              type: 'line',
              data:that.ATMServerRespondingList,
              markLine: {
                silent: true,
                lineStyle: {
                    color: '#fff'
                },
                data: [{
                    yAxis: 40
                }, {
                    yAxis: 70
                }]
              }
            },
            {
              name:'告警',
              type:'scatter',
              symbol:'circle',
              symbolSize:10,
              itemStyle:{
                color:'red'
              },
              data:that.ATMServerWarningList
            }
          ]
        })
        }else{
          console.log(res)}
      })
      //coreService
      let CoreService = this.$echarts.init(document.getElementById('CoreService'))
      this. axios({
        url: '/monitor/getCoreServiceDuration',
        method: 'post',
        data: {nodeName: 'CoreService', num: 12},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("nodeName", data.nodeName);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data
          console.log("CoreService"+data)
          var dataList = []
          var warningList = []
          data.forEach(element => {
            dataList.push({time:element.agg_time, value: element.node_duration})
            if(data.status=="warning"){
              warningList.push({time:element.agg_time, value: element.node_duration})
            }
          });
          that.CoreServiceRespondingList = dataList
          console.log(dataList)
          that.CoreServiceWarningList = warningList
          CoreService.setOption({
        title: {
            text: 'CoreService响应时间',
            left: '1%',
            textStyle:{
          color:'#fff'
        }
        },
        tooltip: {
            trigger: 'axis'
        },
        grid: {
            left: '8%',
            right: '10%',
            bottom: '10%'
        },
        xAxis: {
            data: that.CoreServiceRespondingList.map(function(item){return item.time}),
            nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        yAxis: {
          scale:true,
          nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        
        dataZoom: [{
            
        }, {
            type: 'inside'
        }],
        visualMap: {
          show:false,
            top: 50,
            right: 10,
            pieces: [{
                gt: 0,
                lte: 20,
                color: '#93CE07'
            }, {
                gt: 20,
                lte: 45,
                color: '#FBDB0F'
            }],
            outOfRange: {
                color: '#d73e44'
            }
        },
        series: [{
            name: 'CoreService响应时间',
            type: 'line',
            data:that.CoreServiceRespondingList,
            markLine: {
                silent: true,
                lineStyle: {
                    color: '#fff'
                },
                data: [{
                    yAxis: 20
                }, {
                    yAxis: 45
                }]
            }
        },
        {
          name:'告警',
          type:'scatter',
          symbol:'circle',
          symbolSize:10,
          itemStyle:{
            color:'red'
          },
          data:that.CoreServiceWarningList
        }
        ]
        })
        }else{
          console.log(res)}
      })
      
      // FXEntry
      let FXEntry = this.$echarts.init(document.getElementById('FXEntry'))
      this. axios({
        url: '/monitor/getFXEntryDuration',
        method: 'post',
        data: {nodeName: 'FXEntry', num: 12},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("nodeName", data.nodeName);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data
          console.log("FXEntry"+data)
          var dataList = []
          var warningList = []
          data.forEach(element => {
            dataList.push({time:element.agg_time, value: element.node_duration})
            if(data.status=="warning"){
              warningList.push({time:element.agg_time, value: element.node_duration})
            }
          });
          that.FXEntryRespondingList = dataList
          console.log(data)
          that.FXEntryWarningList = warningList
          console.log(that.FXEntryWarningList)
          FXEntry.setOption({
        title: {
            text: 'FXEntry响应时间',
            left: '1%',
            textStyle:{
          color:'#fff'
        }
        },
        tooltip: {
            trigger: 'axis'
        },
        grid: {
            left: '8%',
            right: '10%',
            bottom: '10%'
        },
        xAxis: {
          type:'category',
          data: that.FXEntryRespondingList.map(function(item){return item.time}),
          nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        yAxis: {
          scale:true,
          nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        dataZoom: [{
            
        }, {
            type: 'inside'
        }],
        visualMap: {
          show:false,
            pieces: [{
                gt: 0,
                lte: 100,
                color: '#93CE07'
            }, {
                gt: 100,
                lte: 200,
                color: '#FBDB0F'
            }, ],
            outOfRange: {
                color: '#d73e44'
            }
        },
        series: [{
            name: 'FXEntry响应时间',
            type: 'line',
            data:that.FXEntryRespondingList,
            markLine: {
                silent: true,
                lineStyle: {
                    color: '#fff'
                },
                data: [{
                    yAxis: 100
                }, {
                    yAxis: 200
                }]
            }
        },
        {
          name:'告警',
          type:'scatter',
          symbol:'circle',
          symbolSize:10,
          itemStyle:{
            color:'red'
          },
          data:that.FXEntryWarningList
        }
        ]
      })
        }else{
          console.log(res)}
      })
      
      // FXServer
      let FXServer = this.$echarts.init(document.getElementById('FXServer'))
      this. axios({
        url: '/monitor/getFXServerDuration',
        method: 'post',
        data: {nodeName: 'FXServer', num: 12},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("nodeName", data.nodeName);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data
          console.log("FXServer"+data)
          var dataList = []
          var warningList = []
          data.forEach(element => {
            dataList.push({time:element.agg_time, value: element.node_duration})
            if(data.status=="warning"){
              warningList.push({time:element.agg_time, value: element.node_duration})
            }
          });
          that.FXServerRespondingList = dataList
          that.FXServerWarningList = warningList
          console.log("FXServer"+warningList)
          FXServer.setOption({
            title: {
              text: 'FXServer响应时间',
              left: '1%',
              textStyle:{
                color:'#fff'
              }
            },
            tooltip: {
              trigger: 'axis'
            },
            grid: {
              left: '8%',
              right: '10%',
              bottom: '10%'
            },
            xAxis: {
              type:'category',
              data: that.FXServerRespondingList.map(function(item){return item.time}),
              nameTextStyle:{
                color:'#fff'
              },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        yAxis: {
          scale:true,
          nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        dataZoom: [{
            
        }, {
            type: 'inside'
        }],
        visualMap: {
          show:false,
            pieces: [{
                gt: 0,
                lte: 70,
                color: '#93CE07'
            }, {
                gt: 70,
                lte: 115,
                color: '#FBDB0F'
            }],
            outOfRange: {
                color: '#d73e44'
            }
        },
        series: [{
            name: 'FXServer响应时间',
            type: 'line',
            data: that.FXServerRespondingList,
            markLine: {
                silent: true,
                lineStyle: {
                    color: '#fff'
                },
                data: [{
                    yAxis: 70
                }, {
                    yAxis: 115
                }]
            }
        },
        {
          name:'告警',
          type:'scatter',
          symbol:'circle',
          symbolSize:10,
          itemStyle:{
            color:'red'
          },
          data:that.FXServerWarningList
        }
        ]
      })
        }else{
          console.log(res)}
      })
      
      // FXCoreEntry
      let FXCoreEntry = this.$echarts.init(document.getElementById('FXCoreEntry'))
      this. axios({
        url: '/monitor/getFXCoreEntryDuration',
        method: 'post',
        data: {nodeName: 'FXCoreEntry', num: 12},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("nodeName", data.nodeName);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data
          console.log("FXCoreService"+data)
          var dataList = []
          var warningList = []
          data.forEach(element => {
            dataList.push({time:element.agg_time, value: element.node_duration})
            if(data.status=="warning"){
              warningList.push({time:element.agg_time, value: element.node_duration})
            }
          });
          that.FXCoreEntryRespondingList = dataList
          that.FXCoreEntryWarningList = warningList
          FXCoreEntry.setOption({
        title: {
            text: 'FXCoreEntry响应时间',
            left: '1%',
            textStyle:{
          color:'#fff'
        }
        },
        tooltip: {
            trigger: 'axis'
        },
        grid: {
            left: '8%',
            right: '10%',
            bottom: '10%'
        },
        xAxis: {
          type:'category',
          data: that.FXCoreEntryRespondingList.map(function(item){return item.time}),
          nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        yAxis: {
          scale:true,
          nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        dataZoom: [{
            
        }, {
            type: 'inside'
        }],
        visualMap: {
          show:false,
            pieces: [{
                gt: 0,
                lte: 40,
                color: '#93CE07'
            }, {
                gt: 40,
                lte: 80,
                color: '#FBDB0F'
            }],
            outOfRange: {
                color: '#d73e44'
            }
        },
        series: [{
            name: 'FXCoreEntry响应时间',
            type: 'line',
            data: that.FXCoreEntryRespondingList,
            markLine: {
                silent: true,
                lineStyle: {
                    color: '#333'
                },
                data: [{
                    yAxis: 40
                }, {
                    yAxis: 80
                }
              ]
            }
        },
        {
          name:'告警',
          type:'scatter',
          symbol:'circle',
          symbolSize:10,
          itemStyle:{
            color:'red'
          },
          data:that.FXCoreEntryWarningList
        }]
      })
        }else{
          console.log(res)}
      })
      
      window.onresize = function(){
          ATMEntry.resize();
          ATMServer.resize();
          CoreService.resize();
          FXEntry.resize();
          FXServer.resize();
          FXCoreEntry.resize()
      }
    },
    //告警对比图
    drawComparisonChart(){
      var that = this
      let chart = this.$echarts.init(document.getElementById('comparison'))
      chart.setOption({
        title:{
          text:"告警信息百分比",
          textStyle:{
          color:'#fff'
        }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        
        grid: {
          left: '0%',
          right: '10%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
        {
          type: 'value',
          nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        }
        ],
        yAxis: [
        {
          type: 'category',
          nameTextStyle:{
          color:'#fff'
          },
          axisLine:{
          lineStyle:{
            color:'#fff'
          }},
          axisTick: {
            show: false
          },
          data: that.percentData.map(function(item){return item.nodeName}),
        }],
        series: [{
          name: '告警占比',
          type: 'bar',
          label: {
            show: false,
            position: 'inside'
          },
          itemStyle:{
              normal:{
                color: "#96dee8"
              }
            },
          emphasis: {
            focus: 'series'
          },
          markPoint: {
                data: [
                    {type: 'max', name: '最大值'},
                    {type: 'min', name: '最小值'}
                ],
                symbolRotate: -90,
            },
          data: that.percentData.map(function(item){return item.percent}),
        },
        {
          name: '告警',
          type: 'bar',
          stack: '总量',
          label: {
            show: true
          },
          emphasis: {
            focus: 'series'
          },
          itemStyle:{
              normal:{
                color: "#3fb1e3"
              }
            },
          data: that.percentData.map(function(item){return item.warning}),
        },
        {
          name: '正常',
          type: 'bar',
          stack: '总量',
          label: {
            show: true,
            
          },
          itemStyle:{
              normal:{
                color: "#6be6c1"
              }
          },
          emphasis: {
            focus: 'series'
          },
           
            
          data: that.percentData.map(function(item){return item.normal}),
        }]
      })
    },
    
    // 当前数据
    getVolumeData(){
      let that = this
      try{
      this. axios({
        url: '/monitor/getATMEntryDuration',
        method: 'post',
        data: {nodeName: "ATMEntry",num: 1},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("nodeName", data.nodeName);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data[0]
          console.log(data)
          that.ATMEntryResponding.value = data.node_duration
          that.ATMEntryResponding.status = data.status
        }else{
          console.log(res.data.msg)}
      })
      this. axios({
        url: '/monitor/getATMServerDuration',
        method: 'post',
        data: {nodeName: "ATMServer",num: 1},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("nodeName", data.nodeName);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data[0]
          console.log(data)
          that.ATMServerResponding.value = data.node_duration
          that.ATMServerResponding.status = data.status
        }else{
          console.log(res.data.msg)}
      })
      this. axios({
        url: '/monitor/getCoreServiceDuration',
        method: 'post',
        data: {nodeName: "CoreService",num: 1},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("nodeName", data.nodeName);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data[0]
          console.log(data)
          that.CoreServiceResponding.value = data.node_duration
          that.CoreServiceResponding.status = data.status
        }else{
          console.log(res.data.msg)}
      })
      this. axios({
        url: '/monitor/getFXEntryDuration',
        method: 'post',
        data: {nodeName: "FXEntry",num: 1},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("nodeName", data.nodeName);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data[0]
          console.log(data)
          that.FXEntryResponding.value = data.node_duration
          that.FXEntryResponding.status = data.status
        }else{
          console.log(res.data.msg)}
      })
      this. axios({
        url: '/monitor/getFXServerDuration',
        method: 'post',
        data: {nodeName: "FXServer",num: 1},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("nodeName", data.nodeName);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data[0]
          console.log(data)
          that.FXServerResponding.value = data.node_duration
          that.FXServerResponding.status = data.status
        }else{
          console.log(res.data.msg)}
      })
      this. axios({
        url: '/monitor/getFXCoreEntryDuration',
        method: 'post',
        data: {nodeName: "FXCoreEntry",num: 1},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("nodeName", data.nodeName);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data[0]
          console.log(data)
          that.FXCoreEntryResponding.value = data.node_duration
          that.FXCoreEntryResponding.status = data.status
        }else{
          console.log(res.data.msg)}
      })
      }catch(err){
        console.log(err)
      }
    },
    // 告警占比
    getPercent(){
      var that = this
      try{
      this. axios({
        url: 'monitor/getNodePercent',
        method: 'post'
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data
          console.log(data)
          that.percentData.splice(0, that.percentData.length)
          data.forEach(element=>{
            var p = 100*element.warning/(element.warning+element.normal+1)
            p = p.toFixed(2)
            that.percentData.push({nodeName:element.nodeName, normal:element.normal,
            warning: element.warning, percent: p})
          })
        }else{
          console.log(res.data.msg)}
      })
      }catch(err){
        console.log(err)
      }
    }
  },
  mounted(){
    let that = this
    this.drawCharts()
    this.flag = setInterval(function(){
      that.getVolumeData();
      that.drawCharts();
    }, 10000)
    
  },
  destroyed(){
    clearInterval(this.flag)
  }
}
</script>

<style lang="less">
.el-header{
  background-image:url('../assets/images/header-left.png');
  background-size:100% 100%;
  color:#fff;
  font-family: Microsoft Yahei;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.el-dropdown-menu{
  background:rgba(0, 0, 0, 0);
  width:200px;
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
  background-image:url('../assets/images/panel.png');
  background-size:100% 100%;
  width:200px;
}
.el-main{
  position: relative;
  padding: 0;
  margin:0;
  width:97%;
}
.monitorList{
  background-image:url('../assets/images/aside.png');
  background-size:100% 100%;
  position: relative;
  margin:0;
  width:3%;
  font-size: medium;
  margin-right: 0;
  padding-left: 4px;
}
.monitor_container{
  background-image:url('../assets/images/bg02.png');
  background-size: cover;
  background-position: center;
  height: 100%;
  width: 100%;
  position: absolute;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

.atmMonitor{
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  position: relative;
  width:100%;
  height: 50%;
}
.fxMonitor{
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  position: relative;
  width:100%;
  height: 50%;
}
.entry,.server,.core{
  background-image:url('../assets/images/panel.png');
  background-size:100% 100%;
  display: flex;
  flex-direction: column;
  position: relative;
  top: 10px;
  width: 32%;
  height: 93%;
  overflow: hidden;
}
.atmEntry,.coreService,.atmServer{
  background-image:url('../assets/images/panel02.png');
  background-size:100% 100%;
  position: relative;
  width:200px;
  height:25% ;
  box-sizing: border-box;
  margin:10px;
  padding: 10px;
  overflow: hidden;
}
.atmEntryChart,.atmServerChart,.coreServiceChart,.fxEntryChart,.fxServerChart,.fxCoreEntryChart{
  box-sizing: border-box;
  position: relative;
  width:100%;
  height: 90%;
  margin:15px;
  padding: 0;
}
.fxEntry,.fxCoreEntry,.fxServer{
  background-image:url('../assets/images/panel02.png');
  background-size:100% 100%;
  position: relative;
  width:200px;
  height:25% ;
  box-sizing: border-box;
  margin:10px;
  padding: 10px;
  overflow: hidden;
}
#drawer{
  position: relative;
  width:100%;
  height: 100%;
  display: flex;
  flex-direction: row !important;
  justify-content: space-evenly;
}
.monitorTable{
  border-right:1px solid #eee;
  position: relative;
  width:50%;
  margin:4px;
}
.normal{
  font-size:30px; 
  font-family:'微软雅黑'; 
  color:yellow; 
  display:inline
}
.warning{
  font-size:30px; 
  font-family:'微软雅黑'; 
  color:#d73e44; 
  display:inline
}
#comparison{
  position: relative;
  width:50%;
  margin:4px;
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