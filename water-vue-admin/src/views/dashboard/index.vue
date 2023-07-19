<template>
  <div class="dashboard-container">
    <div class="title-box">
      <h3 class="newData-tiile">酸碱度</h3>
      <h3 class="newData-tiile">纯净度</h3>
      <h3 class="newData-tiile">温度</h3>
      <h3 class="newData-tiile">数据更新时间</h3>
    </div>

    <div class="newData">
      <div >{{newWater.ph}}pH</div>
      <div >{{newWater.purity}}%</div>
      <div >{{newWater.temp}}℃</div>
      <div >{{newWater.createTime}}</div>
    </div>

    <div style="display: flex;flex-wrap: wrap;">
      <div style="width:500px; height:360px;flex: 50%;padding-bottom: 50px" ref="ph"></div>
      <div style="width:500px; height:360px;flex: 50%;padding-bottom: 50px" ref="purity"></div>
      <div style="width:500px; height:360px;flex: 50%;padding-bottom: 50px" ref="temp"></div>
      <div style="width:500px; height:360px;flex: 50%;padding-bottom: 50px" ref="warning"></div>
    </div>



  </div>

</template>

<script>
  import * as echarts from 'echarts';
  import {
    listWater,
    getNewWater
  } from '@/api/content/water'
  import {
    mapGetters
  } from 'vuex'
  export default {
    name: 'Dashboard',
    data() {
      return {
        option: null,
        option1: null,
        option2: null,
        option3: null,
        waterData: [],
        queryParams: {
          pageNum: 1,
          pageSize: 100,
          name: null,
          metaKeywords: null,
          metaDescription: null,
          status: undefined,
          delFlag: 0
        },
        newWater:{},
        timer1: null,
        timer2: null

      }
    },
    computed: {
      ...mapGetters([
        'name'
      ])
    },
    methods: {
      showEcharts() {
        this.myChart = echarts.init(this.$refs.purity);
        this.myChart1 = echarts.init(this.$refs.ph);
        this.myChart2 = echarts.init(this.$refs.temp);
        this.myChart3 = echarts.init(this.$refs.warning);
        this.option = {
          title: {
            text: '纯净度'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['Purity']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: true,
            data: this.waterData.map(v => v.createTime)
          },

          yAxis: {
            type: 'value'
          },
          series: [{
            symbol: "none",
            name: 'Purity',
            type: 'line',
            stack: 'Total',
            data: this.waterData.map(v => v.purity)

          }
          ]
        }
        this.option1 = {
          title: {
            text: '酸碱度'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['pH']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: true,
            data: this.waterData.map(v => v.createTime)
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            symbol: "none",
            name: 'pH',
            type: 'line',
            stack: 'Total',
            data: this.waterData.map(v => v.ph),
            lineStyle: {
              color: "#ffd165", //折线的颜色
            },
          }]
        }
        this.option2 = {
          title: {
            text: '温度'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['TEMP']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: true,
            data: this.waterData.map(v => v.createTime)
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            symbol: "none",
            name: 'TEMP',
            type: 'line',
            stack: 'Total',
            data: this.waterData.map(v => v.temp),
            lineStyle: {
              color: "#ff0000", //折线的颜色
            },
          }]
        }
        this.option3 = {
          title: {
            text: '预警值'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['Warning']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: true,
            data: this.waterData.map(v => v.createTime)
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            symbol: "none",
            name: 'Warning',
            type: 'line',
            stack: 'Total',
            data: this.waterData.map(v => v.warning),
            lineStyle: {
              color: "#ffff00", //折线的颜色
            },
          }]
        }

        this.myChart.setOption(this.option);
        this.myChart1.setOption(this.option1);
        this.myChart2.setOption(this.option2);
        this.myChart3.setOption(this.option3);
      },
      getData() {
        listWater(this.queryParams).then(response => {
          this.waterData = response.rows;
          this.showEcharts();
        })
      },
      getNewData(){
        getNewWater().then(response=>{
          this.newWater = response;
        })
      },
      timerInterval(){
       this.timer1 = setInterval(()=>{
         this.getData();
       },10000)
      },
      timerInterval1(){
        this. timer2 = setInterval(()=>{
          this.getNewData();
        },2000)
      }
    },
    mounted() {

      this.showEcharts();
      this.getData();
      this.timerInterval1();
      this.timerInterval();

    },
    beforeDestroy(){
      clearInterval(this.timer1);
      clearInterval(this.timer2);
    }
  }
</script>

<style lang="scss" scoped>
  .dashboard-container {

    width: 100%;
    height: 100%;
    position: relative;
    top: 0;

  }

  .dashboard {
    &-container {
      margin: 30px;
    }

    &-text {
      font-size: 30px;
      line-height: 46px;
    }

  }

  //.dashboard-container {
  //  //flex: 50%;
  //  margin-bottom: 50px;
  //}

  .newData{
    display: flex;
    //justify-content: center;
    justify-content: space-around;
    margin-bottom: 50px;
    padding-right: 50px;
  }
  .newData div{


    width: 250px !important;
    height: 150px;
    border: 1px solid #ccc;
    border-radius: 5px;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow:  0 0 10px 0 rgba(#c02cfa, 0.5);
    font-size: 30px;
    font-weight: 700;
    text-align: center;
  }
  .title-box {
    width: 100%;
    height: 50px;
    display: flex;
    justify-content: space-around;
  }
  .newData-tiile {
    //font-size: 50px;
    //font-weight: 700;
    height: 24px;
    border-bottom: 3.5px solid #A9A9A9;
    margin-right: 175px;
  }

</style>
