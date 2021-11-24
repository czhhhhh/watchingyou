<template>
  <el-container class="offline_container">
    <!-- 头部 -->
    <el-header>
      <div>
        <el-dropdown placement="top-start">
          <span id="menu">
            菜单
            <i class="el-icon-more" style="color:#fff"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-menu background-color="#1d425d" text-color="#fff" active-text-color="#ffd04b" default-active="$route.path"
            @open="handleOpen"
            @close="handleClose"
            ref = "elmenu">
              <el-submenu index="1" width="100%">
                <template slot="title">
                  <i class="el-icon-s-data"></i>
                  <span>离线业务</span>
                </template>
              <el-menu-item-group class="submenu" id="d0">
              <el-menu-item class="item" id="d1" index="/offlineAnalysis?day=1" active @click="offlineSubmit_1">
                <i class="el-icon-star-on"></i>
                <span slot="title">昨天</span>
              </el-menu-item>
              <el-menu-item class="item" index="/offlineAnalysis?day=2" @click="offlineSubmit_2">
                <i class="el-icon-star-on"></i>
                <span slot="title">前天</span>
              </el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-menu-item index="/realTimeAnalysis" @click="realTimeSubmit">
            <i class="el-icon-s-data"></i>
            <span slot="title">实时业务</span>
          </el-menu-item>
          <el-menu-item index="/monitorPositioning" @click="monitorSubmit">
            <i class="el-icon-menu"></i>
            <span slot="title">监控指标</span>
          </el-menu-item>
          <el-menu-item index="/userManage" v-show="roleType==1" @click="manageSubmit">
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
    <el-container class="beneth">

      <!-- 具体内容 -->
      <el-main>
        <div class="up">
          <!-- 离线业务地区分布图 -->
          <div class="areaChart">
            <div ref="offlineArea" id="offlineArea"></div>
          </div>
          <!-- 交易总量 -->
          <div class="totalAmount">
            <div class="rightDown">
              <!-- 条形图 -->
              <div id="atmChart" :style="{width:'100%',height:'48%'}"></div>
              <div id="fxChart" :style="{width:'100%',height:'48%'}"></div>
            </div>
          </div>
        </div>
        <div class="down">
          <!-- 环形图 -->
          <!-- 外汇买入目的环形图 -->
          <div ref="FXPurpose" id="FXPurpose">
          </div>
          <!-- 外汇买入年龄段环形图 -->
          <div id="FXAge" >
          </div>
          <!-- 外汇买入币种环形图 -->
          <div id="FXCurrency">
          </div>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'OfflineAnalysis',
  data(){
    return{
      // 角色
      roleType:window.sessionStorage.getItem('token'),
      // 地区分布      
      ATM:[
        {name: '北京市',value: [116, 39.9, 0] },
        {name: '天津市',value: [117.5, 39, 0] },
        {name: '上海市',value: [121.5, 30.9, 0]},
        {name: '重庆市',value: [106.5333, 29.5333, 0]},
        {name: '河北省',value: [115, 38.0333, 0] },
        {name: '河南省',value: [113.7, 34.8, 0] },
        {name: '云南省',value: [100.6833, 24, 0] },
        {name: '辽宁省',value: [123.4, 40.816, 0] },
        {name: '黑龙江省',value: [126.6833, 45.75, 0] },
        {name: '湖南省',value: [111, 27.183, 0] },
        {name: '安徽省',value: [116.7, 31.85, 0] },
        {name: '山东省',value: [117, 36.633, 0] },
        {name: '新疆省',value: [87.6, 43.8, 0] },
        {name: '江苏省',value: [119.5, 32, 0] },
        {name: '浙江省',value: [119.5, 30, 0] },
        {name: '江西省',value: [115.8667, 28.6833, 0] },
        {name: '湖北省',value: [110.35, 30.617, 0] },
        {name: '广西省',value: [108.333, 24, 0] },
        {name: '甘肃省',value: [103.8167, 35.5, 0] },
        {name: '山西省',value: [111.5667, 37.867, 0] },
        {name: '内蒙古',value: [111.8, 42.8167, 0] },
        {name: '陕西省',value: [107.9, 34.267, 0] },
        {name: '吉林省',value: [125.316, 43, 0] },
        {name: '福建省',value: [117.6, 25.5, 0] },
        {name: '贵州省',value: [106.7, 26.583, 0] },
        {name: '广东省',value: [113.25, 23.133, 0] },
        {name: '青海省',value: [98.75, 36.633, 0] },
        {name: '西藏',value: [91.167, 29.667, 0] },
        {name: '四川省',value: [100.0833, 29.65, 0] },
        {name: '宁夏',value: [106, 37.5, 0] },
        {name: '海南省',value: [109.7, 19.5, 0] },
        {name: '台湾',value: [121.5167, 24.6, 0] },
        {name: '香港',value: [114.1667, 22.3, 0] },
        {name: '澳门',value: [113.5, 22.222, 0] }
      ],
      FX:[
        {name: '北京市',value: [116.22, 40.9, 0] },
        {name: '天津市',value: [117.7, 40, 0] },
        {name: '上海市',value: [121.7, 31.9, 0]},
        {name: '重庆市',value: [108.5333, 30.5333, 0]},
        {name: '河北省',value: [116.4667, 39.0333, 0] },
        {name: '河南省',value: [114.8, 35.8, 0] },
        {name: '云南省',value: [102.6833, 25, 0] },
        {name: '辽宁省',value: [125.4, 41.816, 0] },
        {name: '黑龙江省',value: [128.6833, 46.75, 0] },
        {name: '湖南省',value: [113, 28.183, 0] },
        {name: '安徽省',value: [117.8, 32.85, 0] },
        {name: '山东省',value: [119, 37.633, 0] },
        {name: '新疆省',value: [89.6, 44.8, 0] },
        {name: '江苏省',value: [121, 33, 0] },
        {name: '浙江省',value: [120.7, 31, 0] },
        {name: '江西省',value: [117.8667, 29.6833, 0] },
        {name: '湖北省',value: [112.35, 31.617, 0] },
        {name: '广西省',value: [110.333, 25, 0] },
        {name: '甘肃省',value: [105, 36.5, 0] },
        {name: '山西省',value: [113.5667, 38.867, 0] },
        {name: '内蒙古',value: [113.8, 43.8167, 0] },
        {name: '陕西省',value: [109.9, 35.267, 0] },
        {name: '吉林省',value: [127.316, 44, 0] },
        {name: '福建省',value: [118.6, 26.5, 0] },
        {name: '贵州省',value: [108.7, 27.583, 0] },
        {name: '广东省',value: [115.25, 24.133, 0] },
        {name: '青海省',value: [100.75, 37.633, 0] },
        {name: '西藏',value: [93.167, 30.667, 0] },
        {name: '四川省',value: [102.0833, 30.65, 0] },
        {name: '宁夏',value: [107, 38.5, 0] },
        {name: '海南省',value: [110.6, 20.5, 0] },
        {name: '台湾',value: [122.3, 25.6, 0] },
        {name: '香港',value: [114.3, 23.3, 0] },
        {name: '澳门',value: [113.6, 23.222, 0] }
      ],
      // 选择日期
      day: this.$route.query.day,
      // 金额分布
      ATMRange:
      [{name:'小于100元', value:0},
      {name:'101-500元',value:0},
      {name:'501-1000元',value:0},
      {name:'1001-10000元',value:0},
      {name:'10000元以上',value:0}],
      FXRange:
      [{name:'小于100元', value:0},
      {name:'101-500元',value:0},
      {name:'501-1000元',value:0},
      {name:'1001-10000元',value:0},
      {name:'10000元以上',value:0}],
      // 外汇目的分布
      purposeData:[
        {name:'旅游',value:0},
        {name:'就医',value:0},
        {name:'留学',value:0},
        {name:'教育',value:0},
        {name:'其他',value:0},
      ],
      // 外汇年龄分布
      ageData:[
        {name:'18-25',value:0},
        {name:'26-35',value:0},
        {name:'36-45',value:0},
        {name:'46-60',value:0},
        {name:'61-80',value:0},
        {name:'81以上',value:0},
      ],
      // 外汇币种分布
      currencyData:[
        {name:'美元',value:0},
        {name:'英镑',value:0},
        {name:'日元',value:0},
        {name:'欧元',value:0},
        {name:'加拿大元',value:0},
        {name:'港币',value:0},
      ]
    }
  },
  watch:{
    day(val, oldVal){
      this.getAreaData();
      this.getAmountRange();
      this.getFXPurpose();
      this.getFXAge();
      this.getFXCurrency();
      this.drawCharts();
    },
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
    // 页面跳转
    manageSubmit(){
      this.$router.push('/userManage');
    },
    monitorSubmit(){
      this.$router.push('/monitorPositioning')
    },
    realTimeSubmit(){
      this.$router.push('/realTimeAnalysis')
    },
    offlineSubmit_1(){
      this.day = 1
    },
    offlineSubmit_2(){
      this.day = 2
    },
    // 绘制图像
    drawCharts(){
      let that = this
      window.onresize=function () {
        FXPurpose.resize()
        FXAge.resize()
        FXCurrency.resize()
        FXAmount.resize()
        if(!that.atmRangeErr){
          ATMAmount.resize()
        }
        offlineArea.resize()
      }
      
      // 外汇目的
      let FXPurpose = this.$echarts.init(document.getElementById('FXPurpose'))
      FXPurpose.showLoading()
      this. axios({
        url: '/offline/getPurposeVolume',
        method: 'post',
        data: {day: that.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          that.fxPurposeErr = 0
          let data = res.data.data
          console.log(data)
          that.purposeData.forEach(ele=>{ele.value = 0})
          data.forEach(element => {
            switch(element.agg_attr){
              case '旅游':
                that.purposeData[0].value = element.volume;
                break;
              case '就医':
                that.purposeData[1].value = element.volume;
                break;
              case '留学':
                that.purposeData[2].value = element.volume;
                break;
              case '教育':
                that.purposeData[3].value = element.volume;
                break;
              case '其他':
                that.purposeData[4].value = element.volume;
                break;
            }
          });
          FXPurpose.hideLoading()
          FXPurpose.setOption({
        title: {
        top:'4%',
        left:'2%',
        text: '外汇买入目的分布',
        textStyle:{
          color:'#fff'
        }
        },
        tooltip: {
        trigger: 'item',
        formatter:'{a} </br>{b}:{c} ({d}%)'
        },
        legend: {
        top: '10%',
        right: '5%',
        orient: 'vertical',
        itemGap: 10,
        textStyle:{
          color:'#fff'
        }
        },
        series: [
        {
            name: '外汇买入目的',
            type: 'pie',
            avoidLabelOverlap:true,
            stillShowZeroSum:true,
            radius: ['40%', '60%'],
            center: ['50%', '50%'],
            itemStyle: {
                borderWidth: 0,
                color: function(params){
                var color = ['#d95850','#eb8146','#ffb248','#f2d643','#ebdba4']
                 return color[params.dataIndex]
                }
            },
            label: {
                show: false,
                position: 'center'
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: '20',
                    fontWeight: 'bold',
                    textStyle:{color:'#fff'}
                }
            },
            labelLine: {
                show: false
            },
            data: that.purposeData
         }]
      })
        }else{
          console.log(res.data.msg, '外汇目的分布失败', )}
      })
      // 外汇年龄
      let FXAge = this.$echarts.init(document.getElementById('FXAge'))
      FXAge.showLoading()
      this. axios({
        url: '/offline/getAgeRangeVolume',
        method: 'post',
        data: {day: that.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          that.fxAgeErr = 0
          that.ageData.forEach(ele=>{ele.value = 0})
          let data = res.data.data
          console.log(data)
          data.forEach(element => {
            switch(element.agg_attr){
              case '18-25':
                that.ageData[0].value = element.volume;
                break;
              case '26-35':
                that.ageData[1].value = element.volume;
                break;
              case '36-45':
                that.ageData[2].value = element.volume;
                break;
              case '46-60':
                that.ageData[3].value = element.volume;
                break;
              case '61-80':
                that.ageData[4].value = element.volume;
                break;
              case '81':
                that.ageData[5].value = element.volume;
                break;
            }
          });
          FXAge.hideLoading()
          FXAge.setOption({
        title: {
          top:'4%',
          left:'2%',
          text: '外汇买入年龄段分布',
          textStyle:{
          color:'#fff'
        }
        },
        tooltip: {
          trigger: 'item',
          formatter:'{a} </br>{b}:{c} ({d}%)'
        },
        legend: { 
          top: '10%',
          right: '5%',
          orient: 'vertical',
          itemGap: 10,
          textStyle:{
          color:'#fff'
        }
        },
        series: [
        {
            name: '外汇买入年龄',
            type: 'pie',
            avoidLabelOverlap:true,
            stillShowZeroSum:true,
            radius: ['40%', '60%'],
            center: ['50%', '50%'],
            itemStyle: {
                borderWidth: 0,
                color: function(params){
                var color = ['#d95850','#eb8146','#ffb248','#f2d643','#ebdba4','#893448']
                 return color[params.dataIndex]
                }
            },
            label: {
                show: false,
                position:"center"
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: '20',
                    fontWeight: 'bold',
                    textStyle:{color:'#fff'}
                }
            },
            labelLine: {
                show: false
            },
            data: that.ageData
         }]
      })
        }else{
          console.log(res.data.msg, '外汇年龄失败', 
          )}
      })
      
      // 外汇币种
      let FXCurrency = this.$echarts.init(document.getElementById('FXCurrency'))
      FXCurrency.showLoading()
      this. axios({
        url: '/offline/getCurrencyVolume',
        method: 'post',
        data: { day: this.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          that.fxCurrencyErr = 0
          that.currencyData.forEach(ele=>{ele.value = 0})
          let data = res.data.data
          console.log(data)
          data.forEach(element => {
            switch(element.agg_attr){
              case '美元':
                that.currencyData[0].value = element.volume;
                break;
              case '英镑':
                that.currencyData[1].value = element.volume;
                break;
              case '日元':
                that.currencyData[2].value = element.volume;
                break;
              case '欧元':
                that.currencyData[3].value = element.volume;
                break;
              case '加拿大元':
                that.currencyData[4].value = element.volume;
                break;
              case '港币':
                that.currencyData[5].value = element.volume;
                break;
            }
          });
          FXCurrency.hideLoading()
          FXCurrency.setOption({
        title: {
        top:'4%',
        left:'2%',
        text: '外汇买入币种分布',
        textStyle:{
          color:'#fff'
        }
        },
        tooltip: {
          trigger: 'item',
          formatter:'{a} </br>{b}:{c} ({d}%)'
        },
        legend: { 
          top: '10%',
          right: '5%',
          orient: 'vertical',
          itemGap: 10,
          textStyle:{
          color:'#fff'
        }
        },
        toolbox: {
          show: false,
        },
        series: [
         {
            name: '外汇买入币种',
            type: 'pie',
            avoidLabelOverlap:true,
            stillShowZeroSum:true,
            radius: ['40%', '60%'],
            center: ['50%', '50%'],
            itemStyle: {
                borderWidth: 0,
                color: function(params){
                  var color = ['#d95850','#eb8146','#ffb248','#f2d643','#ebdba4','#893448']
                 return color[params.dataIndex]
                }
            },
            label:{
              show: false,
              position:"center"
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: '20',
                    fontWeight: 'bold',
                    textStyle:{color:'#fff'}
                }
            },
            labelLine:{
              show: false
            },
            data: that.currencyData
          }
        ]
      })
        }else{
          console.log(res.data.msg, '外汇币种失败', 
          )}
      })
      // 外汇金额分布
      let FXAmount = this.$echarts.init(document.getElementById('fxChart'))
      FXAmount.showLoading()
      this. axios({
        url: '/offline/getAmountRangeVolume',
        method: 'post',
        data: {trantype: 'FX001', day: that.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data
          console.log('FX')
          console.log(data)
          that.FXRange.forEach(ele=>{ele.value = 0})
          data.forEach(element => {
            switch(element.agg_attr){
              case '0-100':
                that.FXRange[0].value = element.count;
                break;
              case '101-500':
                that.FXRange[1].value = element.count;
                break;
              case '501-1000':
                that.FXRange[2].value = element.count;
                break;
              case '1001-10000':
                that.FXRange[3].value = element.count;
                break;
              case '10001':
                that.FXRange[4].value = element.count;
                break;
            }
          });
          FXAmount.hideLoading()
          FXAmount.setOption({
        title: {
        text: '外汇买入金额分布/笔数',
        left:'2%',
        textStyle:{
          fontSize: 16,
          color:'#fff'
        }
        },
        tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow',
          },
          formatter:function(params){
            if(that.day==1){
              // 昨天 小于100元：1000笔
              return "昨天外汇交易笔数"+'</br>'+params[0].axisValue +':'+params[0].value+'笔'
            }else{
              return "前天外汇交易笔数"+"</br>"+params[0].axisValue +':'+params[0].value+'笔'
            }
          }
        },
        grid: {
        top: '20%',
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
        },
        xAxis: {
        type: 'value',
        boundaryGap: [0, 0.01],
        nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        yAxis: {
        type: 'category',
        data: ['小于100元', '101-500元', '501-1000元', '1001-10000元', '10001元以上'],
        nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        series: [
         {
            type: 'bar',
            itemStyle:{
              normal:{
                color:function(params){
                  var colorList = [
                    '#f0932b','#ffa502','#f9ca24','#F3D64E','#f6e58d'
                  ];
                  return colorList[params.dataIndex]
                },
              }
            },
            data: that.FXRange
          }
        ] 
      })
        }else{
          console.log(res.data.msg)}
      })
     
      // ATM金额分布
      let ATMAmount = this.$echarts.init(document.getElementById('atmChart'))
      ATMAmount.showLoading()
      this. axios({
        url: '/offline/getAmountRangeVolume',
        method: 'post',
        data: {trantype: 'ATM001', day: that.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          that.atmRangeErr = 0
          let data = res.data.data
          console.log('ATM')
          console.log(data)
          that.ATMRange.forEach(ele=>{ele.value = 0})
          data.forEach(element => {
            switch(element.agg_attr){
              case '0-100':
                that.ATMRange[0].value = element.count;
                break;
              case '101-500':
                that.ATMRange[1].value = element.count;
                break;
              case '501-1000':
                that.ATMRange[2].value = element.count;
                break;
              case '1001-10000':
                that.ATMRange[3].value = element.count;
                break;
              case '10001':
                that.ATMRange[4].value = element.count;
                break;
            }
          });
          ATMAmount.hideLoading()
          ATMAmount.setOption({
        title: {
        text: 'ATM交易金额分布/笔数',
        left:'2%',
        textStyle:{
          fontSize: 16,
          color:'#fff'
        }
        },
        tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
          },
          formatter:function(params){
            if(that.day==1){
              // 昨天 小于100元：1000笔
              return "昨天ATM交易笔数"+'</br>'+params[0].axisValue +':'+params[0].value+'笔'
            }else{
              return "前天ATM交易笔数"+"</br>"+params[0].axisValue +':'+params[0].value+'笔'
            }
            
          }
        },
        
        grid: {
        top: '20%',
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
        },
        xAxis: {
        type: 'value',
        boundaryGap: [0, 0.01],
        nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        yAxis: {
        type: 'category',
        data: ['小于100元', '101-500元', '501-1000元', '1001-10000元', '10000元以上'],
        nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        series: [
         {
            type: 'bar',
            itemStyle:{
              normal:{
                color:function(params){
                  var colorList = [
                    '#dbfbb0','#c4e893','#b4db7f','#a3cc6b','#94c059'
                  ];
                  return colorList[params.dataIndex]
                },
              }
            },
            data: that.ATMRange
          }
        ] 

      })
        }else{
          console.log(res.data.msg, 'ATM金额分布失败', 
          )}
      })
      
      // 地图
      let offlineArea = this.$echarts.init(document.getElementById('offlineArea'))
      offlineArea.showLoading()
      this. axios({
        url: '/offline/getLocationVolume',
        method: 'post',
        data: {trantype: 'ATM001', day: this.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
        console.log(res)
        let la = that.ATM.length
        var result = res.data.data
        result.forEach(element=>{
          for(let j=0;j<la;j++){
            if(that.ATM[j].name==element.agg_attr){
              that.ATM[j].value[2]=element.volume
              break;
            }
          }
        })
        that. axios({
        url: '/offline/getLocationVolume',
        method: 'post',
        data: {trantype: 'FX001', day: that.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
        console.log(res)
        let data = res.data.data
        let la = that.FX.length
        data.forEach(element=>{
          for(let j=0;j<la;j++){
            if(that.FX[j].name==element.agg_attr){
              that.FX[j].value[2]=element.volume
              break;
            }
          }
        })
        offlineArea.hideLoading()
        var optionMap = {  
          tooltip: {
            trigger:'item',
            formatter: function(params){
                  console.log(params)
                  return params.seriesName+"</br>"+params.name+": "+params.value[2]},
          },
          title: {
            text: '交易地区分布',
            left:'1%',
            textStyle:{
              color:'#fff'
            }
          }, 
          geo: {
            zlevel:0,
            show: true,
            map: 'china',
            itemStyle: {
              normal: {
              areaColor: '#133149',
              borderColor: '#8cfffb'
              },
              emphasis: {
                areaColor: '#35b1ad'
              }
            },
              label: {
                normal: {
                  show: true,
                  color:'#fff',
                  fontSize:10,
                },
                emphasis: {
                  color:'#000',
                  show: true,
                  fontSize:10
                }
              },
              roam: true,
              zoom: 1.2
          },
                
          selectedMode: 'single',
          series : [     
          // {
          //   name:'交易地区分布',
          //   type:'map',
          //   mapType:'china',
          //   geoIndex:0,
          //   itemStyle:{
          //     normal:{label:{show:true}},
          //     emphasis:{label:{show:true}}
          //   },
          //   data:
          // },                    
          {
            name: 'ATM交易地区', 
            type: 'effectScatter',
            rippleEffect:{brushType:'stroke',scale:3.0},
            coordinateSystem: 'geo',
            symbol:'circle',
            itemStyle: {
              normal:{
              borderColor: 'rgba(0, 0, 0, 0.2)',
              color:'#ffb248'
              },
              emphasis:{
                shadowBlur: 20,
                borderWidth: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              },
            },
            showLegendSymbol: false,
            symbolSize: function (val) {
              return val[2] / 150000;
            },
            label: {
              normal: {
                show: false,
              },
              emphasis: {
                formatter: function(params){
                  return params.value[2]},
                show: true
              }
            },
            // zlevel:100,
            data: this.ATM
          },
          {
            name: '外汇交易地区', 
            type: 'effectScatter',
            rippleEffect:{brushType:'stroke',scale:3.0},
            coordinateSystem: 'geo',
            symbol:'circle',
            itemStyle: {
              normal:{
              borderColor: 'rgba(0, 0, 0, 0.2)',
              color:'#eb8146'
              },
              emphasis:{
                shadowBlur: 20,
                borderWidth: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              },
            },
            showLegendSymbol: true,
            symbolSize: function (val) {
              return val[2] / 150000;
            },
            label: {
              normal: {
                formatter: '{@[2]}',
                show: false,
              },
              emphasis: {
                formatter: '{@[2]}',
                show: true
              }
            },
            // zlevel:100,
            data: this.FX
          }
        ]
      }
      offlineArea.setOption(optionMap)
        }else{
          alert(res.data.msg, '外汇地图数据获取失败', 
        )}
      })
        }else{
          console.log(res.data.msg, 'ATM地图数据获取失败')
        }
      })
    },
    //获取地区分布数据
    getAreaData(){
      var that = this
      try{
      this. axios({
        url: '/offline/getLocationVolume',
        method: 'post',
        data: {trantype: 'ATM001', day: this.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
        console.log(res)
        let la = that.ATM.length
        var result = res.data.data
        result.forEach(element=>{
          for(let j=0;j<la;j++){
            if(that.ATM[j].name==element.agg_attr){
              that.ATM[j].value[2]=element.volume
              console.log(that.ATM[j])
              break;
            }
          }
        })
        }else{
          alert(res.data.msg, 'ATM地图数据获取失败', {
            confirmButtonText: '确定',
            callback: action => {
              this.$message({
                type: 'info',
                message: `action: ${ action }`
              });
            }
          }
          )
        }
      })
      this. axios({
        url: '/offline/getLocationVolume',
        method: 'post',
        data: {trantype: 'FX001', day: this.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
        console.log(res)
        let data = res.data.data
        let la = that.FX.length
        data.forEach(element=>{
          for(let j=0;j<la;j++){
            if(this.FX[j].name==element.agg_attr){
              this.FX[j].value[2]=element.volume
              break;
            }
          }
        })
        }else{
          alert(res.data.msg, '外汇地图数据获取失败', {
          confirmButtonText: '确定',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${ action }`
            });
          }
        }
        )}
      })
      }catch(err){
        console.log(err)
      }
    },
    // 获取金额分布数据
    getAmountRange(){
      var that = this
      try{
      this. axios({
        url: '/offline/getAmountRangeVolume',
        method: 'post',
        data: {trantype: 'ATM001', day: this.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          that.atmRangeErr = 0
          let data = res.data.data
          console.log('ATM')
          console.log(data)
          that.ATMRange.forEach(ele=>{ele.value = 0})
          data.forEach(element => {
            switch(element.agg_attr){
              case '0-100':
                that.ATMRange[0].value = element.count;
                break;
              case '101-500':
                that.ATMRange[1].value = element.count;
                break;
              case '501-1000':
                that.ATMRange[2].value = element.count;
                break;
              case '1001-10000':
                that.ATMRange[3].value = element.count;
                break;
              case '10001':
                that.ATMRange[4].value = element.count;
                break;
            }
          });
        }else{
          alert(res.data.msg, 'ATM金额分布失败', {
            confirmButtonText: '确定',
            callback: action => {
              this.$message({
                type: 'info',
                message: `action: ${ action }`
              });
            }
          }
          )}
      })
      this. axios({
        url: '/offline/getAmountRangeVolume',
        method: 'post',
        data: {trantype: 'FX001', day: this.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let data = res.data.data
          console.log('FX')
          console.log(data)
          that.FXRange.forEach(ele=>{ele.value = 0})
          data.forEach(element => {
            switch(element.agg_attr){
              case '0-100':
                that.FXRange[0].value = element.count;
                break;
              case '101-500':
                that.FXRange[1].value = element.count;
                break;
              case '501-1000':
                that.FXRange[2].value = element.count;
                break;
              case '1001-10000':
                that.FXRange[3].value = element.count;
                break;
              case '10001':
                that.FXRange[4].value = element.count;
                break;
            }
          });
        }else{
          alert(res.data.msg, '外汇金额分布失败', {
            confirmButtonText: '确定',
            callback: action => {
              this.$message({
                type: 'info',
                message: `action: ${ action }`
              });
            }
          }
          )}
      })
      }catch(err){
        console.log(err)
      }
    },
    // 获取外汇目的分布数据
    getFXPurpose(){
      var that = this
      try{
      this. axios({
        url: '/offline/getPurposeVolume',
        method: 'post',
        data: {day: this.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          that.fxPurposeErr = 0
          let data = res.data.data
          console.log(data)
          that.purposeData.forEach(ele=>{ele.value = 0})
          data.forEach(element => {
            switch(element.agg_attr){
              case '旅游':
                that.purposeData[0].value = element.volume;
                break;
              case '就医':
                that.purposeData[1].value = element.volume;
                break;
              case '留学':
                that.purposeData[2].value = element.volume;
                break;
              case '教育':
                that.purposeData[3].value = element.volume;
                break;
              case '其他':
                that.purposeData[4].value = element.volume;
                break;
            }
          });
        }else{
          alert(res.data.msg, '外汇目的分布失败', {
            confirmButtonText: '确定',
            callback: action => {
              this.$message({
                type: 'info',
                message: `action: ${ action }`
              });
            }
          }
          )}
      })
      }catch(err){
        console.log(err)
      }
    },
    // 获取外汇年龄分布数据
    getFXAge(){
      var that = this
      try{
      this. axios({
        url: '/offline/getAgeRangeVolume',
        method: 'post',
        data: {day: this.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          that.fxAgeErr = 0
          that.ageData.forEach(ele=>{ele.value = 0})
          let data = res.data.data
          console.log(data)
          data.forEach(element => {
            switch(element.agg_attr){
              case '18-25':
                that.ageData[0].value = element.volume;
                break;
              case '26-35':
                that.ageData[1].value = element.volume;
                break;
              case '36-45':
                that.ageData[2].value = element.volume;
                break;
              case '46-60':
                that.ageData[3].value = element.volume;
                break;
              case '61-80':
                that.ageData[4].value = element.volume;
                break;
              case '81':
                that.ageData[5].value = element.volume;
                break;
            }
          });
        }else{
          alert(res.data.msg, '外汇年龄失败', {
            confirmButtonText: '确定',
            callback: action => {
              this.$message({
                type: 'info',
                message: `action: ${ action }`
              });
            }
          }
          )}
      })
      }catch(err){
        console.log(err)
      }
    },
    // 获取外汇币种分布数据
    getFXCurrency(){
      var that = this
      try{
      this. axios({
        url: '/offline/getCurrencyVolume',
        method: 'post',
        data: { day: this.day},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("day", data.day);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          that.fxCurrencyErr = 0
          that.currencyData.forEach(ele=>{ele.value = 0})
          let data = res.data.data
          console.log(data)
          data.forEach(element => {
            switch(element.agg_attr){
              case '美元':
                that.currencyData[0].value = element.volume;
                break;
              case '英镑':
                that.currencyData[1].value = element.volume;
                break;
              case '日元':
                that.currencyData[2].value = element.volume;
                break;
              case '欧元':
                that.currencyData[3].value = element.volume;
                break;
              case '加拿大元':
                that.currencyData[4].value = element.volume;
                break;
              case '港币':
                that.currencyData[5].value = element.volume;
                break;
            }
          });
        }else{
          alert(res.data.msg, '外汇币种失败', {
            confirmButtonText: '确定',
            callback: action => {
              this.$message({
                type: 'info',
                message: `action: ${ action }`
              });
            }
          }
          )}
      })
      }catch(err){
        console.log(err)
      }
    }
  },
  mounted(){
    console.log(this.day)
    this.drawCharts()
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
.el-aside{
  background-color: #474c52;
  position: relative;
  height: 100%;
}
.el-menu{
  position: relative;
  width: 100%;
  height: 100%;
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
  width:100%;
}
.offline_container{
  background-image:url('../assets/images/bg02.png');
  background-size: cover;
  background-position: center;
  position: relative;
  height: 100%;
  box-sizing: border-box;
}
.up{
  position: relative;
  width: 100%;
  height: 70%;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
.areaChart{
  background-image:url('../assets/images/panel.png');
  background-size:100% 100%;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  position: relative;
  height: 98%;
  width: 59%;
  margin-top:0;
  margin-right:1%;
}
.totalAmount{
  position: relative;
  height: 100%;
  width:40%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.atmTran,.fxTran{
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  box-shadow: 0 2px 12px rgba(0, 0, 0, .12);
}
.rightDown{
  background-image:url('../assets/images/panel.png');
  background-size:100% 100%;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  height: 100%;
  width: 100%;
  margin-bottom: 2%;
  padding-top: 2%;
}
.down{
  // background-color: #BEE7E9;
  position: relative;
  width: 100%;
  height: 30%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
#offlineArea{
  width: 100%;
  height: 98%;
  margin-top:1%
}
#FXPurpose{
  background-image:url('../assets/images/panel.png');
  background-size:100% 100%;
  box-shadow: 0 2px 40px rgba(0, 0, 0, .12);
  border-radius: 6px;
  position: relative;
  width: 32.5%;
  height:100%
}
#FXAge{
  background-image:url('../assets/images/panel.png');
  background-size:100% 100%;
  box-shadow: 0 2px 40px rgba(0, 0, 0, .12);
  border-radius: 6px;
  position: relative;
  width:32.5%;
  height:100%
}
#FXCurrency{
  background-image:url('../assets/images/panel.png');
  background-size:100% 100%;
  box-shadow: 0 2px 40px rgba(0, 0, 0, .12);
  border-radius: 6px;
  position: relative;
  width:32.5%;
  height:100%
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