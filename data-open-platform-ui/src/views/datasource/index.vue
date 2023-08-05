<template>
  <div class="app-container">
    <el-row>
      <el-col :span="8"><div class="add-container">
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
          新增
        </el-button></div></el-col>
      <el-col :span="16"><div class="filter-container">
        <el-input v-model="queryBody.name" placeholder="名称" style="width: 200px;" class="filter-item" />
        <el-select v-model="queryBody.type" placeholder="请选择类型" style="width: 200px;" class="filter-item" clearable>
          <el-option
            v-for="item in supportList"
            :key="item.code"
            :label="item.name"
            :value="item.code"
          />
        </el-select>
        <el-select v-model="queryBody.enabled" placeholder="请选择状态" style="width: 200px;" class="filter-item" clearable>
          <el-option
            v-for="item in data_source_status_select_map"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
          搜索
        </el-button>
      </div></el-col>
    </el-row>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="名称">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="类型" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ typeFilter(scope.row.type) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否启用" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.enabled ==0 ? 'primary': 'danger'">{{ scope.row.enabled | statusFilter }}</el-tag>
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
      <el-table-column label="操作" align="center" width="260" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button size="mini" :type="row.enabled == '0' ? 'danger': 'primary'" @click="handleModifyStatus(row)">
            {{ row.enabled == '0'? ' 禁用': '启用' }}
          </el-button>
          <el-button size="mini" type="primary" @click="handleTestConnection(row)">
            测试连接
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="pagination.total>0" style="float:right" :total="pagination.total" :page.sync="queryBody.current" :limit.sync="queryBody.pageSize" @pagination="fetchData" />
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="updateFormVisible" :close-on-click-modal="false">
      <el-form ref="dataForm" :rules="rules" :model="form" label-position="left" label-width="80px">
        <div v-show="dataSourceAddActive == 1">
          <template v-for="(item, index) in supportList">
            <el-row v-if="index%4===0" :key="index" style="margin-left:20px">
              <el-col :span="6" style="display: flex;justify-content: left;">
                <div class="api-item" @click="selectDataSourceType(item)">
                  <img :src="require('@/assets/datasource/' + item.icon)" style="height:80%;width:100%">
                  <strong>{{ item.name }}</strong>
                </div>
              </el-col>
              <el-col v-if="supportList.length > index+1" :span="6" style="display: flex;justify-content: center;">
                <div class="api-item" @click="selectDataSourceType(supportList[index+1])">
                  <img :src="require('@/assets/datasource/' + supportList[index+1].icon)" style="height:80%;width:100%">
                  <strong>{{ supportList[index+1].name }}</strong>
                </div>
              </el-col>
              <el-col v-if="supportList.length > index+2" :span="6" style="display: flex;justify-content: end;">
                <div class="api-item" @click="selectDataSourceType(supportList[index+2])">
                  <img :src="require('@/assets/datasource/' + supportList[index+2].icon)" style="height:80%;width:100%">
                  <strong>{{ supportList[index+2].name }}</strong>
                </div>
              </el-col>
              <el-col v-if="supportList.length > index+3" :span="6" style="display: flex;justify-content: end;">
                <div class="api-item" @click="selectDataSourceType(supportList[index+3])">
                  <img :src="require('@/assets/datasource/' + supportList[index+3].icon)" style="height:80%;width:100%">
                  <strong>{{ supportList[index+3].name }}</strong>
                </div>
              </el-col>
            </el-row>
          </template>
        </div>
        <div v-show="dataSourceAddActive == 2">
          <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item label="名称" prop="name">
                <el-input v-model="form.name" autocomplete="off" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="类型" prop="type">
                <el-select v-model="form.type" style="width: 100%;" placeholder="请选择">
                  <el-option
                    v-for="item in supportList"
                    :key="item.code"
                    :label="item.name"
                    :value="item.code"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" autocomplete="off" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="密码" prop="password">
                <el-input v-model="form.password" type="password" autocomplete="off" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="20">
              <el-form-item label="连接URL" prop="url">
                <el-input v-model="form.url" autocomplete="off" :placeholder="selectDateSourceType.format" />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-button @click="testConnection">点击测试</el-button>
            </el-col>
          </el-row>
        </div>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateFormVisible = false">取 消</el-button>
        <el-button v-show="dataSourceAddActive == 2" type="primary" @click="dialogStatus==='create'?addDataSource():updateDataSource() ">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import common_data from '@/views/common/index'
import { list, supportList, updateStatus, addDatasource, updateDatasource, testConnection, testConnectionById, getDatasourceById } from '@/api/datasource'
import Pagination from '@/components/Pagination'
import waves from '@/directive/waves' // waves directive

export default {
  components: {
    Pagination
  },
  directives: { waves },
  filters: {
    statusFilter(status) {
      return common_data.data_source_status_map[status]
    }
  },
  data() {
    return {
      data_source_type_select_map: common_data.data_source_type_select_map,
      data_source_status_select_map: common_data.data_source_status_select_map,
      pagination: {
        total: 0,
        startNumber: 0,
        totalPage: 1
      },
      supportList: [],
      selectDateSourceType: { 'format': '' },
      dataSourceAddActive: 1,
      list: null,
      listLoading: true,
      queryBody: {
        type: undefined,
        name: undefined,
        enabled: undefined,
        pageSize: 10,
        current: 1
      },
      textMap: {
        update: '编辑',
        create: '新增'
      },
      form: {
        id: null,
        name: '',
        type: '',
        username: '',
        password: '',
        url: ''
      },
      dialogStatus: '',
      updateFormVisible: false,
      rules: {
        name: [{ required: true, pattern: '[^ \x22]+', message: '名称不能为空', trigger: 'change' }],
        type: [{ required: true, message: '类型不能为空', trigger: 'blur' }],
        username: [{ required: true, pattern: '[^ \x22]+', message: '用户名不能为空', trigger: 'change' }],
        password: [{ required: true, pattern: '[^ \x22]+', message: '密码不能为空', trigger: 'change' }],
        url: [{ required: true, pattern: '[^ \x22]+', message: 'URL不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.fetchSupportList()
  },
  methods: {
    typeFilter(type) {
      return this.supportList.filter(it => it.code === type)[0].name
    },
    handleUpdate(row) {
      getDatasourceById(row.id).then(response => {
        this.form.id = response.data.id
        this.form.name = response.data.name
        this.form.type = response.data.type
        this.form.username = response.data.username
        this.form.password = response.data.password
        this.form.url = response.data.url
        this.dialogStatus = 'update'
        this.updateFormVisible = true
        this.selectDateSourceType = { 'format': '' }
        this.dataSourceAddActive = 2
      })
    },
    handleCreate() {
      this.form.id = null
      this.form.type = 0
      this.form.name = null
      this.form.url = null
      this.form.username = null
      this.form.password = null
      this.dialogStatus = 'create'
      this.selectDateSourceType = { 'format': '' }
      this.dataSourceAddActive = 1
      this.updateFormVisible = true
    },
    selectDataSourceType(datasourceType) {
      this.selectDateSourceType = datasourceType
      this.dataSourceAddActive = 2
      this.form.type = datasourceType.code
    },
    addDataSource() {
      addDatasource(this.form).then(response => {
        this.$message({
          message: '新增成功',
          type: 'success'
        })
        this.queryBody.current = 1
        this.updateFormVisible = false
        this.fetchData()
      }).finally(() => {
        this.updateFormVisible = false
      })
    },
    fetchSupportList() {
      supportList().then(response => {
        this.supportList = response.data
        this.fetchData()
      })
    },
    updateDataSource() {
      updateDatasource(this.form).then(response => {
        this.$message({
          message: '更新成功',
          type: 'success'
        })
        this.queryBody.current = 1
        this.updateFormVisible = false
        this.fetchData()
      }).finally(() => {
        this.updateFormVisible = false
      })
    },
    testConnection() {
      testConnection(this.form).then(response => {
        this.$message({
          message: '连接成功',
          type: 'success'
        })
      })
    },
    handleFilter() {
      this.queryBody.current = 1
      this.fetchData()
    },
    fetchData() {
      this.listLoading = true
      list(this.queryBody).then(response => {
        this.list = response.data.list
        this.pagination = response.data.pagination
        this.listLoading = false
      }).finally(() => {
        this.listLoading = false
      })
    },
    handleModifyStatus(row) {
      let enabled = 0
      if (row.enabled === 0) {
        enabled = 1
      }
      updateStatus(row.id, enabled).then(response => {
        this.$message({
          message: '更新成功',
          type: 'success'
        })
        this.fetchData()
      })
    },
    handleTestConnection(row) {
      testConnectionById(row.id).then(response => {
        this.$message({
          message: '连接成功',
          type: 'success'
        })
      })
    }
  }
}
</script>
<style lang="scss">
.api-item{
    cursor: pointer;
    background-color: #fff;
    overflow: hidden;
    display: inline-block;
    margin: 12px 12px;
    padding: 10px 10px;
    border: 1px solid #ececec;
    width: 160px;
    height: 160px;
    text-align: center;
  strong {
    font-weight: normal;
    font-size: 16px;
    padding-bottom: 6px;
    display: block;
    color: #999999;
  }
  :hover {
      color: #1399f3 !important;
   }
}
</style>
