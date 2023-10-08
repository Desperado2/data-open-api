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
      <el-table-column label="API名称">
        <template slot-scope="scope">
          {{ scope.row.apiName }}
        </template>
      </el-table-column>
      <el-table-column label="订阅状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="String(scope.row.status)">{{ scope.row.status | statusFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="API状态" width="150" align="center">
        <template slot-scope="scope">
          <el-tag :type="String(scope.row.apiStatus)">{{ scope.row.apiStatus | apiStatusFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="申请时间" width="200">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="330" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button size="mini" type="danger" @click="handleModifyStatus(row,'published')">
            取消申请
          </el-button>
          <el-button v-if="false" size="mini" type="primary" @click="handleDelete(row,$index)">
            统计数据
          </el-button>
          <el-button v-if="row.status=='2'" size="mini" type="primary" @click="handleDelete(row,$index)">
            查看不通过原因
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="pagination.total>0" style="float:right" :total="pagination.total" :page.sync="queryBody.current" :limit.sync="queryBody.pageSize" @pagination="fetchData" />

  </div>
</template>

<script>
import { subscribeList } from '@/api/subscribe'
import Pagination from '@/components/Pagination'

export default {
  components: { Pagination },
  filters: {
    apiStatusFilter(status) {
      const statusMap = {
        7: '全上架',
        6: '上架测试和预发',
        5: '上架测试和正式',
        4: '只上架测试',
        3: '上架预发布和正式',
        2: '只上架预发布',
        1: '只上架正式',
        0: '全下架'
      }
      return statusMap[status]
    },
    statusFilter(status) {
      const statusMap = {
        1: '正常',
        2: '拒绝通过',
        3: '禁用',
        0: '待审核'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      pagination: {
        total: 0,
        startNumber: 0,
        totalPage: 1
      },
      keyAndSecretList: null,
      listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: '+id'
      },
      listLoading: true,
      queryBody: {
        status: '',
        current: 1,
        pageSize: 20
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      subscribeList(this.queryBody).then(response => {
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
