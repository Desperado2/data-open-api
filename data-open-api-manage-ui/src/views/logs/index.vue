<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24"><div class="filter-container">
        <el-input v-model="queryBody.name" placeholder="API名称或用户名" style="width: 200px;" class="filter-item" />
        <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
          搜索
        </el-button>
      </div>
      </el-col>
    </el-row>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="请求URL">
        <template slot-scope="scope">
          {{ scope.row.requestUrl }}
        </template>
      </el-table-column>
      <el-table-column label="请求环境">
        <template slot-scope="scope">
          {{ scope.row.requestEnvironment }}
        </template>
      </el-table-column>
      <el-table-column label="API名称">
        <template slot-scope="scope">
          {{ scope.row.apiName }}
        </template>
      </el-table-column>
      <el-table-column label="项目名称">
        <template slot-scope="scope">
          {{ scope.row.username }}
        </template>
      </el-table-column>
      <el-table-column label="请求状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status ==0 ? 'primary': 'danger'">{{ scope.row.status | statusFilter }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="响应时长" width="100">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ (scope.row.responseTime - scope.row.requestTime)/1000 }}s</span>
        </template>
      </el-table-column>
      <el-table-column label="返回数据量">
        <template slot-scope="scope">
          {{ scope.row.resultCount }}
        </template>
      </el-table-column>
      <el-table-column label="提示信息" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          {{ scope.row.errorMsg }}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="请求时间" width="200">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleRequestParam(row)">
            查看请求参数
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="pagination.total>0" style="float:right" :total="pagination.total" :page.sync="queryBody.current" :limit.sync="queryBody.pageSize" @pagination="fetchData" />
    <el-dialog title="请求参数" :visible.sync="updateFormVisible" :close-on-click-modal="false">
      <pre style="width:100%;border:1px solid #ccc" v-html="highlight(apiRequestParam)" />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateFormVisible = false">关  闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import common_data from '@/views/common/index'
import { listALL } from '@/api/log-api'
import Pagination from '@/components/Pagination'
import waves from '@/directive/waves' // waves directive
import { syntaxHighlight } from '@/utils/json'

export default {
  components: {
    Pagination
  },
  directives: { waves },
  filters: {
    statusFilter(status) {
      return common_data.request_log_status_map[status]
    }
  },
  data() {
    return {
      pagination: {
        total: 0,
        startNumber: 0,
        totalPage: 1
      },
      list: null,
      listLoading: true,
      queryBody: {
        name: undefined,
        apiId: null,
        pageSize: 10,
        current: 1
      },
      apiRequestParam: '',
      updateFormVisible: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    handleFilter() {
      this.queryBody.current = 1
      this.fetchData()
    },
    highlight(str) {
      try {
        return syntaxHighlight(JSON.parse(str))
      } catch (err) {
        return str
      }
    },
    handleRequestParam(row) {
      this.apiRequestParam = row.requestParams
      this.updateFormVisible = true
    },
    fetchData() {
      this.listLoading = true
      listALL(this.queryBody).then(response => {
        this.list = response.data.list
        this.pagination = response.data.pagination
        this.listLoading = false
      }).finally(() => {
        this.listLoading = false
      })
    }
  }
}
</script>
