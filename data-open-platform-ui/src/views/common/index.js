const common_data = {
  user_status: [
    { value: 0, label: '停用' },
    { value: 1, label: '正常' },
    { value: 2, label: '未激活' }
  ],
  role_status: [
    { value: 0, label: '正常' },
    { value: 1, label: '禁用' }
  ],
  role_default_role: {
    0: '否',
    1: '是'
  },
  api_classify_map: {
    0: '正常',
    1: '禁用'
  },

  data_source_type_map: {
    0: 'MySQL',
    1: 'DB2',
    2: 'Oracle',
    3: 'PostgerSQL',
    4: 'Presto',
    5: 'SQLServer',
    6: 'MongoDB',
    7: 'Redis',
    8: 'Clickhouse',
    9: 'StarRocks'
  },

  data_source_status_map: {
    0: '启用',
    1: '禁用'
  },

  request_log_status_map: {
    0: '成功',
    1: '失败'
  },

  api_classify_status: [
    { value: 0, label: '正常' },
    { value: 1, label: '禁用' }
  ],

  api_status: [
    { value: 0, label: '下架' },
    { value: 1, label: '上架' }
  ],

  api_open_apply_status: [
    { value: 0, label: '开放' },
    { value: 1, label: '不开放' }
  ],

  api_protocol_map: [
    { value: 'http', label: 'HTTP' },
    { value: 'https', label: 'HTTPS' },
    { value: 'http/https', label: 'HTTP/HTTPS' }
  ],

  api_requestMode_map: [
    { value: 'get', label: 'GET' },
    { value: 'post', label: 'POST' }
  ],
  api_run_environment: [
    { value: 'test', label: '测试环境' },
    { value: 'pre', label: '预发环境' },
    { value: 'prod', label: '正式环境' }
  ],
  api_returnFormat_map: [
    { value: 'json', label: 'JSON' },
    { value: 'xml', label: 'XML' }
  ],

  api_field_type_map: [
    { value: 'boolean', label: 'boolean' },
    { value: 'char', label: 'char' },
    { value: 'byte', label: 'byte' },
    { value: 'short', label: 'short' },
    { value: 'int', label: 'int' },
    { value: 'long', label: 'long' },
    { value: 'float', label: 'float' },
    { value: 'string', label: 'string' },
    { value: 'object', label: 'object' },
    { value: 'array', label: 'array' }
  ],

  data_source_type_select_map: [
    { value: 0, label: 'MySQL' },
    { value: 1, label: 'DB2' },
    { value: 2, label: 'Oracle' },
    { value: 3, label: 'PostgerSQL' },
    { value: 4, label: 'Presto' },
    { value: 5, label: 'SQLServer' },
    { value: 6, label: 'MongoDB' },
    { value: 7, label: 'Redis' },
    { value: 8, label: 'Clickhouse' },
    { value: 9, label: 'StarRocks' }
  ],

  data_source_status_select_map: [
    { value: 0, label: '启用' },
    { value: 1, label: '禁用' }
  ],

  api_subscribe_status_select_map: [
    { value: 1, label: '通过' },
    { value: 2, label: '拒绝' }
  ],

  allow_email_suffix: ['chaojima.com', 'app315.net']
}

export default common_data
