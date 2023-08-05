<template>
  <div>
    <div class="monacoEditorHeader">
      <div style="width: 60%; margin-top: 2px; display: inline-flex;">
        <el-select v-model="apiInfo.requestMode" placeholder="Choose" style="width: 120px;padding-right: 5px;" size="mini" disabled>
          <el-option
            v-for="item in api_requestMode_map"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-select v-model="apiScript.datasourceCode" placeholder="请选择数据源" style="width: 200px;padding-right: 5px;" size="mini">
          <el-option
            v-for="item in datasourceList"
            :key="item.code"
            :label="item.name"
            :value="item.code"
          />
        </el-select>
        <el-tooltip class="item" effect="dark" placement="bottom" :content="apiInfo.description" :disabled="showComment">
          <el-input v-model="apiInfo.apiAddress" placeholder="the path to access this Api" class="input-with-select" size="mini" :disabled="!editerActions.newMode">
            <el-button slot="append" icon="el-icon-info" @click.native="handleShowComment" />
          </el-input>
        </el-tooltip>
      </div>
      <div v-if="showComment" class="comment">
        <el-input v-model="apiInfo.comment" placeholder="Api's comment." size="mini" @input="handleCommentOnchange">
          <template slot="prepend">Comment</template>
        </el-input>
      </div>
      <div style="display: inline-table;padding-left: 5px;">
        <el-radio-group v-model="apiScript.apiScriptType" size="mini" @change="loadEditorMode">
          <el-tooltip class="item" effect="dark" placement="bottom" content="GROOVY language." style="width:100px">
            <el-radio border label="GROOVY" />
          </el-tooltip>
          <el-tooltip class="item" effect="dark" placement="bottom" content="JAVASCRIPT language." style="width:110px">
            <el-radio border label="JAVASCRIPT" />
          </el-tooltip>
          <el-tooltip class="item" effect="dark" placement="bottom" content="SQL language.">
            <el-radio border label="SQL" />
          </el-tooltip>
        </el-radio-group>
      </div>
      <div style="float: right;">
        <EditorActions
          ref="editerActionsPanel"
          :api-info="apiInfo"
          :api-script="apiScript"
          :action-status="editerActions"
          @onOptionChange="(data)=> {apiScript.apiOption = data}"
          @onAfterSave="onAfterSave"
          @onPublish="onAfterSave"
          @onDisable="onAfterSave"
          @onExecute="onExecute"
          @onSmokeTest="onSmokeTest"
          @onRecover="onRecover"
        />
        <!-- <div style="display: inline-table;padding-left: 5px;">
          <el-tooltip class="item" effect="dark" placement="top" content="Current Api Status">
            <el-tag size="mini" style="width: 65px;text-align: center;" :type="tagInfo.css">{{ tagInfo.title }}</el-tag>
          </el-tooltip>
        </div> -->
      </div>
    </div>
    <el-divider />
    <div :style="{height: panelHeight + 'px'}">
      <SplitPane :min-percent="10" :default-percent="panelPercentVertical" split="vertical" @resize="handleVerticalSplitResize">
        <template slot="paneL">
          <div ref="container" />
        </template>
        <template slot="paneR">
          <SplitPane :min-percent="10" :default-percent="panelPercentHorizontal" split="horizontal" @resize="handleHorizontalSplitResize">
            <template slot="paneL">
              <RequestPanel
                ref="editerRequestPanel"
                :header-data="apiScript.apiRequestHeader"
                :request-body="apiScript.apiRequestBody"
                :hide-run-btn="true"
                :api-info="apiInfo"
                :option-info="apiScript.apiOption"
                @onOptionChange="(data)=> { this.apiScript.apiOption = data}"
                @onHeaderChange="(data)=> { this.apiScript.apiRequestHeader = data}"
                @onRequestBodyChange="(data)=> { this.apiScript.apiRequestBody = data}"
              />
            </template>
            <template slot="paneR">
              <ResponsePanel
                ref="editerResponsePanel"
                :response-body="apiScript.apiResponseBody"
                :response-format="apiScript.apiResponseFormat"
                :custom-response-format="apiScript.customResultStructure"
                :on-edit-page="true"
                result-type="json"
                :option-info="apiScript.apiOption"
                @onCustomResponseFormatChange="(data) => {this.apiScript.customResultStructure = data}"
                @onResponseFormat="(data) => {this.apiScript.apiResponseFormat = data}"
                @onResponseBodyChange="(data)=> { this.apiScript.apiResponseBody = data}"
              />
            </template>
          </SplitPane>
        </template>
      </SplitPane>
    </div>
  </div>
</template>
<script>
import EditorActions from './components/EditorActions'
import RequestPanel from './components/RequestPanel'
import ResponsePanel from './components/ResponsePanel'
import { defineMonacoEditorFoo, loadMonacoEditorSelfTheme } from '@/utils/editorUtils'
import { getApiById } from '@/api/api'
import { apiBaseUrl } from '@/utils/api-const'
import { listALL } from '@/api/datasource'
import { getApiScriptByApiId, publishHistory } from '@/api/api-scripts'
import common_data from '@/views/common/index'

export default {
  components: {
    RequestPanel, ResponsePanel, EditorActions
  },
  data() {
    return {
      api_requestMode_map: common_data.api_requestMode_map,
      apiInfo: {
        apiID: null,
        requestMode: 0,
        apiAddress: '',
        name: '',
        description: "There is no comment, Click 'info' icon to add comment"
      },
      datasourceList: {},
      apiScript: {
        id: null,
        apiId: null,
        datasourceCode: '',
        apiScriptType: 'SQL',
        apiScript: '-- a new Query.\nselect * from a limit 1;',
        apiScriptStatus: null,
        apiRequestHeader: [],
        apiRequestBody: '{"message":"Hello DataQL."}',
        apiResponseHeader: [],
        apiResponseBody: '"empty."',
        apiResponseFormat: '{"returnCode":"@returnCode","message":"@message","results":"@results","dataSize":"@dataSize"}',
        apiOption: { ...this.defaultOption },
        customResultStructure: 0
      },

      editerActions: {
        newMode: false,
        disablePublish: true
      },
      showComment: false,
      apiBaseUrl: apiBaseUrl('/'),
      panelPercentVertical: 50,
      panelPercentHorizontal: 50,
      panelHeight: '100%'
    }
  },
  watch: {
    'apiScript.apiRequestHeader': {
      handler(val, oldVal) {
        this.handleCommentOnchange()
      },
      deep: true
    },
    'apiScript.apiRequestBody': {
      handler(val, oldVal) {
        this.handleCommentOnchange()
      }
    },
    'apiScript.apiOption': {
      handler(val, oldVal) {
        this.handleCommentOnchange()
      },
      deep: true
    }
  },
  mounted() {
    this.apiInfo.apiID = this.$route.params.id
    this.editerActions.newMode = false
    this.showComment = false
    this.loadApiDetail()
    this.loadDataSource()
    this.initMonacoEditor()
    this.layoutMonacoEditor()
    this._resize = () => {
      return (() => {
        this.layoutMonacoEditor()
      })()
    }
    window.addEventListener('resize', this._resize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this._resize)
  },
  methods: {
    // 页面大小调整
    layoutMonacoEditor() {
      this.panelHeight = document.documentElement.clientHeight - 88
      const monacoEditorWidth = ((document.documentElement.clientWidth - 210) * (this.panelPercentVertical / 100))
      this.monacoEditor.layout({
        height: this.panelHeight,
        width: monacoEditorWidth
      })
      //
      const dataNum = this.panelPercentHorizontal / 100
      const heightSize = document.documentElement.clientHeight - 88
      const widthSize = document.documentElement.clientWidth - monacoEditorWidth - 2
      this.$refs.editerRequestPanel.doLayout(heightSize * dataNum, widthSize)
      this.$refs.editerResponsePanel.doLayout(heightSize * (1 - dataNum), widthSize)
    },
    handleVerticalSplitResize(data) {
      this.panelPercentVertical = data
      this.layoutMonacoEditor()
    },
    handleHorizontalSplitResize(data) {
      this.panelPercentHorizontal = data
      this.layoutMonacoEditor()
    },
    //
    // 显示隐藏Comment
    handleShowComment() {
      this.showComment = !this.showComment
    },
    handleCommentOnchange() {
      // console.log('handleCommentOnchange -> apiInfo.editorSubmitted = false');
    },
    //
    // 初始化编辑器
    initMonacoEditor() {
      loadMonacoEditorSelfTheme()
      this.monacoEditor = defineMonacoEditorFoo(this.$refs.container, {
        value: this.apiScript.apiScript,
        language: 'sql',
        theme: 'selfTheme'
      })
      this.monacoEditor.updateOptions({ contextmenu: true })
      //
      // let contextmenu = this.monacoEditor.getContribution('editor.contrib.contextmenu')
      // let actions = this.monacoEditor.getActions()
      const self = this
      this.monacoEditor.onDidChangeModelContent(function(event) { // 编辑器内容changge事件
        self.apiScript.apiScript = self.monacoEditor.getValue()
      })
    //   // 自定义键盘事件
    //   self.monacoEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function () {
    //     self.$emit('onCommit', self.monacoEditor.getValue(), self.monacoEditor)
    //   })
    //   self.monacoEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Alt | monaco.KeyCode.KEY_S, function () {
    //     // 自定义快捷操作
    //   })
    },
    // 加载Api的基本信息
    loadApiDetail() {
      const self = this
      getApiById(self.apiInfo.apiID).then(response => {
        // 加载 API 信息
        const data = response.data
        self.apiInfo.requestMode = data.openApiDetailsRequestModel.requestMode
        self.apiInfo.apiAddress = data.openApiDetailsRequestModel.apiAddress
        self.apiInfo.description = data.description
        self.apiInfo.name = data.name
        self.apiScript.apiId = self.apiInfo.apiID
        // 查询 脚本信息
        getApiScriptByApiId(self.apiInfo.apiID).then(response => {
          const apiScriptData = response.data
          if (apiScriptData != null) {
            self.apiScript.id = apiScriptData.id
            self.apiScript.datasourceCode = apiScriptData.datasourceCode
            self.apiScript.apiScriptType = apiScriptData.apiScriptType
            self.apiScript.apiScript = apiScriptData.apiScript
            self.apiScript.apiScriptStatus = apiScriptData.apiScriptStatus
            self.apiScript.apiRequestHeader = apiScriptData.apiRequestHeader
            self.apiScript.apiRequestBody = JSON.stringify(apiScriptData.apiRequestBody, null, 2)
            self.apiScript.apiResponseHeader = apiScriptData.apiResponseHeader
            self.apiScript.apiResponseBody = apiScriptData.apiResponseBody
            self.apiScript.apiResponseFormat = JSON.stringify(apiScriptData.apiResponseFormat, null, 2)
            self.apiScript.customResultStructure = apiScriptData.customResultStructure
            self.apiScript.apiOption = {
              ...this.defaultOption,
              ...apiScriptData.optionData
            }
          }
          self.loadEditorMode()
          self.monacoEditor.setValue(self.apiScript.apiScript)
          self.editerActions.disablePublish = true
          self.doNextTickUpdate()
        })
      })
    },
    // 刷新编辑器模式
    loadEditorMode() {
      this.editerActions.disablePublish = false
      // console.log('loadApiDetail -> editerActions.disablePublish = true');
      if (this.apiScript.apiScriptType.toLowerCase() === 'javascript') {
        this.monacoEditor.updateOptions({ language: 'javascript' })
        if (this.editerActions.newMode && this.monacoEditor.getValue().trim() === 'return 1') {
          this.monacoEditor.setValue('// a new Query.\nreturn 1')
        }
      }
      if (this.apiScript.apiScriptType.toLowerCase() === 'groovy') {
        this.monacoEditor.updateOptions({ language: 'groovy' })
        if (this.editerActions.newMode && this.monacoEditor.getValue().trim() === 'return 1') {
          this.monacoEditor.setValue('// a new Query.\nreturn 1')
        }
      }
      if (this.apiScript.apiScriptType.toLowerCase() === 'sql') {
        this.monacoEditor.updateOptions({ language: 'sql' })
        if (this.editerActions.newMode && this.monacoEditor.getValue().trim() === '-- a new Query.\nselect * from a limit 1;') {
          this.monacoEditor.setValue('-- a new Query.\nselect * from a limit 1;')
        }
      }
    },
    // 下一个周期更新页面
    doNextTickUpdate() {
      const self = this
      self.$nextTick(function() {
        self.$refs.editerRequestPanel.doUpdate()
        self.$refs.editerResponsePanel.doUpdate()
        self.$refs.editerActionsPanel.doUpdate()
      })
    },
    //
    onAfterSave(spiStatus, scriptId) {
      this.apiScript.id = scriptId
      this.loadApiDetail()
    },
    onSmokeTest(resultValue) {
      this.onExecute(resultValue)
      this.editerActions.disablePublish = false
      this.doNextTickUpdate()
      // console.log('loadApiDetail -> editerActions.disablePublish = false');
    },
    onExecute(resultValue) {
    //   this.responseType = dataTypeMode
    //   if (dataTypeMode === 'json') {
    //     this.apiScript.apiRequestBody = JSON.stringify(resultValue, null, 2)
    //   } else {
    //     this.apiScript.apiRequestBody = resultValue
    //   }
      this.apiScript.apiResponseBody = JSON.stringify(resultValue, null, 2)
      this.doNextTickUpdate()
    },
    onRecover(historyId) {
      const self = this
      publishHistory(historyId).then(response => {
        const apiScriptData = response.data
        self.apiScript.id = apiScriptData.id
        self.apiScript.datasourceCode = apiScriptData.datasourceCode
        self.apiScript.apiScriptType = apiScriptData.apiScriptType
        self.apiScript.apiScript = apiScriptData.apiScript
        self.apiScript.apiScriptStatus = apiScriptData.apiScriptStatus
        self.apiScript.apiRequestHeader = apiScriptData.apiRequestHeader
        self.apiScript.apiRequestBody = apiScriptData.apiRequestBody
        self.apiScript.apiResponseHeader = apiScriptData.apiResponseHeader
        self.apiScript.apiResponseBody = apiScriptData.apiResponseBody
        self.apiScript.apiResponseFormat = apiScriptData.apiResponseFormat
        self.apiScript.customResultStructure = apiScriptData.customResultStructure
        self.apiScript.apiOption = {
          ...this.defaultOption,
          ...apiScriptData.optionData
        }
        self.loadEditorMode()
        self.monacoEditor.setValue(self.apiScript.apiScript)
        self.editerActions.disablePublish = true
        self.doNextTickUpdate()
      })
    },
    // onDelete(apiId) {
    //   const self = this
    //   request(ApiUrl.deleteApi + '?id=' + apiId, {
    //     'method': 'POST',
    //     'data': {
    //       'id': apiId
    //     }
    //   }, response => {
    //     if (response.data.result) {
    //       self.$message({ message: 'Api Delete finish.', type: 'success' })
    //       this.$router.push('/')
    //     } else {
    //       errorBox('result is false.')
    //     }
    //   })
    // },
    loadDataSource() {
      listALL().then(response => {
        this.datasourceList = response.data
      })
    }
  }
}
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    .monacoEditorHeader {
        overflow-x: hidden;
        overflow-y: hidden;
        padding: 5px;
    }

    .comment {
        width: 50%;
        position: absolute;
        z-index: 1000;
    }

    .el-radio {
        margin-right: 1px;
        width: 80px;
    }

    .el-radio--mini.is-bordered {
        padding: 3px 10px 0 5px;
        height: 25px;
    }

    .el-input-group__append, .el-input-group__prepend {
        padding: 0 13px !important;
    }

    .el-radio.is-bordered + .el-radio.is-bordered {
        margin-left: 1px;
    }

    .input-with-select .el-input-group__prepend {
        cursor: copy !important;
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
