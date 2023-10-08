<template>
  <div class="app-container">
    <el-row>
      <el-col :span="12"><div class="add-container">
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
          新增
        </el-button></div></el-col>
      <el-col :span="12"><div class="filter-container">
        <el-input v-model="queryBody.name" placeholder="分类名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
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
      highlight-current-row
    >
      <el-table-column label="名称" width="200">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="编码">
        <template slot-scope="scope">
          <span>{{ scope.row.code }}</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" width="400">
        <template slot-scope="scope">
          <span>{{ scope.row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="显示顺序" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.showIndex }}</span>
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
          <!-- <el-button size="mini" type="success" @click="handleModifyStatus(row,'published')">
            {{ row.status == '0'? ' 禁用': '启用' }}
          </el-button> -->
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left:10px;margin-right:10px"
            @onConfirm="deleteClassify(row.id)"
          >
            <el-button slot="reference" type="danger" size="mini">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="pagination.total>0" style="float:right" :total="pagination.total" :page.sync="queryBody.current" :limit.sync="queryBody.pageSize" @pagination="fetchData" />
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="updateFormVisible" :close-on-click-modal="false">
      <el-form ref="dataForm" :rules="rules" :model="form" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="分类编码" prop="code">
          <el-input v-model="form.code" autocomplete="off" />
        </el-form-item>
        <el-form-item label="展示顺序" prop="code">
          <el-input-number v-model="form.showIndex" :min="1" :max="999" label="新选择展示顺序" />
        </el-form-item>

        <el-form-item
          label="
            状态"
          prop="status"
        >
          <el-select v-model="form.status" placeholder="请选择">
            <el-option
              v-for="item in api_classify_map"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="描述" prop="code">
          <el-input
            v-model="form.description"
            autocomplete="off"
            :rows="2"
            type="textarea"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?addClassify():updateClassify() ">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { classifyList, deleteClassify, addClassify, updateClassify } from '@/api/api'
import Pagination from '@/components/Pagination'
import common_data from '@/views/common'
import waves from "@/directive/waves";

export default {
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      return common_data.api_classify_map[status]
    }
  },
  data() {
    return {
      list: null,
      api_classify_map: common_data.api_classify_status,
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
        code: '',
        name: '',
        current: 1,
        pageSize: 20
      },
      form: {
        id: null,
        name: '',
        code: '',
        description: '',
        showIndex: '',
        status: ''
      },
      dialogStatus: '',
      updateFormVisible: false,
      rules: {
        name: [{ required: true, pattern: '[^ \x22]+', message: '名称不能为空', trigger: 'change' }],
        code: [{ required: true, pattern: '[^ \x22]+', message: '编码不能为空', trigger: 'change' }],
        description: [{ required: true, pattern: '[^ \x22]+', message: '编码不能为空', trigger: 'change' }],
        showIndex: [{ required: true, pattern: '[^ \x22]+', message: '编码不能为空', trigger: 'change' }],
        status: [{ required: true, message: '状态不能为空', trigger: 'blur' }]
      },
      textMap: {
        update: '编辑',
        create: '新增'
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      classifyList(this.queryBody).then(response => {
        this.list = response.data.list
        this.pagination = response.data.pagination
        this.listLoading = false
      })
    },
    handleUpdate(row) {
      this.form.id = row.id
      this.form.code = row.code
      this.form.name = row.name
      this.form.status = row.status
      this.form.description = row.description
      this.form.showIndex = row.showIndex
      this.dialogStatus = 'update'
      this.updateFormVisible = true
    },
    handleCreate() {
      this.form.id = null
      this.form.code = null
      this.form.name = null
      this.form.description = null
      this.form.showIndex = 0
      this.form.defaultRole = 0
      this.form.status = 0
      this.dialogStatus = 'create'
      this.updateFormVisible = true
    },
    updateClassify() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateClassify(this.form).then(response => {
            this.$message({
              message: '更新成功',
              type: 'success'
            })
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
    addClassify() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          addClassify(this.form).then(response => {
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
    deleteClassify(id) {
      deleteClassify(id).then(response => {
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
