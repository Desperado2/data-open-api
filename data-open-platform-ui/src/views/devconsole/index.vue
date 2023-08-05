<template>
  <div>
    <div class="monacoEditorHeader">
      <div style="width: 20%; margin-top: 2px; display: inline-flex;">
        <!-- <el-select v-model="apiInfo.requestMode" placeholder="Choose" style="width: 120px;padding-right: 5px;" size="mini" disabled>
          <el-option
            v-for="item in api_requestMode_map"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select> -->
        <el-button size="mini" type="primary" style="margin-right: 5px;" @click.native="backApiList">
          返回
        </el-button>
        <el-select v-model="apiScript.apiRunEnvironment" placeholder="请选择运行环境" style="width: 200px;padding-right: 5px;" size="mini">
          <el-option
            v-for="item in api_run_environment_map"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <!-- <el-select v-model="apiScript.preDatasourceCode" placeholder="请选择预发数据源" style="width: 200px;padding-right: 5px;" size="mini" :disabled="canEdit">
          <el-option
            v-for="item in datasourceList"
            :key="item.code"
            :label="item.name"
            :value="item.code"
          />
        </el-select>
        <el-select v-model="apiScript.prodDatasourceCode" placeholder="请选择正式数据源" style="width: 200px;padding-right: 5px;" size="mini" :disabled="canEdit">
          <el-option
            v-for="item in datasourceList"
            :key="item.code"
            :label="item.name"
            :value="item.code"
          />
        </el-select> -->
        <!-- <el-tooltip class="item" effect="dark" placement="bottom" :content="apiInfo.description" :disabled="showComment">
          <el-input v-model="apiInfo.apiAddress" placeholder="the path to access this Api" class="input-with-select" size="mini" :disabled="!editerActions.newMode">
            <el-button slot="append" icon="el-icon-info" @click.native="handleShowComment" />
          </el-input>
        </el-tooltip> -->
      </div>
      <!-- <div v-if="showComment" class="comment">
        <el-input v-model="apiInfo.comment" placeholder="Api's comment." size="mini" @input="handleCommentOnchange">
          <template slot="prepend">Comment</template>
        </el-input>
      </div> -->
      <div style="display: inline-table;padding-left: 5px;">
        <el-radio-group v-model="apiScript.apiScriptType" size="mini" :disabled="!canEdit" @change="loadEditorMode">
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
      <div style="display: inline-table;padding-left: 5px;">
        <el-tooltip class="item" effect="dark" placement="bottom-end" content="格式化代码">
          <el-button class="z-index-top" size="mini" round :disabled="!canEdit" @click.native="codeScriptFormatter">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#iconformat" />
            </svg>
          </el-button>
        </el-tooltip>
      </div>
      <div style="float: right;">
        <EditorActions
          ref="editerActionsPanel"
          :api-info="apiInfo"
          :api-script="apiScript"
          :action-status="editerActions"
          :can-edit="canEdit"
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
          <editor
            ref="container"
            v-model="apiScript.apiScript"
            :lang="aceConfig.selectLang"
            :theme="aceConfig.selectTheme"
            :options="aceConfig.options"
            :height="aceConfig.height"
            :width="aceConfig.width"
            @init="initMonacoEditor"
          />

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
                :can-edit="canEdit"
                :api-script="apiScript"
                :datasource-list="datasourceList"
                :option-info="apiScript.apiOption"
                @onOptionChange="(data)=> { apiScript.apiOption = data}"
                @onHeaderChange="(data)=> { apiScript.apiRequestHeader = data}"
                @onRequestBodyChange="(data)=> { apiScript.apiRequestBody = data}"
              />
            </template>
            <template slot="paneR">
              <ResponsePanel
                ref="editerResponsePanel"
                :response-body="apiScript.apiResponseBody"
                :response-format="apiScript.apiResponseFormat"
                :custom-response-format="apiScript.customResultStructure"
                :on-edit-page="canEdit"
                result-type="json"
                :option-info="apiScript.apiOption"
                @onCustomResponseFormatChange="(data) => {apiScript.customResultStructure = data}"
                @onResponseFormat="(data) => {apiScript.apiResponseFormat = data}"
                @onResponseBodyChange="(data)=> { apiScript.apiResponseBody = data}"
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
import Editor from 'vue2-ace-editor'
import { getApiById } from '@/api/api'
import { apiBaseUrl } from '@/utils/api-const'
import { listALL } from '@/api/datasource'
import { getApiScriptByApiId, publishHistory } from '@/api/api-scripts'
import common_data from '@/views/common/index'
import { custom_function_list } from '@/utils/ace-custom-utils'
import 'vue2-ace-editor/node_modules/brace/ext/language_tools'
import 'vue2-ace-editor/node_modules/brace/mode/groovy'
import 'vue2-ace-editor/node_modules/brace/snippets/groovy'
import 'vue2-ace-editor/node_modules/brace/mode/sql'
import 'vue2-ace-editor/node_modules/brace/snippets/sql'
import 'vue2-ace-editor/node_modules/brace/mode/javascript'
import 'vue2-ace-editor/node_modules/brace/snippets/javascript'
import 'vue2-ace-editor/node_modules/brace/ext/searchbox'
import 'vue2-ace-editor/node_modules/brace/theme/monokai'
import beautify from 'js-beautify'
import { format } from 'sql-formatter'
export default {
  components: {
    RequestPanel, ResponsePanel, EditorActions, Editor
  },
  data() {
    return {
      initCustomToken: true,
      canEdit: true,
      aceConfig: {
        selectTheme: 'tomorrow', // 默认选择的主题
        selectLang: 'sql', // 默认选择的语言
        readOnly: false, // 是否只读
        height: '100%',
        width: '100%',
        options: {
          tabSize: 4, // tab默认大小
          showPrintMargin: false, // 去除编辑器里的竖线
          fontSize: 20, // 字体大小
          highlightActiveLine: true, // 高亮配置
          enableBasicAutocompletion: true, // 启用基本自动完成
          enableSnippets: true, // 启用代码段
          enableLiveAutocompletion: true // 启用实时自动完成
        }
      },
      api_requestMode_map: common_data.api_requestMode_map,
      api_run_environment_map: common_data.api_run_environment,
      apiInfo: {
        apiID: null,
        requestMode: 0,
        apiAddress: '',
        name: '',
        description: "There is no comment, Click 'info' icon to add comment"
      },
      datasourceList: [],
      apiScript: {
        id: null,
        apiId: null,
        apiRunEnvironment: 'test',
        testDatasourceCode: '',
        preDatasourceCode: '',
        prodDatasourceCode: '',
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
      panelHeight: '100%',
      keywords_list: custom_function_list
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
    this.canEdit = this.$route.params.canEdit
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
    codeScriptFormatter() {
      if (this.apiScript.apiScriptType.toLowerCase() === 'sql') {
        this.apiScript.apiScript = format(this.apiScript.apiScript)
      } else {
        this.apiScript.apiScript = beautify(this.apiScript.apiScript, {
          indent_size: 2, // 缩进两个空格
          space_in_empty_paren: true
        })
      }
    },
    // 页面大小调整
    layoutMonacoEditor() {
      this.panelHeight = document.documentElement.clientHeight - 140
      const monacoEditorWidth = ((document.documentElement.clientWidth - 210) * (this.panelPercentVertical / 100)) - 20
      this.aceConfig.height = this.panelHeight
      this.aceConfig.width = monacoEditorWidth
      this.monacoEditor.resize()
      const dataNum = this.panelPercentHorizontal / 100
      const heightSize = document.documentElement.clientHeight - 88
      const widthSize = document.documentElement.clientWidth - monacoEditorWidth - 20
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
      this.monacoEditor = this.$refs.container.editor
      this.monacoEditor.setReadOnly(!this.canEdit)
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
            self.apiScript.apiRunEnvironment = apiScriptData.apiRunEnvironment
            self.apiScript.testDatasourceCode = apiScriptData.testDatasourceCode
            self.apiScript.preDatasourceCode = apiScriptData.preDatasourceCode
            self.apiScript.prodDatasourceCode = apiScriptData.prodDatasourceCode
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
      if (this.apiScript.apiScriptType.toLowerCase() === 'javascript') {
        this.aceConfig.selectLang = 'javascript'
        this.initMonacoEditor()
        const that = this
        this.monacoEditor.completers.push({
          getCompletions: function(state, session, pos, prefix, callback) {
            if (prefix.length === 0) {
              callback(null, [])
              return
            }
            callback(null, that.keywords_list)
          }
        })
        if (this.initCustomToken) {
          const session = this.monacoEditor.session
          session.setMode('ace/mode/javascript', function() {
            const rules = session.$mode.$highlightRules.getRules()
            for (const stateName in rules) {
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'db',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'DbParamUtils',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'es',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'count',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'selectList',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'selectOne',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'search',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'bindValue(key, value)',
                caseInsensitive: true
              })
            }
            // force recreation of tokenizer
            session.$mode.$tokenizer = null
            session.bgTokenizer.setTokenizer(session.$mode.getTokenizer())
            // force re-highlight whole document
            session.bgTokenizer.start(0)
          })
          this.initCustomToken = !this.initCustomToken
        }
      }
      if (this.apiScript.apiScriptType.toLowerCase() === 'groovy') {
        this.aceConfig.selectLang = 'groovy'
        this.initMonacoEditor()
        const that = this
        this.monacoEditor.completers.push({
          getCompletions: function(state, session, pos, prefix, callback) {
            if (prefix.length === 0) {
              callback(null, [])
              return
            }
            console.log(that.keywords_list)
            callback(null, that.keywords_list)
          }
        })
        if (this.initCustomToken) {
          const session = this.monacoEditor.session
          session.setMode('ace/mode/groovy', function() {
            const rules = session.$mode.$highlightRules.getRules()
            for (const stateName in rules) {
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'db',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'DbParamUtils',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'es',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'count',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'selectList',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'selectOne',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'search',
                caseInsensitive: true
              })
              rules[stateName].unshift({
                token: 'keyword',
                regex: 'bindValue(key, value)',
                caseInsensitive: true
              })
            }
            // force recreation of tokenizer
            session.$mode.$tokenizer = null
            session.bgTokenizer.setTokenizer(session.$mode.getTokenizer())
            // force re-highlight whole document
            session.bgTokenizer.start(0)
          })
          this.initCustomToken = !this.initCustomToken
        }
      }
      if (this.apiScript.apiScriptType.toLowerCase() === 'sql') {
        this.aceConfig.selectLang = 'sql'
        this.initMonacoEditor()
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
    backApiList() {
      this.$router.push('/api-manage/api')
    },
    onRecover(historyId) {
      const self = this
      publishHistory(historyId).then(response => {
        const apiScriptData = response.data
        self.apiScript.id = apiScriptData.id
        self.apiScript.apiRunEnvironment = apiScriptData.apiRunEnvironment
        self.apiScript.testDatasourceCode = apiScriptData.testDatasourceCode
        self.apiScript.preDatasourceCode = apiScriptData.preDatasourceCode
        self.apiScript.prodDatasourceCode = apiScriptData.prodDatasourceCode
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
    overflow-x: hidden !important;
    overflow-y: hidden !important;
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
