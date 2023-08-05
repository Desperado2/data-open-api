<template>
  <div class="app-container">

    <div class="left">
      <div v-for="typeData in typeList" :key="typeData.type">
        <p class="type-text" v-text="typeData.type" />
        <div v-for="udfData in typeData.udfList" :key="udfData.id">
          <p :class="{active:activeId===udfData.id}" class="udf-text" @click="getUdfInfo(udfData.id)" v-text="udfData.name" />
        </div>
      </div>
    </div>
    <div class="right">
      <div class="div-class" style="font-size:20px" v-text="udfInfo.name" />
      <div class="div-class">
        <p>函数定义: {{ udfInfo.definition }} </p>
      </div>
      <div class="div-class">
        <p>参数定义:</p>
        <div v-for="param in udfInfo.parameter" :key="param.name">
          <span>{{ param.name }}</span>
          <span>:&emsp;&emsp;类型:{{ param.type }}</span>
          <span>,&emsp;&emsp;{{ param.desc }}</span>
        </div>
      </div>
      <div class="div-class">
        <p>返回值:</p>
        <div v-if="udfInfo.returnValue">
          <span>{{ udfInfo.returnValue.name }}</span>
          <span>:&emsp;&emsp;类型:{{ udfInfo.returnValue.type }}</span>
          <span>,&emsp;&emsp;{{ udfInfo.returnValue.desc }}</span>
        </div>
        <div v-else>无</div>
      </div>
      <div class="div-class">
        <p>说明:</p>
        <p>{{ udfInfo.description }}</p>
      </div>
      <div class="div-class">
        <p>示例</p>
        <div class="example" v-html="udfInfo.example" />
      </div>
    </div>

  </div>
</template>

<script>
import { typeList, udfInfo } from '@/api/udf'
import { syntaxHighlight } from '@/utils/json'
export default {
  data() {
    return {
      typeList: [],
      udfInfo: {},
      activeId: null
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    highlight(str) {
      if (str === undefined) {
        return str
      }
      return syntaxHighlight(str)
    },
    fetchData() {
      this.listLoading = true
      typeList().then(response => {
        this.typeList = response.data
        this.listLoading = false
        this.getUdfInfo(this.typeList[0].udfList[0].id)
      }).finally(() => {
        this.listLoading = false
      })
    },
    getUdfInfo(id) {
      this.activeId = id
      udfInfo(id).then(response => {
        this.udfInfo = response.data
      })
    }
  }
}
</script>
<style lang="scss">
p {
    margin: 0 0 0;
}
.highlight {
    overflow-y: scroll;
    max-height: 300px;
    pre {
      display: flex;
      padding: 9.5px;
      margin: 0 0 10px;
      font-size: 13px;
      line-height: 1.42857143;
      color: #333;
      word-break: break-all;
      word-wrap: break-word;
      background: #fff;
      overflow: auto;
      white-space: pre;
  }
}
.type-text{
  font-size: 16px;
  color: black;
  padding-top: 8px;
}
.udf-text{
  font-size: 14px;
  color: darkgray;
  cursor: pointer;
}
.div-class{
  margin-bottom: 20px;
}
.example{
  border: solid 1px #eee;
  padding-top: 10px;
  padding-left: 10px;
  padding-bottom: 10px;
  font-size: 14px;
  background-color: #f6f8fa;
}
.active{
  color: #3ba0f1;
}
span{
  font-size: 14px;
}
.left{
  position: absolute;
  top: 20px;
  left: 30px;
  width: 230px;
  height: 90%;
  overflow: scroll;
}
.right{
  top:30px;
  margin-left: 300px;
}

</style>
