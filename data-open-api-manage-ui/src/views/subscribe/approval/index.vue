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
      <el-table-column label="API名称" width="200">
        <template slot-scope="scope">
          {{ scope.row.apiName }}
        </template>
      </el-table-column>
      <el-table-column label="申请项目" width="100">
        <template slot-scope="scope">
          {{ scope.row.username }}
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
      <el-table-column align="center" label="拒绝原因">
        <template slot-scope="scope">
          <span>{{ scope.row.refuseMessage }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="申请时间" width="200">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="{row}">
          <el-button v-if="row.status=='0'" size="mini" type="danger" @click="toApproval(row)">
            审核
          </el-button>
          <el-button v-if="row.status=='1'" size="mini" type="primary" @click="toDisable(row)">
            禁用
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="pagination.total>0" style="float:right" :total="pagination.total" :page.sync="queryBody.current" :limit.sync="queryBody.pageSize" @pagination="fetchData" />

    <el-dialog title="审核" :visible.sync="approvalShow" :close-on-click-modal="false">
      <el-form ref="dataForm" :model="form" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
        <el-form-item
          label="
            状态"
          prop="status"
        >
          <el-select v-model="form.status" placeholder="请选择">
            <el-option
              v-for="item in api_subscribe_status_select_map"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="描述" prop="code">
          <el-input
            v-model="form.refuseMessage"
            autocomplete="off"
            :rows="2"
            type="textarea"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="approvalShow = false, form.subscribeId=null">取 消</el-button>
        <el-button type="primary" @click="doApproval">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { subscribeAllList, approval, disable } from '@/api/subscribe'
import Pagination from '@/components/Pagination'
import common_data from '@/views/common/index'
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
      approvalShow: false,
      api_subscribe_status_select_map: common_data.api_subscribe_status_select_map,
      pagination: {
        total: 0,
        startNumber: 0,
        totalPage: 1
      },
      form: {
        subscribeId: null,
        status: 1,
        refuseMessage: ''
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
      subscribeAllList(this.queryBody).then(response => {
        this.list = response.data.list
        this.pagination = response.data.pagination
        this.listLoading = false
      })
    },
    toApproval(row) {
      this.form.subscribeId = row.subscribeId
      this.approvalShow = true
    },
    doApproval(row) {
      if (this.form.status !== 1 && this.form.refuseMessage.trim() === '') {
        this.$message.error('拒绝之后，必须填写原因')
      } else {
        this.listLoading = true
        approval(this.form).then(response => {
          this.listLoading = false
          this.approvalShow = false
          this.queryBody.current = 1
          this.fetchData()
        }).finally(() => {
          this.listLoading = false
          this.approvalShow = false
        })
      }
    },
    toDisable(row) {
      this.listLoading = true
      this.form.subscribeId = row.subscribeId
      this.form.status = 4
      disable(this.form).then(response => {
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
