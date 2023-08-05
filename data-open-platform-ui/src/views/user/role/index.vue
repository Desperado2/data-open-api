<template>
  <div class="app-container">

    <el-row>
      <el-col :span="12"><div class="add-container">
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
          新增
        </el-button></div></el-col>
      <el-col :span="12"><div class="filter-container">
        <el-input v-model="queryBody.name" placeholder="角色名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
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
      <el-table-column label="编码" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.code }}</span>
        </template>
      </el-table-column>
      <el-table-column label="默认角色" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.defaultRole | defaultRoleFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="String(scope.row.status)">{{ scope.row.status | statusFilter }}</el-tag>
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
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left:10px;margin-right:10px"
            @onConfirm="deleteRole(row.id)"
          >
            <el-button slot="reference" type="danger" size="mini">删除</el-button>
          </el-popconfirm>
          <!-- <el-button v-if="row.status=='1'" size="mini" type="success" @click="handleModifyStatus(row,'published')">
            禁用
          </el-button>
          <el-button v-if="row.status=='0'" size="mini" type="danger" @click="handleDelete(row,$index)">
            启用
          </el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="pagination.total>0" style="float:right" :total="pagination.total" :page.sync="queryBody.current" :limit.sync="queryBody.pageSize" @pagination="fetchData" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="updateFormVisible" :close-on-click-modal="false">
      <el-form ref="dataForm" :rules="rules" :model="form" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" autocomplete="off" />
        </el-form-item>
        <el-form-item label="默认角色" prop="defaultRole">
          <el-radio v-model="form.defaultRole" :label="1">是</el-radio>
          <el-radio v-model="form.defaultRole" :label="0">否</el-radio>
        </el-form-item>
        <el-form-item label="角色状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择">
            <el-option
              v-for="item in role_status_map"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?addRole():updateRole() ">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { roleList, addRole, updateRole, deleteRole } from '@/api/role'
import Pagination from '@/components/Pagination'
import waves from '@/directive/waves' // waves directive
import common_data from '@/views/common/index'
export default {
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: '禁用',
        0: '正常'
      }
      return statusMap[status]
    },
    defaultRoleFilter(defaultRole) {
      return common_data.role_default_role[defaultRole]
    }
  },
  data() {
    return {
      role_status_map: common_data.role_status,
      list: null,
      dialogStatus: '',
      updateFormVisible: false,
      pagination: {
        total: 0,
        startNumber: 0,
        totalPage: 1
      },
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
        name: '',
        current: 1,
        pageSize: 20
      },
      textMap: {
        update: '编辑',
        create: '新增'
      },
      form: {
        id: null,
        name: '',
        code: '',
        status: '',
        defaultRole: ''
      },
      rules: {
        name: [{ required: true, pattern: '[^ \x22]+', message: '名称不能为空', trigger: 'change' }],
        code: [{ required: true, pattern: '[^ \x22]+', message: '编码不能为空', trigger: 'change' }],
        status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
        defaultRole: [{ required: true, message: '是否默认不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      roleList(this.queryBody).then(response => {
        this.list = response.data.list
        this.pagination = response.data.pagination
        this.listLoading = false
      }).finally(() => {
        this.listLoading = false
      })
    },
    handleUpdate(row) {
      this.form.id = row.id
      this.form.code = row.code
      this.form.name = row.name
      this.form.defaultRole = row.defaultRole
      this.form.status = row.status
      this.dialogStatus = 'update'
      this.updateFormVisible = true
    },
    handleCreate() {
      this.form.id = null
      this.form.code = null
      this.form.name = null
      this.form.defaultRole = 0
      this.form.status = 0
      this.dialogStatus = 'create'
      this.updateFormVisible = true
    },
    updateRole() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateRole(this.form).then(response => {
            this.$message({
              message: '更新成功',
              type: 'success'
            })
            this.updateFormVisible = false
            this.fetchData()
          }).finally(() => {
            this.updateFormVisible = false
          })
        }
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.fetchData()
    },
    addRole() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          addRole(this.form).then(response => {
            this.$message({
              message: '新增成功',
              type: 'success'
            })
            this.updateFormVisible = false
            this.fetchData()
          }).finally(() => {
            this.updateFormVisible = false
          })
        }
      })
    },
    deleteRole(id) {
      deleteRole(id).then(response => {
        this.$message({
          message: '删除成功',
          type: 'success'
        })
        this.fetchData()
      })
    }
  }
}
</script>
