<template>
  <div style="display: inline;">
    <el-button-group>
      <!-- 设置 -->
      <el-tooltip class="item" effect="dark" content="更多设置" placement="bottom-end">
        <el-button size="mini" round @click.native="handleMoreAction">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#iconmore" />
          </svg>
        </el-button>
      </el-tooltip>
      <!-- 保存 -->
      <el-tooltip class="item" effect="dark" content="保存" placement="bottom-end">
        <el-button size="mini" round :disabled="!canEdit" @click.native="handleSaveAction">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#iconsave" />
          </svg>
        </el-button>
      </el-tooltip>
      <!-- 执行 -->
      <el-tooltip class="item" effect="dark" content="执行查询" placement="bottom-end">
        <el-button size="mini" round @click.native="handleExecuteAction">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#iconexecute" />
          </svg>
        </el-button>
      </el-tooltip>
      <!-- 冒烟 -->
      <el-tooltip class="item" effect="dark" content="冒烟测试" placement="bottom-end">
        <el-button size="mini" round :disabled="disabledBtn('testAction')" @click.native="handleTestAction">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#icontest" />
          </svg>
        </el-button>
      </el-tooltip>
      <!-- 发布 -->
      <el-tooltip class="item" effect="dark" content="发布" placement="bottom-end">
        <el-button size="mini" round :disabled="disabledBtn('publishAction')" @click.native="handlePublishAction">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#iconrelease" />
          </svg>
        </el-button>
      </el-tooltip>

      <!-- 历史 -->
      <el-tooltip class="item" effect="dark" content="发布历史" placement="bottom-end">
        <el-button v-popover:releaseHistoryPopover size="mini" round :disabled="disabledBtn('historyAction')" @click.native="handleHistoryAction">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#iconhistory" />
          </svg>
        </el-button>
      </el-tooltip>
    </el-button-group>
    <div style="display: block;position: absolute;z-index: 1000;">
      <el-popover ref="releaseHistoryPopover" placement="bottom" title="历史版本" width="250">
        <el-timeline style="max-height: 300px;overflow-y: scroll; padding-top: 5px;">
          <el-timeline-item v-for="(history, index) in historyList" :key="history.id" :color="historyIconColor(index===0 ? 1:2)" :hide-timestamp="true" size="large">
            <span>{{ history.createTime }}</span>
            <el-button size="mini" circle icon="el-icon-edit" style="float:right;margin-top: 5px;" @click.native="handleRecoverAction(history.id)" />
          </el-timeline-item>
        </el-timeline>
      </el-popover>
    </div>

    <el-drawer :visible.sync="moreConfig" :with-header="false" size="70%">
      <div style="padding: 20px 10px 0px 10px;">
        <el-collapse v-model="drawerConfig.activeNames">
          <el-collapse-item title="参数" name="1">
            <div class="z-index-top" style="padding-right: 10px;">
              <span style="padding-right: 5px;line-height: 24px;">包装所有的参数</span>
              <el-switch v-model="optionInfoCopy['wrapAllParameters']" />
              <span style="padding: 5px;line-height: 24px;">为新参数</span>
              <el-input v-model="optionInfoCopy['wrapParameterName']" :disabled="!optionInfoCopy['wrapAllParameters']" size="mini" style="width: 80px; display: inline-block" />
            </div>
          </el-collapse-item>
          <el-collapse-item title="跨域" name="2">
            <div class="z-index-top" style="padding-right: 10px;">
              <span style="padding-right: 5px;line-height: 24px;">禁用</span>
              <el-switch v-model="optionInfoCopy['enableCrossDomain']" />
              <span style="padding: 5px;line-height: 24px;">启用</span>
            </div>
          </el-collapse-item>
          <el-collapse-item title="缓存" name="3">
            <div class="z-index-top" style="padding-right: 10px;">
              <span style="padding-right: 5px;line-height: 24px;">禁用</span>
              <el-switch v-model="optionInfoCopy['enableCache']" />
              <span style="padding: 5px;line-height: 24px;">启用</span>
              <div>
                <span style="line-height: 24px;">缓存有效时长:</span>
                <el-input-number v-model="optionInfoCopy['cacheValidity']" :disabled="!optionInfoCopy['enableCache']" controls-position="right" :min="0" size="mini" style="width: 150px; display: inline-block" />
                <span style="padding: 5px;line-height: 24px;">秒</span>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-drawer>
  </div>
</template>
<script>
import { Loading } from 'element-ui'
import { checkRequestBody, fixGetRequestBody, statusTagInfo } from '@/utils/devutils'
import { addApiScript, updateApiScript, executeApiScript, apiScriptSmokeTest, publishApiScript, publishHistoryList } from '@/api/api-scripts'

export default {
  props: {
    optionInfo: {
      type: Object,
      default: function() {
        return {}
      }
    },
    canEdit: {
      type: Boolean,
      default: true
    },
    apiInfo: {
      type: Object,
      default: function() {
        return {
          apiID: 1,
          requestMode: 'POST',
          apiAddress: '',
          name: '',
          description: "There is no comment, Click 'info' icon to add comment"
        }
      }
    },
    apiScript: {
      type: Object,
      default: function() {
        return {
          id: null,
          apiId: null,
          testDatasourceCode: '',
          preDatasourceCode: '',
          prodDatasourceCode: '',
          apiRunEnvironment: 'test',
          apiScriptType: 'SQL',
          apiScript: '-- a new Query.\nselect * from a limit 1;',
          apiScriptStatus: null,
          apiRequestHeader: [],
          apiRequestBody: '{"message":"Hello DataQL."}',
          apiResponseHeader: [],
          apiResponseBody: '"empty."',
          apiResponseFormat: '{"returnCode":"@returnCode","message":"@message","result":"@result","dataSize":"@dataSize"}',
          apiOption: { ...this.defaultOption },
          customResultStructure: []
        }
      }
    },
    actionStatus: {
      type: Object,
      default: function() {
        return {
          newMode: true,
          disablePublish: true
        }
      }
    }
  },
  data() {
    return {
      moreConfig: false,
      historyList: [],
      optionInfoCopy: {},
      drawerConfig: {
        activeNames: ['1', '2', '3']
      }
    }
  },
  watch: {
    'optionInfoCopy': {
      handler(val, oldVal) {
        if (val['enableCache'] === false) {
          this.optionInfoCopy['cacheValidity'] = 0
        }
        this.$emit('onOptionChange', this.optionInfoCopy)
      },
      deep: true
    }
  },
  mounted() {
    const self = this
    self.$nextTick(function() {
      self.doUpdate()
    })
  },
  methods: {
    historyIconColor(status) {
      return statusTagInfo(status).tagColor
    },
    disabledBtn(btnName) {
      if (btnName === 'testAction') {
        return (this.actionStatus.newMode || this.apiScript.apiScriptStatus === 1) && !this.canEdit
      }
      if (btnName === 'publishAction') {
        return (this.actionStatus.newMode || this.apiScript.apiScriptStatus === 1 || this.actionStatus.disablePublish) && !this.canEdit
      }
      if (btnName === 'historyAction') {
        return this.actionStatus.newMode && !this.canEdit
      }
      if (btnName === 'disableAction') {
        return (this.actionStatus.newMode ||
          !(this.apiScript.apiScriptStatus === 1 || this.apiScript.apiScriptStatus === 2)) && !this.canEdit
      }
      if (btnName === 'deleteAction') {
        return this.actionStatus.newMode && !this.canEdit
      }
      return false
    },
    //
    // 更多配置按钮
    handleMoreAction() {
      this.moreConfig = true
    },
    // 保存按钮
    handleSaveAction() {
      const self = this
      const newLoading = Loading.service({ fullscreen: true, background: 'rgb(0,0,0,0.5)' })
      try {
        const selfApiRequestBody = fixGetRequestBody(self.apiInfo.requestMode, self.apiScript.apiRequestBody)
        const selfApiResponseFormat = JSON.parse(self.apiScript.apiResponseFormat)
        if (self.apiScript.id && self.apiScript.id !== '') {
          updateApiScript(
            {
              id: self.apiScript.id,
              apiId: self.apiScript.apiId,
              apiRunEnvironment: self.apiScript.apiRunEnvironment,
              testDatasourceCode: self.apiScript.testDatasourceCode,
              preDatasourceCode: self.apiScript.preDatasourceCode,
              prodDatasourceCode: self.apiScript.prodDatasourceCode,
              apiScriptType: self.apiScript.apiScriptType,
              apiScript: self.apiScript.apiScript,
              apiScriptStatus: self.apiScript.apiScriptStatus,
              apiRequestHeader: self.apiScript.apiRequestHeader,
              apiRequestBody: selfApiRequestBody,
              apiResponseHeader: self.apiScript.apiResponseHeader,
              apiResponseFormat: selfApiResponseFormat,
              apiOption: self.apiScript.apiOption,
              customResultStructure: self.apiScript.customResultStructure
            }
          ).then(response => {
            newLoading.close()
            if (!self.actionStatus.newMode) {
              self.$message({ message: '保存成功', type: 'success' })
              self.$emit('onAfterSave', self.apiInfo.apiStatus, response.data)
            } else {
              this.$router.push('/edit/' + response.data.result)
            }
          }).finally(() => {
            newLoading.close()
          })
        } else {
          addApiScript(
            {
              id: self.apiScript.id,
              apiId: self.apiScript.apiId,
              apiRunEnvironment: self.apiScript.apiRunEnvironment,
              testDatasourceCode: self.apiScript.testDatasourceCode,
              preDatasourceCode: self.apiScript.preDatasourceCode,
              prodDatasourceCode: self.apiScript.prodDatasourceCode,
              apiScriptType: self.apiScript.apiScriptType,
              apiScript: self.apiScript.apiScript,
              apiScriptStatus: self.apiScript.apiScriptStatus,
              apiRequestHeader: self.apiScript.apiRequestHeader,
              apiRequestBody: selfApiRequestBody,
              apiResponseHeader: self.apiScript.apiResponseHeader,
              apiResponseFormat: selfApiResponseFormat,
              apiOption: self.apiScript.apiOption,
              customResultStructure: self.apiScript.customResultStructure
            }
          ).then(response => {
            if (!self.actionStatus.newMode) {
              self.$message({ message: '保存成功', type: 'success' })
              self.$emit('onAfterSave', self.apiInfo.apiStatus, response.data)
            } else {
              this.$router.push('/edit/' + response.data.result)
            }
          }).finally(() => {
            newLoading.close()
          })
        }
      } catch (err) {
        self.$message({ message: '参数有误，请检查', type: 'error' })
      } finally {
        newLoading.close()
      }
    },
    // 执行按钮
    handleExecuteAction() {
      // test
      const newLoading = Loading.service({ fullscreen: true, background: 'rgb(0,0,0,0.5)' })
      const testResult = checkRequestBody(this.apiInfo.requestMode, this.apiScript.apiScriptType, this.apiScript.apiRequestBody,
        this.apiScript.testDatasourceCode, this.apiScript.preDatasourceCode, this.apiScript.prodDatasourceCode,
        this.apiScript.apiRunEnvironment, this.apiScript.apiResponseFormat)
      if (!testResult) {
        newLoading.close()
        return
      }
      const self = this
      executeApiScript({
        id: self.apiScript.id,
        apiId: self.apiScript.apiId,
        apiRunEnvironment: self.apiScript.apiRunEnvironment,
        testDatasourceCode: self.apiScript.testDatasourceCode,
        preDatasourceCode: self.apiScript.preDatasourceCode,
        prodDatasourceCode: self.apiScript.prodDatasourceCode,
        apiScriptType: self.apiScript.apiScriptType,
        apiScript: self.apiScript.apiScript,
        apiScriptStatus: self.apiScript.apiScriptStatus,
        apiRequestHeader: self.apiScript.apiRequestHeader,
        apiRequestBody: fixGetRequestBody(self.apiInfo.requestMode, self.apiScript.apiRequestBody),
        apiResponseHeader: self.apiScript.apiResponseHeader,
        apiResponseFormat: JSON.parse(self.apiScript.apiResponseFormat),
        apiOption: self.apiScript.apiOption,
        customResultStructure: self.apiScript.customResultStructure
      }).then(response => {
        this.$message({
          message: '执行成功',
          type: 'success'
        })
        self.$emit('onExecute', response)
      }).finally(() => {
        newLoading.close()
      })
    },
    // 冒烟按钮
    handleTestAction() {
      // test
      const newLoading = Loading.service({ fullscreen: true, background: 'rgb(0,0,0,0.5)' })
      const testResult = checkRequestBody(this.apiInfo.requestMode, this.apiScript.apiScriptType, this.apiScript.apiRequestBody,
        this.apiScript.testDatasourceCode, this.apiScript.preDatasourceCode, this.apiScript.prodDatasourceCode,
        this.apiScript.apiRunEnvironment, this.apiScript.apiResponseFormat)
      if (!testResult) {
        newLoading.close()
        return
      }
      //
      const self = this
      apiScriptSmokeTest(self.apiScript.apiId).then(response => {
        this.$message({
          message: '执行成功',
          type: 'success'
        })
        newLoading.close()
        self.$emit('onSmokeTest', response)
      }).finally(() => {
        newLoading.close()
      })
    },
    // 发布按钮
    handlePublishAction() {
      const self = this
      const newLoading = Loading.service({ fullscreen: true, background: 'rgb(0,0,0,0.5)' })
      publishApiScript(self.apiScript.apiId).then(response => {
        this.$message({
          message: '发布成功',
          type: 'success'
        })
        newLoading.close()
        self.$emit('onPublish', response.data.result)
      }).finally(() => {
        newLoading.close()
      })
    },
    // 历史按钮
    handleHistoryAction() {
      const self = this
      publishHistoryList(self.apiScript.apiId).then(response => {
        console.log(response.data)
        self.historyList = response.data
      })
    },
    // 恢复历史的某个版本
    handleRecoverAction(historyId) {
      this.$emit('onRecover', historyId)
    },
    doUpdate() {
      this.optionInfoCopy = this.apiScript.apiOption
    }
  }
}
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.is-disabled {
  background-color: #f3f5f9 !important;
  -webkit-filter: grayscale(1); /* Webkit */
  filter: grayscale(1); /* W3C */
}

.el-timeline-item {
  padding-bottom: 15px !important;
}

.el-timeline-item:hover {
  background-color: #f7f7f7;
  padding-bottom: 15px !important;
}

.el-timeline {
  padding-inline-start: 5px !important;
}

body {
  margin: 0px;
  padding: 0px;
}

.el-header, .el-main {
  padding: 0px !important;
}

.el-table td, .el-table th {
  padding: 2px 0 !important;
}

.el-table .cell {
  padding: 0px 4px !important;
}

.el-tabs--border-card > .el-tabs__content, .el-tabs--card > .el-tabs__header {
  margin: 0px;
  padding-top: 3px;
}

.el-tabs--border-card {
  height: 100% !important;
  overflow-y: hidden !important;
}

.el-tabs__item {
  height: 30px !important;
  line-height: 30px !important;
}

.vue-splitter-container {
  padding: 1px;
}

.el-tabs--card > .el-tabs__header {
  padding-left: 30px;
}

.el-table--border {
  border: 0px !important;
}

.el-input--mini .el-input__inner {
  height: 25px !important;
  line-height: 25px !important;
}

.el-divider--horizontal {
  margin: 0px !important;
}

.input-with-select .el-input-group__prepend {
  background-color: #fff;
}

.icon {
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}

.el-drawer.rtl {
  overflow-y: scroll !important;
}

</style>
