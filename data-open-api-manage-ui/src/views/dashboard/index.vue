<template>
  <div class="dashboard-editor-container">

    <panel-group v-if="isAdminRole" ref="panel" :stat-data="statBaseData" />
    <user-panel-group v-else ref="panel" :stat-data="statBaseData" />

    <el-row style="background:rgb(240, 242, 245);padding:16px 16px 0;margin-bottom:32px;">
      <line-chart ref="line" :chart-data="requireData" />
    </el-row>

    <!-- <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <raddar-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <bar-chart />
        </div>
      </el-col>
    </el-row> -->
  </div>
</template>

<script>
import UserPanelGroup from './components/UserPanelGroup'
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
import { baseInfo, requireInfo } from '@/api/statistics-api'
import store from '@/store'

export default {
  name: 'DashboardAdmin',
  components: {
    PanelGroup,
    UserPanelGroup,
    LineChart
  },
  data() {
    return {
      statBaseData: {},
      requireData: {},
      isAdminRole: true
    }
  },
  mounted() {
    this.loadBaseInfo()
    this.loadRequireData()
    this.isAdminRole = store.getters.roles.indexOf('admin') > -1
    this.$nextTick(() => {
      window.onresize = () => {
        this.$refs.panel.reResizeFn()
        this.$refs.line.reResizeFn()
      }
    })
  },
  methods: {
    loadBaseInfo() {
      baseInfo().then(response => {
        this.statBaseData = response.data
        this.statBaseData['successRatio'] = []
        if(this.statBaseData['callCount'] !==0 ){
            this.statBaseData['successRatio'].push(this.statBaseData['successCount'] / this.statBaseData['callCount'])
        }else{
           this.statBaseData['successRatio'].push(0)
        }
      })
    },
    loadRequireData() {
      requireInfo().then(response => {
        const data = response.data
        const dateList = data.map(it => it['date'])
        const callCount = data.map(it => it['callCount'])
        const successCount = data.map(it => it['successCount'])
        const failCount = data.map(it => it['failCount'])
        this.requireData['date'] = dateList
        this.requireData['callCount'] = callCount
        this.requireData['successCount'] = successCount
        this.requireData['failCount'] = failCount
        this.$refs.line.initChart()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  // background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: rgb(240, 242, 245);
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
