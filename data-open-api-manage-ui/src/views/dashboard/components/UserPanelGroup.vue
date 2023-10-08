<template>

  <el-row :gutter="40">
    <el-col :xs="20" :sm="12" :lg="18">
      <el-row :gutter="40" class="panel-group">
        <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
          <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
            <div class="card-panel-icon-wrapper icon-money">
              <svg-icon icon-class="api" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                订阅数量
              </div>
              <count-to :start-val="0" :end-val="statData.apiCount" :duration="2600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
          <div class="card-panel" @click="handleSetLineChartData('messages')">
            <div class="card-panel-icon-wrapper icon-money">
              <svg-icon icon-class="peoples" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                已通过
              </div>
              <count-to :start-val="0" :end-val="statData.userCount" :duration="3000" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
          <div class="card-panel" @click="handleSetLineChartData('purchases')">
            <div class="card-panel-icon-wrapper icon-money">
              <svg-icon icon-class="apply" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                审核中
              </div>
              <count-to :start-val="0" :end-val="statData.subscribeCount" :duration="3200" class="card-panel-num" />
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="40" class="panel-group">
        <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
          <div class="card-panel" @click="handleSetLineChartData('purchases')">
            <div class="card-panel-icon-wrapper icon-money">
              <svg-icon icon-class="request" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                调用次数
              </div>
              <count-to :start-val="0" :end-val="statData.callCount" :duration="3200" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
          <div class="card-panel" @click="handleSetLineChartData('messages')">
            <div class="card-panel-icon-wrapper icon-money">
              <svg-icon icon-class="success" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                成功次数
              </div>
              <count-to :start-val="0" :end-val="statData.successCount" :duration="3000" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
          <div class="card-panel" @click="handleSetLineChartData('purchases')">
            <div class="card-panel-icon-wrapper icon-money">
              <svg-icon icon-class="fail" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                失败次数
              </div>
              <count-to :start-val="0" :end-val="statData.failCount" :duration="3200" class="card-panel-num" />
            </div>
          </div>
        </el-col>
      </el-row>
    </el-col>
    <el-col :xs="4" :sm="12" :lg="6">
      <div style="margin-top:18px; background-color:rgb(240, 242, 245); height:270px">
        <div style="text-align: center;padding-top:20px">
          请求成功率
        </div>
        <shui-qiu ref="shuiqiu" :chart-data="statData.successRatio" height="270px" style="margin-top: -20px;" />
      </div>
    </el-col>
  </el-row>

</template>

<script>
import CountTo from 'vue-count-to'
import ShuiQiu from '@/components/ShuiQiu'

export default {
  components: {
    CountTo, ShuiQiu
  },
  props: {
    statData: {
      type: Object,
      default: function() {
        return {}
      }
    }
  },
  methods: {
    reResizeFn() {
      this.$nextTick(() => {
        this.$refs.shuiqiu.reResizeFn()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: rgb(240, 242, 245);
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: rgb(240, 242, 245);
        background-color: rgb(240, 242, 245);
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
      background-color: #fff;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
