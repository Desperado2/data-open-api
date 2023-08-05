<template>
  <div id="sss" :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts/core'
// import echarts from 'echarts'
import 'echarts-liquidfill'
require('echarts/theme/macarons') // echarts theme

export default {
  name: 'ShuiQiu',
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
      default: '316px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Array,
      required: true,
      default: () => [0]
    }
  },
  data() {
    return {
      shuiQiuChart: null
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
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.shuiQiuChart) {
      return
    }
    this.shuiQiuChart.dispose()
    this.shuiQiuChart = null
  },
  methods: {
    initChart() {
      this.shuiQiuChart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.chartData)
    },
    reResizeFn() {
      this.$nextTick(() => {
        this.shuiQiuChart.resize()
      })
    },
    setOptions(data) {
      this.shuiQiuChart.setOption({
        series: [{
          type: 'liquidFill',
          data: data,
          label: {
            normal: {
              textStyle: {
                color: 'green',
                insideColor: 'yellow',
                fontSize: 50
              }
            }
          },
          color: ['#0f0'],
          outline: {
            show: false
          },
          backgroundStyle: {
            shadowColor: 'rgba(0, 0, 0, 0.4)',
            shadowBlur: 20
          }
        }]
      })
    }
  }
}
</script>
