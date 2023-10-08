<template>
  <div class="responsePanel">
    <div class="response-btns">
      <el-tooltip class="item" effect="dark" content="use Result Structure" placement="top-end">
        <el-checkbox v-model="customResponseFormatCopy" :disabled="!onEditPage" style="padding: 3px 5px;z-index: 1000" :true-label="1" :false-label="0">Structure</el-checkbox>
      </el-tooltip>
      <el-button-group>
        <el-tooltip class="item" effect="dark" content="Copy to Clipboard" placement="top-end">
          <el-button
            v-clipboard:copy="responseBodyCopy"
            v-clipboard:success="handleJsonResultCopySuccess"
            v-clipboard:error="handleJsonResultCopyError"
            class="z-index-top"
            size="mini"
            round
          >
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#iconcopy" />
            </svg>
          </el-button>
        </el-tooltip>
        <el-tooltip class="item" effect="dark" content="Format Result" placement="top-end">
          <el-button
            v-if="panelActiveName ==='result_view' && resultType ==='json'"
            class="z-index-top"
            size="mini"
            round
            @click.native="handleJsonResultFormatter"
          >
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#iconformat" />
            </svg>
          </el-button>
        </el-tooltip>
        <el-tooltip class="item" effect="dark" content="Save As Download" placement="top-end">
          <el-button
            v-if="panelActiveName ==='result_view' && resultType ==='bytes'"
            class="z-index-top"
            size="mini"
            round
            @click.native="handleResultDownload"
          >
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icondownload" />
            </svg>
          </el-button>
        </el-tooltip>
        <el-tooltip class="item" effect="dark" content="Format Structure" placement="top-end">
          <el-button
            v-if="panelActiveName ==='result_format'"
            class="z-index-top"
            size="mini"
            round
            @click.native="handleStructureFormatter"
          >
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#iconformat" />
            </svg>
          </el-button>
        </el-tooltip>
      </el-button-group>
    </div>
    <el-tabs v-model="panelActiveName" class="response-tabs" type="card">
      <el-tab-pane name="result_view" label="Result">
        <div ref="responsePanel" />
      </el-tab-pane>
      <el-tab-pane name="result_format" label="Structure">
        <div ref="responseFormatPanel" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import { defineMonacoEditorFoo } from '@/utils/editorUtils'
import { formatDate } from '@/utils/devutils'

export default {
  props: {
    responseBody: {
      type: String,
      default: function() {
        return '"empty."'
      }
    },
    // response-format
    responseFormat: {
      type: String,
      default: function() {
        return '"empty."'
      }
    },
    customResponseFormat: {
      type: Number,
      default: function() {
        return 0
      }
    },
    onEditPage: {
      type: Boolean,
      default: false
    },
    resultType: {
      type: String,
      default: function() {
        return 'json'
      }
    }
  },
  data() {
    return {
      optionInfoCopy: '',
      customResponseFormatCopy: 0,
      responseBodyCopy: '',
      panelActiveName: 'result_view',
      height: '10px'
    }
  },
  watch: {
    'optionInfoCopy': {
      handler(val, oldVal) {
        this.$emit('onResponseFormat', this.optionInfoCopy)
      },
      deep: true
    },
    'customResponseFormatCopy': {
      handler(val, oldVal) {

        if (this.customResponseFormatCopy === 1) {
          const self = this
          self.$nextTick(function() {
            self.panelActiveName = 'result_view'
          })
        }
        this.$emit('onCustomResponseFormatChange', this.customResponseFormatCopy)
      },
      deep: true
    },
    'responseBodyCopy': {
      handler(val, oldVal) {
        this.$emit('onResponseBodyChange', this.responseBodyCopy)
      }
    }
  },
  mounted() {
    const self = this
    this.monacoDataEditor = defineMonacoEditorFoo(this.$refs.responsePanel, { language: 'javascript', wordWrap: 'on' })
    this.monacoDataEditor.onDidChangeModelContent(function(event) { // 编辑器内容changge事件
      self.responseBodyCopy = self.monacoDataEditor.getValue()
    })
    //
    this.monacoForamtEditor = defineMonacoEditorFoo(this.$refs.responseFormatPanel, { language: 'javascript', readOnly: !this.onEditPage })
    this.monacoForamtEditor.onDidChangeModelContent(function(event) { // 编辑器内容changge事件
      self.optionInfoCopy = self.monacoForamtEditor.getValue()
    })

    //
    this.responseBodyCopy = this.responseBody
    this.optionInfoCopy = JSON.stringify(JSON.parse(this.customResponseFormat), null, 2)
    // this.customResponseFormatCopy = JSON.stringify(JSON.parse(this.customResponseFormat), null, 2)
    this.customResponseFormatCopy = this.customResponseFormat
    this.doUpdate()
  },
  methods: {
    // 响应结果格式化
    handleJsonResultFormatter() {
      try {
        this.monacoDataEditor.updateOptions({ language: 'javascript' })
        this.responseBodyCopy = JSON.stringify(JSON.parse(this.responseBodyCopy), null, 2)
        this.monacoDataEditor.setValue(this.responseBodyCopy)
      } catch (e) {
        this.$message.error('JSON格式化错误 : ' + e)
      }
    },
    // 响应结果格式化
    handleStructureFormatter() {
      try {
        this.optionInfoCopy = JSON.stringify(JSON.parse(this.optionInfoCopy), null, 2)
        this.monacoForamtEditor.setValue(this.optionInfoCopy)
      } catch (e) {
        this.$message.error('结构体格式化错误 : ' + e)
      }
    },
    // 拷贝结果
    handleJsonResultCopySuccess() {
      this.$message({ message: '复制成功', type: 'success' })
    },
    handleJsonResultCopyError() {
      this.$message.error('复制失败')
    },
    // 下载
    handleResultDownload() {
      // 把十六进制转换为bytes
      const localResponseBody = this.responseBody
      const localArrays = localResponseBody.replace(/\n/g, ' ').split(' ')
      const byteArray = []
      for (let i = 0; i < localArrays.length; i++) {
        byteArray.push(parseInt(localArrays[i], 16))
      }
      const byteUint8Array = new Uint8Array(byteArray)
      // 创建隐藏的可下载链接
      const eleLink = document.createElement('a')
      eleLink.download = formatDate(new Date()) + '.result'
      eleLink.style.display = 'none'
      // 字符内容转变成blob地址
      const blob = new Blob([byteUint8Array])
      eleLink.href = URL.createObjectURL(blob)
      // 触发点击
      document.body.appendChild(eleLink)
      eleLink.click()
      // 然后移除
      document.body.removeChild(eleLink)
    },
    //
    // 执行布局
    doLayout(height, width) {
      this.monacoDataEditor.layout({ height: (height - 47), width: width })
      this.onEditPage && this.monacoForamtEditor.layout({ height: (height - 47), width: width })
    },
    doUpdate() {
      this.customResponseFormatCopy = this.customResponseFormat
      this.responseBodyCopy = this.responseBody
      this.optionInfoCopy = this.responseFormat
      this.monacoDataEditor.setValue(this.responseBodyCopy)
      this.onEditPage && this.monacoForamtEditor.setValue(this.optionInfoCopy)
    }
  }
}
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

.responsePanel {
  overflow-x: scroll;
}

.response-tabs {
  top: -33px;
  position: relative;
}

.response-btns {
  padding: 2px 5px;
  display: flex;
  justify-content: flex-end;

}

.z-index-top {
  z-index: 1000;
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
