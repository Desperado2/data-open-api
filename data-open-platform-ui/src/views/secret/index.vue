<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="APPKEY">
        <template slot-scope="scope">
          {{ scope.row.appKey }}
          <el-tag class="copy-tag" type="primary" @click="clipboardCopy(scope.row.appKey, $event)">复制</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status">{{ scope.row.status | statusFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="创建时间" width="200">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="更新时间" width="200">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-popconfirm
            title="确定重置吗？"
            style="margin-right:10px"
            @onConfirm="resetKeyAndSecret"
          >
            <el-button slot="reference" type="primary" size="mini">重置</el-button>
          </el-popconfirm>
          <el-button v-if="row.status=='1'" size="mini" type="success" @click="getAppSecret(row.appKey)">
            查看APPSECRET
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      title="APPSECRET"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <span>APPSECRET: {{ appSecret }}</span>
      <el-tag class="copy-tag" type="primary" @click="clipboardCopy(appSecret, $event)">复制</el-tag>
    </el-dialog>

  </div>
</template>

<script>
import { listByUser, selectSecretByKey, reset } from '@/api/secret'
import clip from '@/utils/clipboard'

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: '正常',
        2: '未激活',
        0: '禁用'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      appSecret: null,
      listLoading: true,
      dialogVisible: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      listByUser().then(response => {
        this.list = response.data
        this.pagination = response.data.pagination
        this.listLoading = false
      }).finally(() => {
        this.listLoading = false
      })
    },
    clipboardCopy(text, event) {
      clip(text, event)
    },
    getAppSecret(appkey) {
      selectSecretByKey(appkey).then(response => {
        this.appSecret = response.data
        this.dialogVisible = true
      })
    },
    resetKeyAndSecret() {
      reset().then(response => {
        this.$message({
          message: '重置成功',
          type: 'success'
        })
        this.fetchData()
      })
    },
    handleClose() {
      this.appSecret = null
      this.dialogVisible = false
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
