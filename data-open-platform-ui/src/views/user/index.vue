<template>
  <div class="app-container">
     <el-row>
      <el-col :span="12"><div class="add-container">
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
          新增
        </el-button></div></el-col>
      <el-col :span="12"><div class="filter-container">
        <el-input v-model="queryBody.email" placeholder="邮箱" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
        <el-input v-model="queryBody.name" placeholder="名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
        <el-select v-model="queryBody.status" placeholder="状态" class="filter-item" clearable>
          <el-option
            v-for="item in user_status_map"
            :key="item.value"
            :label="item.label"
            :value="item.label"
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
      <el-table-column label="项目名称">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="邮箱" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.email }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="String(scope.row.status)">{{ scope.row.status | statusFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="创建时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="更新时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <!-- <el-button v-if="row.status=='1'" size="mini" type="success" @click="handleModifyStatus(row,'published')">
            禁用
          </el-button>-->
          <el-button v-if="row.status=='2'" size="mini" type="primary" @click="activeUser(row.id)">
            激活
          </el-button>
          <el-popconfirm
            title="确定重置吗？"
            style="margin-left:10px;margin-right:10px"
            @onConfirm="resetKeyAndSecret(row.id)"
          >
            <el-button slot="reference" type="primary" size="mini">重置KEY</el-button>
          </el-popconfirm>
          <el-button v-if="row.status=='1'" size="mini" type="success" @click="getAppKeySecret(row.id)">
            查看秘钥
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="pagination.total>0" style="float:right" :total="pagination.total" :page.sync="queryBody.current" :limit.sync="queryBody.pageSize" @pagination="fetchData" />

    <el-dialog
      title="秘钥"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <div v-for="keyAndSecret in keyAndSecretList" :key="keyAndSecret.id">
        <div>
          <span>APPKEY: {{ keyAndSecret.appKey }}</span>
          <el-tag class="copy-tag" type="primary" @click="clipboardCopy(keyAndSecret.appKey, $event)">复制</el-tag>
        </div>
        <div>
          <span>APPSECRET: {{ keyAndSecret.appSecret }}</span>
          <el-tag class="copy-tag" type="primary" @click="clipboardCopy(keyAndSecret.appSecret, $event)">复制</el-tag>
        </div>
      </div>
    </el-dialog>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="updateFormVisible" :close-on-click-modal="false">
      <el-form :model="form" label-position="left" label-width="80px" :rules="registerRules">
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="用户邮箱" prop="email">
          <el-input v-model="form.email" autocomplete="off" />
        </el-form-item>
        <el-form-item label="用户角色"  v-if="dialogStatus=='update'">
          <el-select v-model="form.roleId"  placeholder="请选择">
            <el-option
              v-for="item in role_map"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="用户状态" v-if="dialogStatus=='update'">
          <el-select v-model="form.status"  placeholder="请选择">
            <el-option
              v-for="item in user_status_map"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?addUser():updateUser()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { userList, updateUser, addUser, activeUser } from '@/api/user-list'
import { roleList } from '@/api/role'
import { selectSecretByUserId, resetByUserId } from '@/api/secret'
import Pagination from '@/components/Pagination'
import clip from '@/utils/clipboard'
import common_data from '@/views/common/index'
import { validUsername, validAllowEmailSuffix } from '@/utils/validate'
import { Loading } from 'element-ui'
import waves from '@/directive/waves' // waves directive
export default {
  components: { Pagination },
  directives: { waves },
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
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('请输入正确的邮箱'))
      } else if(!validAllowEmailSuffix(value, common_data.allow_email_suffix)){
        callback(new Error('不支持的邮箱地址,支持的邮箱后缀为:' + common_data.allow_email_suffix.join()))
      } else {
        callback()
      }
    }
    return {
      user_status_map: common_data.user_status,
      role_map: [],
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
      updateFormVisible: false,
      dialogVisible: false,
      queryBody: {
        email: '',
        name: '',
        status: '',
        current: 1,
        pageSize: 20
      },
      form: {
        id: null,
        name: '',
        email: '',
        status: '',
        roleId: ''
      },
      textMap: {
        update: '编辑',
        create: '新增'
      },
      dialogStatus: '',
      registerRules: {
        name: [{  required: true, pattern: '[^ \x22]+', message: '项目名称不能为空', trigger: 'change'}],
        email: [{ required: true, trigger: 'change', validator: validateUsername }],
      },
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    handleCreate(){
      this.form.id = null
      this.form.email = null
      this.form.name = null
      this.form.status = null
      this.form.roleId = null
      this.dialogStatus = 'create'
      this.updateFormVisible = true
    },
    fetchData() {
      this.listLoading = true
      userList(this.queryBody).then(response => {
        this.list = response.data.list
        this.pagination = response.data.pagination
        this.listLoading = false
      }).finally(() => {
        this.listLoading = false
      })
    },
    getAppKeySecret(userId) {
      selectSecretByUserId(userId).then(response => {
        this.keyAndSecretList = response.data
        this.dialogVisible = true
      })
    },
    resetKeyAndSecret(userId) {
      resetByUserId(userId).then(response => {
        this.$message({
          message: '重置成功',
          type: 'success'
        })
        this.fetchData()
      })
    },
    activeUser(userId) {
      const newLoading = Loading.service({ fullscreen: true, background: 'rgb(0,0,0,0.5)' })
      activeUser(userId).then(response => {
        this.$message({
          message: '激活成功',
          type: 'success'
        })
        this.fetchData()
      }).finally(() => {
        newLoading.close()
      })
    },
    handleClose() {
      this.keyAndSecretList = null
      this.dialogVisible = false
    },
    clipboardCopy(text, event) {
      clip(text, event)
    },
    handleUpdate(row) {
      this.form.id = row.id
      this.form.email = row.email
      this.form.name = row.name
      this.form.status = row.status
      this.form.roleId = row.roleId
      this.getRoleListMap()
      this.dialogStatus = 'update'
      this.updateFormVisible = true
    },
    getRoleListMap() {
      if (this.role_map.length === 0) {
        roleList({
          name: '',
          current: 1,
          pageSize: 20
        }).then(response => {
          for (var data of response.data.list) {
            this.role_map.push({ 'value': data.id, 'label': data.name })
          }
        })
      }
    },
    updateUser() {
      updateUser(this.form).then(response => {
        this.$message({
          message: '更新成功',
          type: 'success'
        })
        this.fetchData()
      }).finally(() => {
            this.updateFormVisible = false
      })
    },
    addUser() {
      addUser(this.form).then(response => {
        this.$message({
          message: '新增成功',
          type: 'success'
        })
        this.fetchData()
      }).finally(() => {
            this.updateFormVisible = false
      })
    },
    handleFilter(){

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
