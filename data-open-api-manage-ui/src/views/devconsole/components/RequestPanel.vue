<template>
  <div class="requestPanel">
    <div class="request-btns" style="height: 30px;">
      <el-button-group>
        <el-tooltip class="item" effect="dark" content="执行查询" placement="bottom-end">
          <el-button v-if="this.hideRunBtn === false" class="z-index-top" size="mini" round @click.native="triggerRun">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#iconexecute" />
            </svg>
          </el-button>
        </el-tooltip>
        <el-tooltip v-if="this.panelMode === 'req_parameters'" class="item" effect="dark" placement="bottom-end" content="格式化参数">
          <el-button class="z-index-top" size="mini" round @click.native="handleParametersFormatter">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#iconformat" />
            </svg>
          </el-button>
        </el-tooltip>
        <el-tooltip v-if="this.panelMode === 'req_headers'" class="item" effect="dark" placement="bottom-end" content="新增请求头">
          <el-button class="z-index-top" size="mini" round @click.native="handleHeaderAddNew">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#iconadd" />
            </svg>
          </el-button>
        </el-tooltip>
      </el-button-group>
    </div>
    <el-tabs v-model="panelMode" class="request-tabs" type="card">
      <el-tab-pane name="req_parameters" label="参数" lazy>
        <div ref="requestPanel" />
      </el-tab-pane>
      <el-tab-pane name="req_headers" label="请求头" lazy>
        <el-table ref="requestHeaderTable" :data="headerDataCopy" :height="headerPanelHeight" border empty-text="无数据">
          <el-table-column prop="checked" width="38" :resizable="false">
            <template slot="header">
              <el-checkbox v-model="headerSelectAllStatus" name="type" :indeterminate="headerSelectIndeterminateStatus" @change="handleHeaderCheckAllChange" />
            </template>
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.checked" name="type" @change="updateIndeterminate" />
            </template>
          </el-table-column>
          <el-table-column prop="name" label="Key" min-width="30%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.name" size="mini" placeholder="请求头的KEY" />
            </template>
          </el-table-column>
          <el-table-column prop="value" label="Value" :resizable="false">
            <template slot-scope="scope">
              <el-input v-model="scope.row.value" size="mini" placeholder="请求头的VALUE" />
            </template>
          </el-table-column>
          <el-table-column prop="name" width="38" :resizable="false">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" content="删除" placement="left">
                <el-button size="mini" type="danger" icon="el-icon-delete" circle @click.native="handleHeaderDelete(scope.row,scope.$index)" />
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane name="datasource" label="数据源" lazy>
        <el-row>
          <el-col :span="4"><div style="padding:10px;color:#606266;font-size: 12px;">数据源类型</div></el-col>
          <el-col :span="8"><div style="padding:10px;color:#606266;font-size: 12px;">数据源</div></el-col>
          <el-col :span="12"><div style="padding:10px;color:#606266;font-size: 12px;">数据源编号</div></el-col>
        </el-row>
        <el-row>
          <el-col :span="4"><div style="padding:10px;color:#606266;font-size: 12px;">测试数据源</div></el-col>
          <el-col :span="8">
            <el-select v-model="apiScript.testDatasourceCode" placeholder="请选择测试数据源" style="width: 150px;padding-right: 5px;" size="mini" :disabled="!canEdit">
              <el-option
                v-for="item in datasourceList"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-col>
          <el-col :span="12" style="display:flex;">
            <div style="padding:10px;color:#606266;font-size: 12px;" v-text="apiScript.testDatasourceCode" />
            <el-tag class="copy-tag" type="primary" @click="clipboardCopy(apiScript.testDatasourceCode, $event)">复制</el-tag>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="4"><div style="padding:10px;color:#606266;font-size: 12px;">预发数据源</div></el-col>
          <el-col :span="8">
            <el-select v-model="apiScript.preDatasourceCode" placeholder="请选择预发数据源" style="width: 150px;padding-right: 5px;" size="mini" :disabled="!canEdit">
              <el-option
                v-for="item in datasourceList"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-col>
          <el-col :span="12" style="display:flex;">
            <div style="padding:10px;color:#606266;font-size: 12px;" v-text="apiScript.preDatasourceCode" />
            <el-tag class="copy-tag" type="primary" @click="clipboardCopy(apiScript.preDatasourceCode, $event)">复制</el-tag>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="4"><div style="padding:10px;color:#606266;font-size: 12px;">正式数据源</div></el-col>
          <el-col :span="8">
            <el-select v-model="apiScript.prodDatasourceCode" placeholder="请选择正式数据源" style="width: 150px;padding-right: 5px;" size="mini" :disabled="!canEdit">
              <el-option
                v-for="item in datasourceList"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-col>
          <el-col :span="12" style="display:flex;">
            <div style="padding:10px;color:#606266;font-size: 12px;" v-text="apiScript.prodDatasourceCode" />
            <el-tag class="copy-tag" type="primary" @click="clipboardCopy(apiScript.prodDatasourceCode, $event)">复制</el-tag>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import { defineMonacoEditorFoo } from '@/utils/editorUtils'
import clip from '@/utils/clipboard'

export default {
  props: {
    optionInfo: {
      type: Object,
      default: function() {
        return {}
      }
    },
    apiInfo: {
      type: Object,
      default: function() {
        return {
          apiID: -1,
          requestMode: 'POST',
          apiAddress: '',
          description: ''
        }
      }
    },
    datasourceList: {
      type: Object,
      default: function() {
        return null
      }
    },
    canEdit: {
      type: Boolean,
      default: true
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
        }
      }
    },
    requestBody: {
      type: String,
      default: function() {
        return '{}'
      }
    },
    headerData: {
      type: Array,
      default: function() {
        return []
      }
    },
    hideRunBtn: {
      type: Boolean,
      default: function() {
        return false
      }
    }
  },
  data() {
    return {
      optionInfoCopy: {},
      requestBodyCopy: '',
      headerDataCopy: [],
      //
      headerPanelHeight: '100%',
      panelMode: 'req_parameters',
      headerSelectIndeterminateStatus: false,
      headerSelectAllStatus: false
    }
  },
  watch: {
    'optionInfoCopy': {
      handler(val, oldVal) {
        this.$emit('onOptionChange', this.optionInfoCopy)
      },
      deep: true
    },
    'headerDataCopy': {
      handler(val, oldVal) {
        this.updateIndeterminate()
        this.$emit('onHeaderChange', this.headerDataCopy)
      },
      deep: true
    },
    'requestBodyCopy': {
      handler(val, oldVal) {
        this.$emit('onRequestBodyChange', this.requestBodyCopy)
      }
    }
  },
  mounted() {
    const self = this
    this.monacoEditor = defineMonacoEditorFoo(this.$refs.requestPanel, {})
    this.monacoEditor.onDidChangeModelContent(function(event) { // 编辑器内容changge事件
      self.requestBodyCopy = self.monacoEditor.getValue()
    })
    this.doUpdate()
  },
  methods: {
    clipboardCopy(text, event) {
      clip(text, event)
    },
    // Header 点击了全选
    handleHeaderCheckAllChange(s) {
      for (let i = 0; i < this.headerDataCopy.length; i++) {
        this.headerDataCopy[i].checked = s
      }
      this.updateIndeterminate()
    },
    // 请求参数格式化
    handleParametersFormatter() {
      try {
        this.requestBodyCopy = JSON.stringify(JSON.parse(this.requestBodyCopy), null, 2)
        this.monacoEditor.setValue(this.requestBodyCopy)
      } catch (e) {
        this.$message.error('参数格式错误 : ' + e)
      }
    },
    // Header 添加一个新的
    handleHeaderAddNew() {
      this.headerDataCopy.push({ checked: true, name: '', value: '' })
      this.updateIndeterminate()
    },
    // Header 删除
    handleHeaderDelete(row, rowIndex) {
      const newArrays = []
      for (let i = 0; i < this.headerDataCopy.length; i++) {
        if (i !== rowIndex) {
          newArrays.push(this.headerDataCopy[i])
        }
      }
      this.headerDataCopy = newArrays
      this.updateIndeterminate()
    },
    updateIndeterminate() {
      let checkedCount = 0
      for (let i = 0; i < this.headerDataCopy.length; i++) {
        if (this.headerDataCopy[i].checked) {
          checkedCount++
        }
      }
      this.headerSelectAllStatus = checkedCount === this.headerDataCopy.length
      this.headerSelectIndeterminateStatus = checkedCount > 0 && checkedCount !== this.headerDataCopy.length
    },
    // 触发执行
    triggerRun() {
      this.$emit('onRun')
    },
    // 执行布局
    doLayout(height, width) {
      this.headerPanelHeight = (height - 50) + 'px'
      this.monacoEditor.layout({ height: (height - 50), width: width })
    },
    doUpdate() {
      this.optionInfoCopy = { ...this.defaultOption, ...this.optionInfo }
      this.requestBodyCopy = this.requestBody
      this.headerDataCopy = this.headerData
      this.monacoEditor.setValue(this.requestBodyCopy)
    }
  }
}
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

.requestPanel {
}

.request-tabs {
  top: -33px;
  position: relative;
}

.request-btns {
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

.copy-tag{
  cursor: pointer;
  margin-left:10px
}
</style>
