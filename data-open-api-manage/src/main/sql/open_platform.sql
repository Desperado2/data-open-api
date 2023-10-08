/*
 Navicat Premium Data Transfer

 Source Server         : 大数据正式
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : 192.168.20.66:3306
 Source Schema         : open_platform

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 11/04/2023 21:17:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_table
-- ----------------------------
DROP TABLE IF EXISTS `base_table`;
CREATE TABLE `base_table`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基础表-未使用' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_table
-- ----------------------------

-- ----------------------------
-- Table structure for t_api_logs
-- ----------------------------
DROP TABLE IF EXISTS `t_api_logs`;
CREATE TABLE `t_api_logs`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `log_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志唯一key',
  `app_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'APPKEY',
  `api_id` bigint(11) NULL DEFAULT NULL COMMENT 'API id',
  `api_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'API名称',
  `user_id` bigint(11) NULL DEFAULT NULL COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `request_time` bigint(11) NULL DEFAULT NULL COMMENT '请求时间戳',
  `response_time` bigint(11) NULL DEFAULT NULL COMMENT '响应时间戳',
  `request_environment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求环境',
  `request_params` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `result_count` bigint(11) NULL DEFAULT NULL COMMENT '返回结果数据量',
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '请求状态',
  `error_msg` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unq_log_key`(`log_key`) USING BTREE COMMENT '唯一key'
) ENGINE = InnoDB AUTO_INCREMENT = 219 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'API日志表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_api_subscribe
-- ----------------------------
DROP TABLE IF EXISTS `t_api_subscribe`;
CREATE TABLE `t_api_subscribe`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `api_id` bigint(11) NULL DEFAULT NULL COMMENT 'api id',
  `user_id` bigint(11) NULL DEFAULT NULL COMMENT '用户id',
  `subscribe_status` tinyint(2) NULL DEFAULT NULL COMMENT '状态',
  `refuse_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拒绝原因',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'API订阅信息' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_datasource_config
-- ----------------------------
DROP TABLE IF EXISTS `t_datasource_config`;
CREATE TABLE `t_datasource_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '类型',
  `driver` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '驱动类',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库连接',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `enabled` tinyint(2) NULL DEFAULT NULL COMMENT '是否启用，0-是，1-否',
  `properties` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接信息对象',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基础表-未使用' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_key_secret
-- ----------------------------
DROP TABLE IF EXISTS `t_key_secret`;
CREATE TABLE `t_key_secret`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `app_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'appKey',
  `app_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'appSecret',
  `user_id` bigint(11) NULL DEFAULT NULL COMMENT '用户id',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对接秘钥表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_open_api
-- ----------------------------
DROP TABLE IF EXISTS `t_open_api`;
CREATE TABLE `t_open_api`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'API名称',
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `classify_id` bigint(11) NULL DEFAULT NULL COMMENT '分类id',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态',
  `open_apply_status` tinyint(2) NULL DEFAULT NULL COMMENT '开放申请状态',
  `not_open_apply_reason` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '不开放原因',
  `image_url` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片URL',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '开发API' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_open_api_classify
-- ----------------------------
DROP TABLE IF EXISTS `t_open_api_classify`;
CREATE TABLE `t_open_api_classify`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `show_index` int(11) NULL DEFAULT NULL COMMENT '展示地址',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'API分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_open_api_classify
-- ----------------------------
INSERT INTO `t_open_api_classify` VALUES (1, 'xwzx', '新闻资讯', 1, '日常生活服务类接口，如天气预报、垃圾分类等', 0, '2023-02-15 18:13:02', '2023-03-16 09:56:59');
INSERT INTO `t_open_api_classify` VALUES (4, 'shfw', '生活服务', 2, '趣味娱乐类接口，如藏头诗、脑筋急转弯等', 0, '2023-03-16 09:31:29', '2023-03-16 09:57:17');
INSERT INTO `t_open_api_classify` VALUES (5, 'qwyl', '趣味娱乐', 3, '图像识别、信息处理、二维码等功能类接口', 0, '2023-03-16 09:31:45', '2023-03-16 09:57:31');
INSERT INTO `t_open_api_classify` VALUES (6, 'gnyy', '功能应用', 4, '一站到底、唐诗大全、十万个为什么等知识类接口', 0, '2023-03-16 09:31:58', '2023-03-16 09:57:45');
INSERT INTO `t_open_api_classify` VALUES (7, 'zswd', '知识问答', 5, '图像识别、垃圾分类、人脸融合等智能类接口', 0, '2023-03-16 09:32:09', '2023-03-16 09:58:10');
INSERT INTO `t_open_api_classify` VALUES (8, 'sjzn', '数据智能', 6, '图像识别、垃圾分类、人脸融合等智能类接口', 0, '2023-03-16 09:32:27', '2023-03-16 09:58:24');

-- ----------------------------
-- Table structure for t_open_api_request_params
-- ----------------------------
DROP TABLE IF EXISTS `t_open_api_request_params`;
CREATE TABLE `t_open_api_request_params`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `api_id` bigint(11) NULL DEFAULT NULL COMMENT 'API id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名称',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数类型',
  `required` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否必须',
  `example_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '示例值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'API请求参数' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_open_api_response
-- ----------------------------
DROP TABLE IF EXISTS `t_open_api_response`;
CREATE TABLE `t_open_api_response`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `api_id` bigint(11) NULL DEFAULT NULL COMMENT 'API id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名称',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数类型',
  `example_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '示例值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'API响应参数' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_open_api_scripts
-- ----------------------------
DROP TABLE IF EXISTS `t_open_api_scripts`;
CREATE TABLE `t_open_api_scripts`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `api_id` bigint(20) NOT NULL COMMENT 'API ID',
  `api_run_environment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'API执行环境',
  `test_datasource_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '测试数据源code',
  `pre_datasource_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预发数据源code',
  `prod_datasource_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '正式数据源code',
  `api_script_type` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '脚本类型：SQL、DataQL',
  `api_script` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '查询脚本：xxxxxxx',
  `api_script_status` tinyint(2) NULL DEFAULT NULL COMMENT '状态',
  `api_request_header` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口的请求头',
  `api_request_body` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口的请求数据结构',
  `api_response_header` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口的响应头',
  `api_response_body` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口的响应数据结构',
  `api_response_format` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口的响应自定义数据结构',
  `api_option` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '扩展配置信息',
  `custom_result_structure` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否自定义返回结果，0-否，1-是',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'API的脚本' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_open_api_scripts_release
-- ----------------------------
DROP TABLE IF EXISTS `t_open_api_scripts_release`;
CREATE TABLE `t_open_api_scripts_release`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `api_id` bigint(20) NOT NULL COMMENT 'API ID',
  `api_run_environment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'API执行环境',
  `test_datasource_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '测试数据源code',
  `pre_datasource_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预发数据源code',
  `prod_datasource_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '正式数据源code',
  `api_script_type` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '脚本类型：SQL、DataQL',
  `api_script` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '查询脚本：xxxxxxx',
  `api_script_status` tinyint(2) NULL DEFAULT NULL COMMENT '状态',
  `api_request_header` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口的请求头',
  `api_request_body` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口的请求数据结构',
  `api_response_header` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口的响应头',
  `api_response_body` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口的响应数据结构',
  `api_response_format` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口的响应自定义数据结构',
  `api_option` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '扩展配置信息',
  `custom_result_structure` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否自定义返回结果，0-否，1-是',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'API的脚本' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_open_details
-- ----------------------------
DROP TABLE IF EXISTS `t_open_details`;
CREATE TABLE `t_open_details`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `api_id` bigint(11) NULL DEFAULT NULL COMMENT 'API ID',
  `detail_description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细描述',
  `api_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'API访问地址',
  `protocol` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支持协议',
  `request_mode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
  `return_format` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '返回格式',
  `return_example` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回示例',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'API详情' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `default_role` tinyint(2) NULL DEFAULT NULL COMMENT '默认角色，0-否，1-是',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, 'admin', '管理员', 0, 0, '2023-02-15 16:33:11', '2023-02-15 16:33:13');
INSERT INTO `t_role` VALUES (4, 'normal', '普通角色', 1, 0, '2023-03-01 15:17:09', '2023-03-01 16:07:06');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` bigint(11) NULL DEFAULT NULL COMMENT '角色id',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 1, 'admin@qq.com', '8C4C48F0BE2C4CFD9CCCB15D0AFCE5640B3415AEC8E9B568D3EDCEDBB0321919366BE0BD5EA97D3B82AD54E7159C296C', '管理员1号', 1, '2023-02-15 16:36:48', '2023-03-01 14:20:31');

SET FOREIGN_KEY_CHECKS = 1;
