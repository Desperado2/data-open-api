<template>
  <div :loading="loading" class="app-container">
    <div class="editor-container">
      <json-editor ref="jsonEditor" v-model="signData" />
    </div>

    <el-button type="primary" style="margin-top:20px" @click="toSign">开始签名</el-button>
    <!-- <div style="height:40px; background:#ddd;margin-top:40px;line-height:40px ">签名: {{ signResult.sign }}
      <el-tag class="copy-tag" type="primary" @click="clipboardCopy(signResult.sign, $event)">复制</el-tag>
    </div>
    <div style="height:40px; background:#ddd;margin-top:20px;line-height:40px">签名字符串: {{ signResult.signStr }}</div> -->
    <table style="margin-top:40px;border:1px solid #fafafa99; width:100%;border-collapse: collapse;">
      <thead>
        <tr style="border:1px solid #fafafa99;">
          <td style="width: 150px;border:1px solid #fafafa99;height:30px; padding:10px">字段</td>
          <td style="border:1px solid #fafafa99;height:30px; padding:10px">值</td>
        </tr>
      </thead>
      <tbody>
        <tr style="border:1px solid #fafafa99;">
          <td style="width: 150px;border:1px solid #fafafa99;;height:52px; padding:10px">签名</td>
          <td style="border:1px solid #fafafa99;height:30px; padding:10px">{{ signResult.sign }}
            <el-tag v-if="signResult.sign" class="copy-tag" type="primary" @click="clipboardCopy(signResult.sign, $event)">复制</el-tag>
          </td>
        </tr>
        <tr style="border:1px solid #fafafa99;">
          <td style="width: 150px;border:1px solid #fafafa99;height:52px; padding:10px">签名字符串</td>
          <td style="border:1px solid #fafafa99;height:30px; padding:10px">{{ signResult.signStr }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
<script>
import { defineMonacoEditorFoo } from '@/utils/editorUtils'
import { sign } from '@/api/api-utils'
import JsonEditor from '@/components/JsonEditor'
import clip from '@/utils/clipboard'

export default {
  components: {
    JsonEditor
  },
  data() {
    return {
      loading: false,
      signData: '',
      signResult: {},
      panelActiveName: 'result_view',
      height: '300px'
    }
  },
  mounted() {
    const self = this
    this.monacoSignDataEditor = defineMonacoEditorFoo(this.$refs.signDataPanel, { language: 'javascript' })
    this.monacoSignDataEditor.onDidChangeModelContent(function(event) { // 编辑器内容changge事件
      self.signData = self.monacoSignDataEditor.getValue()
    })
    this.monacoSignDataEditor.layout({
      height: this.height,
      width: '100%'
    })
    this.doLayout(this.height, 100)
    this._resize = () => {
      return (() => {
        this.doLayout(this.height, 100)
      })()
    }
    this.doUpdate()
  },
  methods: {
    clipboardCopy(text, event) {
      clip(text, event)
    },
    toSign() {
      this.loading = true
      try {
        var data = JSON.parse(this.signData)
      } catch (err) {
        this.$message({
          message: '不是正确的JSON格式',
          type: 'error'
        })
        return
      }
      sign(data).then(response => {
        this.signResult = response.data
      }).finally(() => {
        this.loading = false
      })
    },
    doLayout(height, width) {
      this.$refs.signDataPanel.layout({ height: height, width: width })
    },
    doUpdate() {
      this.monacoSignDataEditor.setValue(JSON.parse(this.signData))
    }
  }
}
</script>
<style lang="scss">
  .copy-tag{
    cursor: pointer;
    margin-left:10px
  }
</style>
