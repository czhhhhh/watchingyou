<template>
  <div class="RealTimeAnalysis">
    <el-container class="real_container">
    <!-- 头部 -->
    <el-header>
      <div>
        <el-dropdown placement="top-start">
          <span id="menu">
            菜单
            <i class="el-icon-more" style="color:#fff"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-menu background-color="#1d425d" text-color="#fff" active-text-color="#ffd04b" default-active="2" 
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
          <el-menu-item index="2">
            <i class="el-icon-s-data"></i>
            <span slot="title">实时业务</span>
          </el-menu-item>
          <el-menu-item index="3" @click="monitorSubmit">
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
        <div class="up">
          <!-- 实时业务地区分布图 -->
          <div class="areaChart">
            <div id="rtArea"></div>
          </div>
          <!-- 交易总量 -->
          <div class="totalAmount">
            <div class="rightUp">
              <div class="atmTran">
                <div>
                  <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:block; position:relative; top:2%; float:left">
                    ATM交易金额/最近5秒
                  </div>
                  <div style="clear:both">
                    <p style="font-size:30px; font-family:'微软雅黑'; color:yellow; display:inline">{{atmVolume}}</p>
                    <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:inline">  元</div>
                  </div>
                </div>
                <div>
                  <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:block; position:relative; top:2%; float:left">
                    ATM交易笔数/最近5秒
                  </div>
                  <div style="clear:both">
                    <p style="font-size:30px; font-family:'微软雅黑'; color:yellow; display:inline">{{atmCount}}</p>
                    <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:inline">  笔</div>
                  </div>
                </div>
              </div>
              <div class="fxTran">
                <div>
                  <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:block; position:relative; top:2%; float:left">
                    外汇交易金额/最近5秒
                  </div>
                  <div style="clear:both">
                    <p style="font-size:30px; font-family:'微软雅黑'; color:yellow; display:inline">{{fxVolume}}</p>
                    <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:inline">  元</div>
                  </div>
                </div>
                <div>
                  <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:block; position:relative; top:2%; float:left">
                    外汇交易笔数/最近5秒
                  </div>
                  <div style="clear:both">
                    <p style="font-size:30px; font-family:'微软雅黑'; color:yellow; display:inline">{{fxCount}}</p>
                    <div style="font-size:12px; font-family:'微软雅黑'; color: #fff; display:inline">  笔</div>
                  </div>
                </div>
              </div>
            </div>
          
            <div class="rightDown">
              <!-- 折线图 -->
              <div id="atmChart" :style="{width:'100%',height:'48%'}"></div>
              <div id="fxChart" :style="{width:'100%',height:'48%'}"></div>
            </div>
          </div>
        </div>
        <div class="down">
          <!-- 环形图 -->
          <!-- 外汇买入目的环形图 -->
          <div id="FXPurpose">
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
  </div>
</template>


<script>
export default {
  name: 'realTimeAnalysis',
  data(){
    return{
      roleType:window.sessionStorage.getItem('token'),
      // setInterval返回数据
      flag: 0,
      // 数据框数据
      atmVolume:0,
      atmCount:0,
      fxVolume:0,
      fxCount:0,
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
        {name: '北京市',value: [116.22, 39.9, 0] },
        {name: '天津市',value: [117.7, 39, 0] },
        {name: '上海市',value: [121.7, 30.9, 0]},
        {name: '重庆市',value: [108.5333, 29.5333, 0]},
        {name: '河北省',value: [116.4667, 38.0333, 0] },
        {name: '河南省',value: [114.8, 34.8, 0] },
        {name: '云南省',value: [102.6833, 24, 0] },
        {name: '辽宁省',value: [125.4, 40.816, 0] },
        {name: '黑龙江省',value: [128.6833, 45.75, 0] },
        {name: '湖南省',value: [113, 27.183, 0] },
        {name: '安徽省',value: [117.8, 31.85, 0] },
        {name: '山东省',value: [119, 36.633, 0] },
        {name: '新疆省',value: [89.6, 43.8, 0] },
        {name: '江苏省',value: [121, 32, 0] },
        {name: '浙江省',value: [120.7, 30, 0] },
        {name: '江西省',value: [117.8667, 28.6833, 0] },
        {name: '湖北省',value: [112.35, 30.617, 0] },
        {name: '广西省',value: [110.333, 24, 0] },
        {name: '甘肃省',value: [105, 35.5, 0] },
        {name: '山西省',value: [113.5667, 37.867, 0] },
        {name: '内蒙古',value: [113.8, 42.8167, 0] },
        {name: '陕西省',value: [109.9, 34.267, 0] },
        {name: '吉林省',value: [127.316, 43, 0] },
        {name: '福建省',value: [118.6, 25.5, 0] },
        {name: '贵州省',value: [108.7, 26.583, 0] },
        {name: '广东省',value: [115.25, 23.133, 0] },
        {name: '青海省',value: [100.75, 36.633, 0] },
        {name: '西藏',value: [93.167, 29.667, 0] },
        {name: '四川省',value: [102.0833, 29.65, 0] },
        {name: '宁夏',value: [107, 37.5, 0] },
        {name: '海南省',value: [110.6, 19.5, 0] },
        {name: '台湾',value: [122.3, 24.6, 0] },
        {name: '香港',value: [114.3, 22.3, 0] },
        {name: '澳门',value: [113.6, 22.222, 0] }
      ],
      // 图表横轴时间间隔
      interval: 5,
      // 交易金额折线图
      atmTransData:[
        // ['time',0]
      ],
      fxTransData:[
        // ['time',0]
      ],
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
    // 绘制图像
    drawCharts(){
      let that = this
      // 外汇目的
      let FXPurpose = this.$echarts.init(document.getElementById('FXPurpose'))
      this. axios({
        url: '/realtime/getPurposeVolume',
        method: 'post',
        data: {interval: this.interval},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("interval", data.interval);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          let map = res.data.data
          that.purposeData.forEach(ele=>{ele.value = 0})
          for(var key in map){
            that.purposeData.forEach(element=>{
              if(element.name==key){ 
              element.value=map[key]
            }
          })
        }
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
        textStyle:{color:'#fff'}
        },
        series: [
        {
            name: '外汇买入目的',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            stillShowZeroSum: true,
            itemStyle: {
                borderWidth: 0,
                color: function(params){
                var color = ['#d95850','#eb8146','#ffb248','#f2d643','#ebdba4','#893448']
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
           }
          ]
      })
      }else{
        console.log('外汇目的分布'+res.data.msg)
      }
      })
      
      // 外汇年龄
      let FXAge = this.$echarts.init(document.getElementById('FXAge'))
      this. axios({
        url: '/realtime/getAgeRangeVolume',
        method: 'post',
        data: {interval: this.interval},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("interval", data.interval);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          console.log(res)
          let map = res.data.data
          that.ageData.forEach(ele=>{ele.value = 0})
          for(var key in map){
            that.ageData.forEach(element=>{
              if(element.name==key){ 
              element.value=map[key]
            }
          })
        }
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
          textStyle:{color:'#fff'}
        },
        series: [
        {
            name: '外汇买入年龄',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            stillShowZeroSum:true,
            itemStyle: {
                borderWidth: 0,
                color: function(params){
                var color = ['#d95850','#eb8146','#ffb248','#f2d643','#ebdba4','#893448']
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
            data: that.ageData
         }]
      })
        }else{
          console.log('外汇年龄分布'+res.data.msg)}
      })
      // 外汇币种
      let FXCurrency = this.$echarts.init(document.getElementById('FXCurrency'))
      this. axios({
        url: '/realtime/getCurrencyVolume',
        method: 'post',
        data: { interval: this.interval},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("interval", data.interval);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          console.log(res.data)
          let map = res.data.data
          that.currencyData.forEach(ele=>{ele.value = 0})
          for(var key in map){
            that.currencyData.forEach(element=>{
              if(element.name==key){ 
              element.value=map[key]
            }
          })
        }
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
          textStyle:{color:'#fff'}
        },
        series: [
        {
            name: '外汇买入币种',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            stillShowZeroSum:true,
            itemStyle: {
                borderWidth: 0,
                color: function(params){
                var color = ['#d95850','#eb8146','#ffb248','#f2d643','#ebdba4','#893448']
                 return color[params.dataIndex]
                }
            },
            label:{
              show:false,
              position:"center"
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: '20',
                    position: 'center',
                    fontWeight: 'bold',
                    textStyle:{color:'#fff'}
                }
            },
            labelLine: {
                show: false
            },
            data: that.currencyData
         }]
      })
        }else{
          console.log("外汇币种分布"+res.data.msg)
          }
      })
      
      // 外汇金额折线图
      let FXAmount = this.$echarts.init(document.getElementById('fxChart'))
      this. axios({
        url: '/realtime/getVolume',
        method: 'post',
        data: {trantype: 'FX001', interval: 5, num:12},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("interval", data.interval);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          that.fxTransData.splice(0,that.fxTransData.length);
          let list = res.data.data
          console.log(list)
          list.forEach(ele=>{
            that.fxTransData.push({time:ele.agg_time,value: ele.volume})
          })
          console.log(that.fxTransData)
          FXAmount.setOption({
        title: {
        text: '外汇买入金额趋势',
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
          }
        },
        visualMap: {
        show: false,
        pieces: [{
                gt: 0,
                lte: 2000,
                color: '#93CE07'
            }, {
                gt: 2000,
                lte: 5000,
                color: '#FBDB0F'
            }],
            outOfRange: {
                color: '#d73e44'
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
        type: 'category',
        data: that.fxTransData.map(function(item){return item.time}),
        nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        yAxis: {
        type: 'value',
        axisLabel: {
          formatter: '{value} 元'
        },
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
            type: 'line',
            showSymbol: true,
            // data: data.map(function (item) {  return item[1];})
            data:that.fxTransData
          }
        ] 
      })
        }else{
          console.log('外汇金额图'+res.data.msg)
        }
      })
      
      // ATM金额折现图
      let ATMAmount = this.$echarts.init(document.getElementById('atmChart'))
      this. axios({
        url: '/realtime/getVolume',
        method: 'post',
        data: {trantype: 'ATM001', interval: 5, num:12},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("interval", data.interval);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
          that.atmTransData.splice(0,that.atmTransData.length);
          let list = res.data.data
          console.log(list)
          list.forEach(ele=>{
            that.atmTransData.push({time:ele.agg_time,value: ele.volume})
          })
          ATMAmount.setOption({
        title: {
        text: 'ATM交易金额趋势',
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
          }
        },
        visualMap: {
        show: false,
        pieces: [{
                gt: 0,
                lte: 2000,
                color: '#93CE07'
            }, {
                gt: 2000,
                lte: 5000,
                color: '#FBDB0F'
            }],
            outOfRange: {
                color: '#d73e44'
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
        type: 'category',
        data: that.fxTransData.map(function(item){return item.time}),
        // data: data.map(function (item) {  return item[0];})
        nameTextStyle:{
          color:'#fff'
        },
        axisLine:{
          lineStyle:{
            color:'#fff'
          }}
        },
        yAxis: {
        type: 'value',
        axisLabel: {
          formatter: '{value} 元'
        },
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
            type: 'line',
            showSymbol: true,
            // data: data.map(function (item) {  return item[1];})
            data:that.fxTransData
          }
        ] 

      })
        }else{
          console.log('ATM金额图'+res.data.msg)}
      })
      
      // 地图
      let rtArea = this.$echarts.init(document.getElementById('rtArea'))
      this. axios({
        url: '/realtime/getLocationVolume',
        method: 'post',
        data: {trantype: 'ATM001', interval: this.interval},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("interval", data.interval);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
        let map = res.data.data
        that.ATM.forEach(ele=>{ele.value[2] = 0})
         for(var key in map){
         that.ATM.forEach(element=>{
          if(element.name==key){ 
              element.value[2]=map[key]
            }
         })
        }
        console.log(that.ATM)
        this. axios({
        url: '/realtime/getLocationVolume',
        method: 'post',
        data: {trantype: 'FX001', interval: this.interval},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("interval", data.interval);
          return formData;
        }]
      }).then(res=>{
        if(res.data.status==200){
        console.log(res)
        let map = res.data.data
        that.FX.forEach(ele=>{ele.value[2] = 0})
        for(var key in map){
          that.FX.forEach(element=>{
            if(element.name==key){ 
              element.value[2]=map[key]
            }
          })
        }
         var optionMap = { 
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
          {
            name: 'ATM交易地区', 
            type: 'effectScatter',
            coordinateSystem: 'geo',
            symbol:'circle',
            itemStyle: {
              normal:{
              borderColor: 'rgba(0, 0, 0, 0.2)',
              color:'#FCAF17'
              },
              emphasis:{
                shadowBlur: 20,
                borderWidth: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              },
            },
            showLegendSymbol: true,
            symbolSize: function (val) {
              return val[2] / 2000;
            },
            label: {
              normal: {
                formatter: '{@[2]}',
                show: true,
              },
              emphasis: {
                formatter: '{@[2]}',
                show: true
              }
            },
            // zlevel:100,
            data: this.ATM
          },
          {
            name: '外汇交易地区', 
            type: 'effectScatter',
            coordinateSystem: 'geo',
            symbol:'circle',
            itemStyle: {
              normal:{
              borderColor: 'rgba(0, 0, 0, 0.2)',
              color:'#F26522'
              },
              emphasis:{
                shadowBlur: 20,
                borderWidth: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              },
            },
            showLegendSymbol: true,
            symbolSize: function (val) {
              return val[2] / 2000;
            },
            label: {
              normal: {
                formatter: '{@[2]}',
                show: true,
              },
              emphasis: {
                formatter: '{@[2]}',
                show: true
              }
            },
            data: this.FX
          }
        ]
      }
      rtArea.setOption(optionMap);
        }else{
          console.log(res.data.msg)
        }
      })
        }else{
          console.log(res.data.msg)
        }
      })
        // 绘制图表
     
      window.onresize = function(){
          FXPurpose.resize();
          FXAge.resize();
          FXCurrency.resize();
          FXAmount.resize();
          ATMAmount.resize();
          rtArea.resize()
      }
    },

    // 获取最近五秒的数据
    getVolume(){
      var that = this
      try{
      this. axios({
        url: '/realtime/getVolume',
        method: 'post',
        data: {trantype: 'ATM001', interval: 5, num:1},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("interval", data.interval);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        console.log(res)
        if(res.data.status==200){
          let list = res.data.data[0]
          that.atmVolume = list.volume
        }else{
          console.log(response.data.msg, '失败')}
      })
      this. axios({
        url: '/realtime/getVolume',
        method: 'post',
        data: {trantype: 'FX001', interval: 5, num:1},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("interval", data.interval);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        console.log(res)
        if(res.data.status==200){
         let list = res.data.data[0]
          that.fxVolume = list.volume
        }
      })
      }catch(err){
        console.log(err)
      }
    },
    getCount(){
      var that = this
      try{
      this. axios({
        url: '/realtime/getCount',
        method: 'post',
        data: {trantype: 'ATM001', interval: 5, num:1},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("interval", data.interval);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        console.log(res)
        if(res.data.status==200){
          let list = res.data.data[0]
          that.atmCount = list.count
        }else{
          alert(response.data.msg, '失败', {
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
        url: '/realtime/getCount',
        method: 'post',
        data: {trantype: 'FX001', interval: 5, num:1},
        transformRequest: [function (data) {
          var formData = new FormData();
          formData.append("trantype", data.trantype);
          formData.append("interval", data.interval);
          formData.append("num", data.num);
          return formData;
        }]
      }).then(res=>{
        console.log(res)
        if(res.data.status==200){
          let list = res.data.data[0]
          that.fxCount = list.count
        }
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
      that.getCount()
      that.getVolume()
      that.drawCharts()
    }, 10000)
  },
  destroyed(){
    clearInterval(this.flag)
  }
}
</script>

<style lang="less">
.RealTimeAnalysis{
  background-image:url('../assets/images/bg02.png');
  background-size: cover;
  background-position: center;
  height: 100%;
  width: 100%
}
.el-header{
  background-image:url('../assets/images/header-left.png');
  background-size:100% 100%;
  font-size: 30px;
  color: #fff;
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
  height: 100%;
  box-sizing: border-box;
}
.submenu.el-menu-item-group{
  position: relative;
  width:100%;
  overflow: hidden;
}
.el-main{
  width:100%;
}
.real_container{
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
  overflow: hidden;
  resize: both;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  position: relative;
  height: 98%;
  width: 59%;
  margin-top:0;
  margin-right:1%;
}
.totalAmount{
  overflow: hidden;
  resize: both;
  position: relative;
  height: 100%;
  width:40%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.rightUp{
  background-image:url('../assets/images/panel.png');
  background-size:100% 100%;
  overflow: hidden;
  border-radius: 6px;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-shadow: 0 2px 12px rgba(0, 0, 0, .12);
  height: 25%;
  width: 100%;
  margin:0
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
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  height: 69%;
  width: 100%;
  margin-bottom: 2%;
  margin-top: 2%;
  padding-top: 2%;
  padding-bottom: 2%;
}
.down{
  position: relative;
  width: 100%;
  height: 30%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
#rtArea{
  width: 100%;
  height: 98%;
  margin-top:1%
}
#FXPurpose{
  background-image:url('../assets/images/panel.png');
  background-size:100% 100%;
  overflow: hidden;
  box-shadow: 0 2px 40px rgba(0, 0, 0, .12);
  width: 32.5%;
  height:100%
}
#FXAge{
  background-image:url('../assets/images/panel.png');
  background-size:100% 100%;
  overflow: hidden;
  box-shadow: 0 2px 40px rgba(0, 0, 0, .12);
  width:32.5%;
  height:100%
}
#FXCurrency{
  background-image:url('../assets/images/panel.png');
  background-size:100% 100%;
  overflow: hidden;
  box-shadow: 0 2px 40px rgba(0, 0, 0, .12);
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