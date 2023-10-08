<template>
  <div class="app-container">
    <el-row>
      <el-col :span="4"><div class="add-container">
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
          新增
        </el-button></div></el-col>
      <el-col :span="20"><div class="filter-container">
        <el-input v-model="queryBody.name" placeholder="API名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
        <el-select v-model="queryBody.classifyName" placeholder="API分类" class="filter-item" clearable>
          <el-option
            v-for="item in api_classify_map"
            :key="item.value"
            :label="item.label"
            :value="item.label"
          />
        </el-select>
        <el-select v-model="queryBody.status" placeholder="状态" class="filter-item" clearable>
          <el-option
            v-for="item in api_status_map"
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
      <el-table-column label="名称" width="200">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="分类" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.classifyName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="上下架" align="center" width="200">
        <template slot-scope="scope">
          <el-tag :type="String(scope.row.status)">{{ scope.row.status | statusFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="开放申请" align="center">
        <template slot-scope="scope">
          <el-tag :type="String(scope.row.openApplyStatus)">{{ scope.row.openApplyStatus | openApplyStatusFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="描述" width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.description }}</span>
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

      <el-table-column label="操作" align="center" width="420" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" :disabled="row.status === 1" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button size="mini" :type="row.status === 0 ? 'success' : 'danger'" @click="showUpAndDown(row)">
            {{ row.status === 0 ? '全部下架' : (row.status === 7 ? '全部上架': '部分上架') }}
          </el-button>
          <el-button size="mini" :type="row.openApplyStatus === 1 ? 'success' : 'danger'" @click="openApplyStatus(row)">
            {{ row.openApplyStatus === 0 ? '关闭申请' : '开放申请' }}
          </el-button>
          <el-button type="primary" size="mini" @click="handleDetails(row)">
            详情
          </el-button>
          <el-button
            :type="row.status === 0 ? 'primary' : 'success'"
            size="small"
            @click="developApi(row)"
          >
            {{ row.status === 0 ? '开发' : '预览' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="pagination.total>0" style="float:right" :total="pagination.total" :page.sync="queryBody.current" :limit.sync="queryBody.pageSize" @pagination="fetchData" />

    <el-dialog v-el-drag-dialog :title="textMap[dialogStatus]" :visible.sync="updateFormVisible" style="width: 2000px; " :close-on-click-modal="false" @dragDialog="handleDrag">

      <el-steps :active="active" finish-status="success">
        <el-step title="API信息" />
        <el-step title="API详细信息" />
        <el-step title="API请求参数" />
        <el-step title="API响应参数" />
      </el-steps>

      <el-form ref="dataForm" :rules="rules" :model="form" label-position="left" label-width="100px">
        <div v-show="active == 1">
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name" autocomplete="off" />
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input v-model="form.description" autocomplete="off" />
          </el-form-item>
          <el-form-item label="分类" prop="classifyId">
            <el-select v-model="form.classifyId" placeholder="请选择">
              <el-option
                v-for="item in api_classify_map"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="开放申请" prop="openApplyStatus">
            <el-select v-model="form.openApplyStatus" placeholder="请选择">
              <el-option
                v-for="item in api_open_apply_status"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item v-if="form.openApplyStatus === 1" label="不开放原因" prop="notOpenApplyReason">
            <el-input v-model="form.notOpenApplyReason" type="area" rows="2" />
          </el-form-item>
          <el-form-item label="图片" prop="imageUrl">
            <el-upload
              ref="upload"
              action="#"
              list-type="picture-card"
              :class="{ disabled: uploadDisabled }"
              :auto-upload="false"
              :limit="1"
              :on-change="getFile"
              :on-preview="handlePictureCardPreview"
              :on-remove="handleRemove"
              :file-list="fileList"
            >
              <i class="el-icon-plus" />
            </el-upload>
            <!-- 预览图片 -->
            <el-dialog :visible.sync="dialogVisibleimg" append-to-body>
              <img width="100%" :src="form.imageUrl" alt="">
            </el-dialog>
          </el-form-item>
        </div>
        <div v-show="active == 2">
          <el-form-item label="访问地址" prop="openApiDetailsRequestModel.apiAddress">
            <el-input v-model="form.openApiDetailsRequestModel.apiAddress" autocomplete="off" />
          </el-form-item>
          <el-form-item label="协议" prop="openApiDetailsRequestModel.protocol">
            <el-select v-model="form.openApiDetailsRequestModel.protocol" placeholder="请选择">
              <el-option
                v-for="item in api_protocol_map"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="请求方式" prop="openApiDetailsRequestModel.requestMode">
            <el-select v-model="form.openApiDetailsRequestModel.requestMode" placeholder="请选择">
              <el-option
                v-for="item in api_requestMode_map"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="返回格式" prop="openApiDetailsRequestModel.returnFormat">
            <el-select v-model="form.openApiDetailsRequestModel.returnFormat" placeholder="请选择">
              <el-option
                v-for="item in api_returnFormat_map"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="详细描述" prop="openApiDetailsRequestModel.detailDescription">
            <el-input v-model="form.openApiDetailsRequestModel.detailDescription" type="textarea" />
          </el-form-item>
          <el-form-item label="返回示例" prop="openApiDetailsRequestModel.returnExample">
            <div class="editor-container">
              <json-editor ref="jsonEditor" v-model="form.openApiDetailsRequestModel.returnExample" />
            </div>
            <!-- <el-input v-model="form.openApiDetailsRequestModel.returnExample" type="textarea" /> -->
          </el-form-item>
        </div>
        <div v-show="active == 3">
          <el-table :data="form.openApiRequestParamsRequestModelList" border fit highlight-current-row style="width: 100%">
            <el-table-column min-width="120px" label="参数名称">
              <template slot-scope="{row}">
                <template v-if="row.edit">
                  <el-input v-model="row.name" class="edit-input" size="small" />
                </template>
                <span v-else>{{ row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column min-width="120px" label="参数类型">
              <template slot-scope="{row}">
                <template v-if="row.edit">
                  <!-- <el-input v-model="row.type" class="edit-input" size="small" /> -->
                  <el-select v-model="row.type" placeholder="请选择" class="edit-input" size="small">
                    <el-option
                      v-for="item in api_field_type_map"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </template>
                <span v-else>{{ row.type }}</span>
              </template>
            </el-table-column>
            <el-table-column min-width="120px" label="示例值">
              <template slot-scope="{row}">
                <template v-if="row.edit">
                  <el-input v-model="row.exampleValue" class="edit-input" size="small" />
                </template>
                <span v-else>{{ row.exampleValue }}</span>
              </template>
            </el-table-column>
            <el-table-column min-width="40px" label="必填">
              <template slot-scope="{row}">
                <template v-if="row.edit">
                  <el-checkbox v-model="row.required" true-label="1" false-label="0" />
                </template>
                <el-checkbox v-else v-model="row.required" true-label="1" false-label="0" disabled />
              </template>
            </el-table-column>
            <el-table-column min-width="120px" label="说明">
              <template slot-scope="{row}">
                <template v-if="row.edit">
                  <el-input v-model="row.description" class="edit-input" size="small" />
                </template>
                <span v-else>{{ row.description }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="160">
              <template slot-scope="{row, $index}">
                <el-button
                  v-if="row.edit"
                  type="success"
                  size="small"
                  @click="confirmEdit('request', $index, row)"
                >
                  确定
                </el-button>
                <el-button
                  v-else
                  type="primary"
                  size="small"
                  @click="updateEdit('', $index, row)"
                >
                  编辑
                </el-button>
                <el-button
                  type="danger"
                  size="small"
                  @click="deleteRequestParmaLine($index)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-button style="margin-top: 12px" @click="addRequestParmaLine">新增一行</el-button>
        </div>
        <div v-show="active == 4">
          <el-table :data="form.openApiResponseRequestModelList" border fit highlight-current-row style="width: 100%">
            <el-table-column :key="0" min-width="120px" label="字段名称">
              <template slot-scope="{row}">
                <template v-if="row.edit">
                  <el-input v-model="row.name" class="edit-input" size="small" />
                </template>
                <span v-else>{{ row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column :key="1" min-width="120px" label="字段类型">
              <template slot-scope="{row}">
                <template v-if="row.edit">
                  <el-select v-model="row.type" placeholder="请选择" class="edit-input" size="small">
                    <el-option
                      v-for="item in api_field_type_map"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                  <!-- <el-input v-model="row.type" class="edit-input" size="small" /> -->
                </template>
                <span v-else>{{ row.type }}</span>
              </template>
            </el-table-column>
            <el-table-column :key="2" min-width="120px" label="示例值">
              <template slot-scope="{row}">
                <template v-if="row.edit">
                  <el-input v-model="row.exampleValue" class="edit-input" size="small" />
                </template>
                <span v-else>{{ row.exampleValue }}</span>
              </template>
            </el-table-column>
            <el-table-column :key="3" min-width="120px" label="说明">
              <template slot-scope="{row}">
                <template v-if="row.edit">
                  <el-input v-model="row.description" class="edit-input" size="small" />
                </template>
                <span v-else>{{ row.description }}</span>
              </template>
            </el-table-column>
            <el-table-column :key="4" align="center" label="Actions" width="160">
              <template slot-scope="{row, $index}">
                <el-button
                  v-if="row.edit"
                  type="success"
                  size="small"
                  @click="confirmEdit('', $index, row)"
                >
                  确定
                </el-button>
                <el-button
                  v-else
                  type="primary"
                  size="small"
                  @click="updateEdit('', $index, row)"
                >
                  编辑
                </el-button>
                <el-button
                  type="danger"
                  size="small"
                  @click="deleteResponseParmaLine($index)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-button style="margin-top: 12px" @click="addResponseParmaLine">新增一行</el-button>
        </div>

      </el-form>
      <el-button v-if="active > 1" style="margin-top: 12px" @click="pre">上一步</el-button>
      <el-button v-if="active < 4" style="margin-top: 12px" type="primary" @click="next">下一步</el-button>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetDialong">取 消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?addApi():updateApi() ">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="上/下架" :visible.sync="updateUpAndDownStatusObj.upAndDownDialogFormVisible" :close-on-click-modal="false">
      <el-form :model="form">
        <el-form-item label="测试环境" :label-width="formLabelWidth">
          <el-radio-group v-model="updateUpAndDownStatusObj.testStatus">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="预发环境" :label-width="formLabelWidth">
          <el-radio-group v-model="updateUpAndDownStatusObj.preStatus">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="正式环境" :label-width="formLabelWidth">
          <el-radio-group v-model="updateUpAndDownStatusObj.prodStatus">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateUpAndDownStatusObj.upAndDownDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="upAndDown">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="关闭申请" :visible.sync="updateApplyStatusObj.notOpenApplyReasonDialogFormVisible" :close-on-click-modal="false">
      <el-form :model="form">
        <el-form-item label="关闭原因" :label-width="formLabelWidth">
          <el-input v-model="updateApplyStatusObj.notOpenApplyReason" autocomplete="off" type="textarea" rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateApplyStatusObj.notOpenApplyReasonDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="changeOpenApplyStatus">确 定</el-button>
      </div>
    </el-dialog>

    <detail :api-data="apiData" :drawer="detailDrawer" @close="closeDatailDrawer" />
  </div>
</template>

<script>
import { apiList, classifyList, addApi, updateApi, getApiById, updateApiStatus, updateApplyStatus } from '@/api/api'
import Pagination from '@/components/Pagination'
import common_data from '@/views/common/index'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog'
import detail from '@/views/api/detail.vue'
import JsonEditor from '@/components/JsonEditor'
import { validatorApiPath } from '@/utils/validate'

export default {
  name: 'APIMANAGE',
  components: { Pagination, JsonEditor, detail },
  directives: { waves, elDragDialog },
  filters: {
    statusFilter(status) {
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
    openApplyStatusFilter(status) {
      const statusMap = {
        0: '开放',
        1: '不开放'
      }
      return statusMap[status]
    }
  },
  data() {
    const validateApiPathVaild = (rule, value, callback) => {
      if (!validatorApiPath(value)) {
        callback(new Error('请输入正确的路径,必须以/开头，只能包含数字、字母、/、-、_'))
      } else {
        callback()
      }
    }
    return {
      formLabelWidth: 80,
      api_protocol_map: common_data.api_protocol_map,
      api_status_map: common_data.api_status,
      api_requestMode_map: common_data.api_requestMode_map,
      api_returnFormat_map: common_data.api_returnFormat_map,
      api_field_type_map: common_data.api_field_type_map,
      api_open_apply_status: common_data.api_open_apply_status,
      api_classify_map: [],
      fileList: [],
      dialogVisibleimg: false,
      uploadDisabled: false,
      active: 1,
      list: null,
      pagination: {
        total: 0,
        startNumber: 0,
        totalPage: 1
      },
      listQuery: {
        page: 1,
        limit: 10,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: '+id'
      },
      listLoading: true,
      queryBody: {
        name: '',
        classifyName: '',
        classifyId: '',
        status: undefined,
        current: 1,
        pageSize: 10
      },
      apiData: {},
      updateApplyStatusObj: {
        id: null,
        notOpenApplyReasonDialogFormVisible: false,
        notOpenApplyReason: 'API维护中,暂不支持申请......',
        openApplyStatus: 0
      },
      updateUpAndDownStatusObj: {
        id: null,
        upAndDownDialogFormVisible: false,
        upAndDownStatus: 0,
        testStatus: 0,
        preStatus: 0,
        prodStatus: 0
      },
      form: {
        id: null,
        name: null,
        description: null,
        imageUrl: null,
        classifyId: null,
        openApplyStatus: 0,
        notOpenApplyReason: 'API维护中,暂不支持申请......',
        openApiDetailsRequestModel: {
          detailDescription: null,
          apiAddress: null,
          protocol: null,
          requestMode: null,
          returnFormat: null,
          returnExample: null
        },
        openApiRequestParamsRequestModelList: [
          {
            name: null,
            type: null,
            required: 0,
            exampleValue: null,
            description: null,
            edit: true
          }
        ],
        openApiResponseRequestModelList: [
          {
            name: null,
            type: null,
            exampleValue: null,
            description: null,
            edit: true
          }
        ]
      },
      detailDrawer: false,
      dialogStatus: '',
      updateFormVisible: false,
      rules: {
        name: [{ required: true, pattern: '[^ \x22]+', message: '名称不能为空', trigger: 'change' }],
        description: [{ required: true, pattern: '[^ \x22]+', message: '描述不能为空', trigger: 'change' }],
        imageUrl: [{ required: true, pattern: '[^ \x22]+', message: '图片不能为空', trigger: 'change' }],
        classifyId: [{ required: true, message: '分类不能为空', trigger: 'change' }],
        detailDescription: [{ required: true, pattern: '[^ \x22]+', message: '详细描述不能为空', trigger: 'change' }],
        'openApiDetailsRequestModel.apiAddress': [{ required: true, validator: validateApiPathVaild, trigger: 'blur' }],
        'openApiDetailsRequestModel.protocol': [{ required: true, pattern: '[^ \x22]+', message: '访问协议不能为空', trigger: 'change' }],
        'openApiDetailsRequestModel.requestMode': [{ required: true, pattern: '[^ \x22]+', message: '请求方式不能为空', trigger: 'change' }],
        'openApiDetailsRequestModel.returnFormat': [{ required: true, pattern: '[^ \x22]+', message: '返回格式不能为空', trigger: 'change' }],
        'openApiDetailsRequestModel.returnExample': [{ required: true, pattern: '[^ \x22]+', message: '返回示例不能为空', trigger: 'change' }]
      },
      textMap: {
        update: '编辑',
        create: '新增'
      }
    }
  },
  created() {
  },
  beforeRouteLeave(to, from, next) {
    if (to.name === 'devApi') {
      // 保存参数
      sessionStorage.setItem('api.queryBody', JSON.stringify(this.queryBody))
    } else {
      sessionStorage.removeItem('api.queryBody')
    }
    next()
  },
  beforeRouteEnter(to, from, next) {
    if (from.name === 'devApi') {
      // 获取参数
      next(vm => {
        vm.queryBody = JSON.parse(sessionStorage.getItem('api.queryBody'))
        vm.fetchData()
        vm.loadClassify()
      })
    } else {
      next(vm => {
        vm.fetchData()
        vm.loadClassify()
      })
    }
  },
  methods: {
    fetchData() {
      this.listLoading = true
      this.queryBody.current = 1
      apiList(this.queryBody).then(response => {
        this.list = response.data.list
        this.pagination = response.data.pagination
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.fetchData()
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw)
    },
    developApi(row) {
      this.$router.push({
        name: 'devApi',
        params: {
          id: row.id,
          canEdit: row.status === 0
        }
      })
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isLt2M
    },
    getFile(file, fileList) {
      if (this.beforeAvatarUpload(file)) {
        this.getBase64(file.raw).then(res => {
          this.form.imageUrl = res
          this.uploadDisabled = true
        })
      } else {
        fileList.pop()
      }
    },
    handleDetails(row) {
      // 获取数据
      getApiById(row.id).then(response => {
        this.apiData = response.data
        this.detailDrawer = true
      })
    },
    updateEdit(type, index, row) {
      if (row.edit === undefined) {
        if (type === 'request') {
          this.$set(this.form.openApiRequestParamsRequestModelList, index, { ...this.form.openApiRequestParamsRequestModelList[index], edit: true })
        } else {
          this.$set(this.form.openApiResponseRequestModelList, index, { ...this.form.openApiResponseRequestModelList[index], edit: true })
        }
      } else {
        if (type === 'request') {
          this.$set(this.form.openApiRequestParamsRequestModelList, index, { ...this.form.openApiRequestParamsRequestModelList[index], edit: !this.form.openApiRequestParamsRequestModelList[index].edit })
        } else {
          this.$set(this.form.openApiResponseRequestModelList, index, { ...this.form.openApiResponseRequestModelList[index], edit: !this.form.openApiResponseRequestModelList[index].edit })
        }
      }
    },
    closeDatailDrawer() {
      this.apiData = {
        id: null,
        name: null,
        description: null,
        imageUrl: null,
        classifyId: null,
        openApplyStatus: 0,
        notOpenApplyReason: 'API维护中,暂不支持申请......',
        openApiDetailsRequestModel: {
          detailDescription: null,
          apiAddress: null,
          protocol: null,
          requestMode: null,
          returnFormat: null,
          returnExample: null
        },
        openApiRequestParamsRequestModelList: [
          {
            name: null,
            type: null,
            exampleValue: null,
            required: 0,
            description: null,
            edit: true
          }
        ],
        openApiResponseRequestModelList: [
          {
            name: null,
            type: null,
            exampleValue: null,
            description: null,
            edit: true
          }
        ]
      }
      this.detailDrawer = false
    },
    // 这里是文件转base64
    getBase64(file) {
      return new Promise(function(resolve, reject) {
        const reader = new FileReader()
        let imgResult = ''
        reader.readAsDataURL(file)
        reader.onload = function() {
          imgResult = reader.result
        }
        reader.onerror = function(error) {
          reject(error)
        }
        reader.onloadend = function() {
          resolve(imgResult)
        }
      })
    },
    showUpAndDown(row) {
      this.updateUpAndDownStatusObj.id = row.id
      this.updateUpAndDownStatusObj.upAndDownStatus = row.status
      // 获取二进制  3位
      var str = this.updateUpAndDownStatusObj.upAndDownStatus.toString(2)
      const strLength = str.length
      for (let i = 0; i < 3 - strLength; i++) {
        str = '0' + str
      }
      this.updateUpAndDownStatusObj.testStatus = Number(str[2])
      this.updateUpAndDownStatusObj.preStatus = Number(str[1])
      this.updateUpAndDownStatusObj.prodStatus = Number(str[0])
      this.updateUpAndDownStatusObj.upAndDownDialogFormVisible = true
    },

    upAndDown() {
      const str = String(this.updateUpAndDownStatusObj.testStatus) + String(this.updateUpAndDownStatusObj.preStatus) + String(this.updateUpAndDownStatusObj.testStatus)
      this.updateUpAndDownStatusObj.upAndDownStatus = parseInt(str, 2)
      updateApiStatus(this.updateUpAndDownStatusObj.id, this.updateUpAndDownStatusObj.upAndDownStatus).then(response => {
        this.$message({
          message: '成功',
          type: 'success'
        })
        this.fetchData()
      }).finally(() => {
        this.updateUpAndDownStatusObj.upAndDownDialogFormVisible = false
      })
    },
    openApplyStatus(row) {
      const status = row.openApplyStatus === 0 ? 1 : 0
      if (status === 1) {
        this.updateApplyStatusObj.id = row.id
        this.updateApplyStatusObj.openApplyStatus = status
        this.updateApplyStatusObj.notOpenApplyReasonDialogFormVisible = true
      } else {
        this.updateApplyStatusObj.id = row.id
        this.updateApplyStatusObj.openApplyStatus = status
        this.changeOpenApplyStatus()
      }
    },
    changeOpenApplyStatus() {
      updateApplyStatus(this.updateApplyStatusObj.id, this.updateApplyStatusObj.openApplyStatus, this.updateApplyStatusObj.notOpenApplyReason).then(response => {
        this.$message({
          message: '成功',
          type: 'success'
        })
        this.fetchData()
      }).finally(() => {
        this.updateApplyStatusObj.notOpenApplyReasonDialogFormVisible = false
      })
    },
    resetDialong() {
      this.form = {
        id: null,
        name: null,
        description: null,
        imageUrl: null,
        classifyId: null,
        openApplyStatus: 0,
        notOpenApplyReason: 'API维护中,暂不支持申请......',
        openApiDetailsRequestModel: {
          detailDescription: null,
          apiAddress: null,
          protocol: null,
          requestMode: null,
          returnFormat: null,
          returnExample: null
        },
        openApiRequestParamsRequestModelList: [
          {
            name: null,
            type: null,
            required: 0,
            exampleValue: null,
            description: null,
            edit: true
          }
        ],
        openApiResponseRequestModelList: [
          {
            name: null,
            type: null,
            exampleValue: null,
            description: null,
            edit: true
          }
        ]
      }
      this.fileList = []
      this.active = 1
      this.uploadDisabled = false
      this.updateFormVisible = false
    },
    handleUpdate(row) {
      // 更新
      if (this.api_classify_map.length === 0) {
        this.loadClassify()
      }
      // 获取数据
      getApiById(row.id).then(response => {
        this.form = response.data
        this.active = 1
        this.dialogStatus = 'update'
        this.updateFormVisible = true
        this.uploadDisabled = true
        this.fileList = []
        this.fileList.push({ 'name': 'image', 'url': this.form.imageUrl })
        if (this.form.openApiRequestParamsRequestModelList === null) {
          this.form.openApiRequestParamsRequestModelList = []
        } else {
          this.form.openApiRequestParamsRequestModelList.forEach(it => {
            it.edit = false
          })
        }
        if (this.form.openApiResponseRequestModelList === null) {
          this.form.openApiResponseRequestModelList = []
        } else {
          this.form.openApiResponseRequestModelList.forEach(it => {
            it.edit = false
          })
        }
      })
    },
    async checkActive1() {
      const fieldsToValidate = ['name', 'description', 'imageUrl', 'classifyId']
      // 将回调转换为Promise
      const valid = await Promise.all(
        fieldsToValidate.map((field) => {
          return new Promise((resolve, reject) => {
            this.$refs['dataForm'].validateField(field, (errorMessage) => {
              resolve(errorMessage)
            })
          })
        })
      ).then((errorMessages) => {
        const valid = errorMessages.every((errorMessage) => {
          return errorMessage === ''
        })
        return valid
      })
      return valid
    },
    async checkActive2() {
      const valid = await this.$refs['dataForm'].validate().catch((err) => { return err })
      return valid
    },
    checkActive3() {
      if (this.form.openApiRequestParamsRequestModelList) {
        for (const data of this.form.openApiRequestParamsRequestModelList) {
          if (data.edit) {
            if (!this.confirmEdit('request', data)) {
            // 存在有问题的
              return false
            }
          }
        }
      }
      return true
    },
    checkActive4() {
      if (this.form.openApiResponseRequestModelList) {
        for (const data of this.form.openApiResponseRequestModelList) {
          if (data.edit) {
            if (!this.confirmEdit('', data)) {
            // 存在有问题的
              return false
            }
          }
        }
      }
      return true
    },
    next() {
      if (this.active === 1) {
        // if (this.checkActive1()) {
        //   if (this.active++ > 3) this.active = 1
        // }
        this.checkActive1().then(
          valid => {
            if (valid) {
              if (this.active++ > 3) this.active = 1
            }
          }
        )
      } else if (this.active === 2) {
        this.checkActive2().then(
          valid => {
            if (valid) {
              if (this.active++ > 3) this.active = 1
            }
          }
        )
      } else if (this.active === 3) {
        if (this.checkActive3()) {
          if (this.active++ > 3) this.active = 1
        }
      }
    },
    pre() {
      if (this.active-- < 2) this.active = 1
    },
    // 删除活动展示照片
    handleRemove(file, fileList) {
      this.form.imageUrl = ''
      this.uploadDisabled = false
    },
    // 活动展示照片预览
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisibleimg = true
    },
    loadClassify() {
      classifyList({
        code: '',
        name: '',
        current: 1,
        pageSize: 100
      }).then(response => {
        for (const data of response.data.list) {
          this.api_classify_map.push({
            'label': data.name, 'value': data.id
          })
        }
      })
    },
    handleCreate() {
      if (this.api_classify_map.length === 0) {
        this.loadClassify()
      }
      this.active = 1
      this.dialogStatus = 'create'
      this.updateFormVisible = true
    },
    confirmEdit(type, index, row) {
      // 判断是否为空
      if (row.name === undefined || row.name === null || row.name.trim() === '' || row.type === undefined || row.type === null || row.type.trim() === '') {
        this.$message({
          message: '名称或类型不能为空',
          type: 'error'
        })
        return false
      }

      // 判断是否存在重名、是否为空
      if (type === 'request') {
        const newListLength = new Set(this.form.openApiRequestParamsRequestModelList.map(item => item.name.trim())).size
        const listLength = this.form.openApiRequestParamsRequestModelList.length
        if (listLength > newListLength) {
          this.$message({
            message: '存在重复参数',
            type: 'error'
          })
          return false
        }
      } else {
        const newListLength = new Set(this.form.openApiResponseRequestModelList.map(item => item.name.trim())).size
        const listLength = this.form.openApiResponseRequestModelList.length
        if (listLength > newListLength) {
          this.$message({
            message: '存在重复参数',
            type: 'error'
          })
          return false
        }
      }
      if (type === 'request') {
        this.$set(this.form.openApiRequestParamsRequestModelList, index, { ...this.form.openApiRequestParamsRequestModelList[index], edit: false })
      } else {
        this.$set(this.form.openApiResponseRequestModelList, index, { ...this.form.openApiResponseRequestModelList[index], edit: false })
      }
      return true
    },
    addRequestParmaLine() {
      this.form.openApiRequestParamsRequestModelList.push({
        name: '',
        type: '',
        exampleValue: '',
        required: 0,
        description: '',
        edit: true
      })
    },
    deleteRequestParmaLine(index) {
      this.form.openApiRequestParamsRequestModelList.splice(index, 1)
    },
    addResponseParmaLine() {
      this.form.openApiResponseRequestModelList.push({
        name: '',
        type: '',
        exampleValue: '',
        description: '',
        edit: true
      })
    },
    deleteResponseParmaLine(index) {
      this.form.openApiResponseRequestModelList.splice(index, 1)
    },
    handleDrag() {
      this.$refs.select.blur()
    },
    addApi() {
      // 校验所有
      if (!this.checkActive1()) {
        this.$message({
          message: 'API信息校验未通过,请检查',
          type: 'error'
        })
        this.active = 1
        return false
      }
      if (!this.checkActive2()) {
        this.$message({
          message: 'API详细信息校验未通过,请检查',
          type: 'error'
        })
        this.active = 2
        return false
      }
      if (!this.checkActive3()) {
        this.$message({
          message: 'API请求参数校验未通过,请检查',
          type: 'error'
        })
        this.active = 3
        return false
      }
      if (!this.checkActive4()) {
        this.$message({
          message: 'API响应参数校验未通过,请检查',
          type: 'error'
        })
        this.active = 4
        return false
      }
      addApi(this.form).then(response => {
        this.updateFormVisible = false
        this.fetchData()
      }).finally(() => {
        this.updateFormVisible = false
      })
    },
    updateApi() {
      // 校验所有
      if (!this.checkActive1()) {
        this.$message({
          message: 'API信息校验未通过,请检查',
          type: 'error'
        })
        this.active = 1
        return false
      }
      if (!this.checkActive2()) {
        this.$message({
          message: 'API详细信息校验未通过,请检查',
          type: 'error'
        })
        this.active = 2
        return false
      }
      if (!this.checkActive3()) {
        this.$message({
          message: 'API请求参数校验未通过,请检查',
          type: 'error'
        })
        this.active = 3
        return false
      }
      if (!this.checkActive4()) {
        this.$message({
          message: 'API响应参数校验未通过,请检查',
          type: 'error'
        })
        this.active = 4
        return false
      }
      updateApi(this.form).then(response => {
        this.updateFormVisible = false
        this.fetchData()
      }).finally(() => {
        this.updateFormVisible = false
      })
    }
  }
}
</script>
<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
  .disabled .el-upload--picture-card {
	display: none;
}
.editor-container{
  position: relative;
  height: 100%;
}
</style>
