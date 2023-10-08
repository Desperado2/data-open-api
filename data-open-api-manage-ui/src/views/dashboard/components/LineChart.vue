<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts/core'
require('echarts/theme/macarons') // echarts theme

export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      handler(val) {
        this.$nextTick(() => {
          this.setOptions(val)
        })
      }
    }
  },
  mounted() {
    // this.$nextTick(() => {
    //   this.initChart()
    // })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.chartData)
    },
    reResizeFn() {
      this.$nextTick(() => {
        this.chart.resize()
      })
    },
    setOptions({ callCount, date, failCount, successCount } = {}) {
      this.chart.setOption({
        xAxis: {
          data: date,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 30,
          right: 32,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          axisTick: {
            show: false
          }
        },
        legend: {
          data: ['调用次数', '成功次数', '失败次数']
        },
        series: [{
          name: '调用次数',
          itemStyle: {
            normal: {
              color: '#3888fa',
              lineStyle: {
                color: '#3888fa',
                width: 2
              }
            }
          },
          smooth: true,
          type: 'line',
          data: callCount,
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: '成功次数',
          smooth: true,
          type: 'line',
          itemStyle: {
            normal: {
              color: '#00FF7F',
              lineStyle: {
                color: '#00FF7F',
                width: 2
              },
              areaStyle: {
                color: '#7FFFAA'
              }
            }
          },
          data: successCount,
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        },
        {
          name: '失败次数',
          smooth: true,
          type: 'line',
          itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              },
              areaStyle: {
                color: '#FFC0CB'
              }
            }
          },
          data: failCount,
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }]
      })
    }
  }
}
</script>
