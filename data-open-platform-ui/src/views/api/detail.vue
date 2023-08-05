<template>
  <el-drawer
    title="API详情"
    :visible.sync="drawer"
    :direction="direction"
    :before-close="handleClose"
    size="50%"
  >
    <div style="margin-left:30px; margin-bottom:30px; heigth:100%; overflow-y:auto">
      <div style="margin-top:0px">
        <span style="margin-top:30px;font-size:20px;font-weight:700">API基本信息</span>
      </div>
      <el-divider style="background-color:#999999!important; margin-top:3px!important" />
      <el-row style="margin-bottom:10px">
        <el-col :span="6"><div class="grid-content bg-purple">API名称: </div></el-col>
        <el-col :span="18"><div class="grid-content bg-purple-light">
          {{ apiData.name }}</div></el-col>
      </el-row>
      <el-row style="margin-bottom:10px">
        <el-col :span="6"><div class="grid-content bg-purple">API描述:</div></el-col>
        <el-col :span="18"><div class="grid-content bg-purple-light">{{ apiData.description }}</div></el-col>
      </el-row>
      <el-row style="margin-bottom:10px">
        <el-col :span="6"><div class="grid-content bg-purple">API分类:</div></el-col>
        <el-col :span="18"><div class="grid-content bg-purple-light">{{ apiData.classifyId }}</div></el-col>
      </el-row>
      <el-row v-if="apiData.openApplyStatus=== 1" style="margin-bottom:10px">
        <el-col :span="6"><div class="grid-content bg-purple">不开放申请原因:</div></el-col>
        <el-col :span="18"><div class="grid-content bg-purple-light">{{ apiData.notOpenApplyReason }}</div></el-col>
      </el-row>
      <el-row>
        <el-col :span="6"><div class="grid-content bg-purple">API图片:</div></el-col>
        <el-col :span="18"><div class="grid-content bg-purple-light">
          <img :src="apiData.imageUrl" style="width:80px">
        </div></el-col>
      </el-row>

      <div style="margin-top:30px">
        <span style="font-size:20px;font-weight:700">API详细信息</span>
      </div>

      <el-divider />
      <el-row style="margin-bottom:10px" v-if="apiData.openApiDetailsRequestModel">
        <el-col :span="6"><div class="grid-content bg-purple">API地址: </div></el-col>
        <el-col :span="18"><div class="grid-content bg-purple-light">{{ apiData.openApiDetailsRequestModel.apiAddress }}</div></el-col>
      </el-row>
      <el-row style="margin-bottom:10px" v-if="apiData.openApiDetailsRequestModel">
        <el-col :span="6"><div class="grid-content bg-purple">协议:</div></el-col>
        <el-col :span="18"><div class="grid-content bg-purple-light">{{ apiData.openApiDetailsRequestModel.protocol }}</div></el-col>
      </el-row>
      <el-row style="margin-bottom:10px" v-if="apiData.openApiDetailsRequestModel">
        <el-col :span="6"><div class="grid-content bg-purple">请求方式:</div></el-col>
        <el-col :span="18"><div class="grid-content bg-purple-light">{{ apiData.openApiDetailsRequestModel.requestMode }}</div></el-col>
      </el-row>
      <el-row style="margin-bottom:10px" v-if="apiData.openApiDetailsRequestModel">
        <el-col :span="6"><div class="grid-content bg-purple">返回格式:</div></el-col>
        <el-col :span="18"><div class="grid-content bg-purple-light">
          {{ apiData.openApiDetailsRequestModel.returnFormat }}
        </div></el-col>
      </el-row>
      <el-row style="margin-bottom:10px" v-if="apiData.openApiDetailsRequestModel">
        <el-col :span="6"><div class="grid-content bg-purple">API详细描述:</div></el-col>
        <el-col :span="18"><div class="grid-content bg-purple-light">
          {{ apiData.openApiDetailsRequestModel.detailDescription }}
        </div></el-col>
      </el-row>
      <el-row v-if="apiData.openApiDetailsRequestModel">
        <el-col :span="6"><div class="grid-content bg-purple">返回示例:</div></el-col>
        <el-col :span="18"><div class="grid-content bg-purple-light">
          <pre style="width:100%;border:1px solid #ccc" v-html="highlight(apiData.openApiDetailsRequestModel.returnExample)" />
        </div></el-col>
      </el-row>

      <div style="margin-top:30px">
        <span style="font-size:20px;font-weight:700">请求参数信息</span>
      </div>
      <el-divider />

      <table class="tbparm" v-if="apiData.openApiRequestParamsRequestModelList">
        <thead>
          <tr class="firstRow">
            <th style="width:100px">名称</th>
            <th style="width:80px">类型</th>
            <th style="width:50px">必填</th>
            <th style="width:300px">示例值</th>
            <th style="width:235px">说明</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="params in apiData.openApiRequestParamsRequestModelList" :key="params.name">
            <td>{{ params.name }}</td>
            <td>{{ params.type }}</td>
            <td>{{ params.required === 0 ? '否' : '是' }}</td>
            <td>{{ params.exampleValue }}</td>
            <td>{{ params.description }}</td>
          </tr>
          <tr v-if="apiData.openApiRequestParamsRequestModelList === null || apiData.openApiRequestParamsRequestModelList.length===0"><td colspan="5" style="text-align: center;">无请求参数</td></tr>
        </tbody>
      </table>

      <div style="margin-top:30px">
        <span style="font-size:20px;font-weight:700">响应参数信息</span>
      </div>

      <el-divider />
      <table class="tbparm" v-if="apiData.openApiResponseRequestModelList">
        <thead>
          <tr class="firstRow">
            <th style="width:100px">名称</th>
            <th style="width:80px">类型</th>
            <th style="width:300px">示例值</th>
            <th style="width:235px">说明</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="params in apiData.openApiResponseRequestModelList" :key="params.name">
            <td>{{ params.name }}</td>
            <td>{{ params.type }}</td>
            <td>{{ params.exampleValue }}</td>
            <td>{{ params.description }}</td>
          </tr>
          <tr v-if="apiData.openApiResponseRequestModelList === null || apiData.openApiResponseRequestModelList.length===0"><td colspan="4" style="text-align: center;">无响应参数</td></tr>
        </tbody>
      </table>

    </div>
  </el-drawer>
</template>

<script>
import { syntaxHighlight } from '@/utils/json'
export default {
  props: {
    apiData: {
      type: Object,
      required: true,
      default: undefined
    },
    drawer: {
      type: Boolean,
      required: false
    }
  },
  data() {
    return {
      direction: 'rtl'
    }
  },

  methods: {
    highlight(str) {
      return syntaxHighlight(JSON.parse(str))
    },
    handleClose(done) {
      done()
      this.$emit('close')
    }
  }
}
</script>
<style lang="scss">

.el-drawer.rtl{
    overflow: scroll;
}
/*2.隐藏滚动条，太丑了*/
.el-drawer__container ::-webkit-scrollbar{
    display: none;
}

.el-divider {
    background-color:#999999;
    margin-top:3px;
}

.tbparm{
    border-collapse: collapse;
    margin-top: 20px;
    width: 100%;
    display: table;
    border-color: grey;

    thead {
        display: table-header-group;
        vertical-align: middle;
        border-color: inherit;
    }

tr {
    border: 1px solid #cecece;
}
th {
    text-align: left;
    padding: 10px 0px;
    background: rgb(233, 233, 233);
    padding-left: 6px;
    font-weight: 600;
    border-color: rgb(206, 206, 206);
}
tbody {
    display: table-row-group;
    vertical-align: middle;
    border-color: inherit;
}
 tr {
    border: 1px solid #cecece;
}
td {
    background: rgb(255, 255, 255);
    border: 1px solid #cecece;
    padding: 10px 0px;
    padding-left: 6px;
    display: table-cell;
    vertical-align: inherit;
}
}
pre {
    white-space: pre-wrap;
    word-wrap: break-word;
    .string {
    color: green;
}
.key {
    color: red;
}
.number {
    color: darkorange;
}
}
.api-item{
background-color: #fff;
    overflow: hidden;
    display: inline-block;
    margin: 12px 0;
    padding: 10px 0px;
    border: 1px solid #ececec;
    width: 252px;
    height: 210px;
    text-align: center;
  strong{
    font-weight: normal;
    font-size: 16px;
    padding-bottom: 6px;
    display: block;
  }

  span{
    color: #999999;
  }

  img{
    width: 60px;
    height: 60px;
    margin: 14px 0;
  }

  p{
    margin-top: 32px;
  }
  small{
        margin: 0px 16px;
    font-size: 14px;
    color: #ccc;
  }

i{
  vertical-align: middle;
    margin-right: 0px;
    display: inline-block;
}
o{
      margin-left: 6px;
    vertical-align: middle;
}
}

</style>
