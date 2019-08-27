/*
 Navicat Premium Data Transfer

 Source Server         : MySql-3306-docker
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 127.0.0.1:3306
 Source Schema         : mq-transaction

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 27/08/2019 15:14:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for money
-- ----------------------------
DROP TABLE IF EXISTS `money`;
CREATE TABLE `money`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `my_money` int(11) NULL DEFAULT NULL,
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of money
-- ----------------------------
INSERT INTO `money` VALUES ('1ceb9443-5c99-4f14-a051-40d1caef6221', 0, '3d203e0ca62543049ffa925ba2bbf893');
INSERT INTO `money` VALUES ('68b26d7d-f63c-4038-817b-e6a847d56b08', 0, 'ec3226947b5845da8f4bcfdcde5e4d1e');
INSERT INTO `money` VALUES ('7cf54c63-d36f-4fd0-9548-b1c0b74c5f44', 0, '2d5e2f34bd0d429b96420d351f5f9da2');
INSERT INTO `money` VALUES ('9cb61728-bb75-403e-bdac-f744295aee61', 0, '78f1afca3a284609ab9e08e137837dcc');
INSERT INTO `money` VALUES ('b135f319-fd3c-42e2-8ba4-5d8cea14f10a', 0, '7c99a5eadc5a4fa08aad783cd1108fbe');
INSERT INTO `money` VALUES ('b317caf7-6721-4834-9581-654eebc7be54', 0, 'fe50e88862924479b262772f99918fb0');
INSERT INTO `money` VALUES ('d38dcd1e-c87c-48d6-a529-f1d0c30d58ec', 0, '1b2fb9b538274f4393e23ce6aaef0a3c');

-- ----------------------------
-- Table structure for transaction_message
-- ----------------------------
DROP TABLE IF EXISTS `transaction_message`;
CREATE TABLE `transaction_message`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '主键ID',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
  `editor` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者',
  `creater` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `message_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '消息ID',
  `message_body` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息内容',
  `message_data_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息数据类型',
  `consumer_queue` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '消费队列',
  `message_send_times` smallint(6) NOT NULL DEFAULT 0 COMMENT '消息重发次数',
  `areadly_dead` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '是否死亡',
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '状态',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `field1` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段1',
  `field2` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段2',
  `field3` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段3',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `AK_Key_2`(`message_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of transaction_message
-- ----------------------------
INSERT INTO `transaction_message` VALUES ('2bff78f3b42044c28d7f9d703f804155', 0, NULL, NULL, '2019-08-27 12:32:03', '2019-08-27 12:05:07', '098f6f09405e4595a8c924b7bc8297d5', '{\"age\":44,\"id\":\"9211484e9d5e45eeb40a5534578a00e1\",\"userName\":\"1a8d62\"}', 'saveUser', 'ACCOUNTING_NOTIFY', 7, 'YES', 'SENDING', NULL, NULL, NULL, NULL);
INSERT INTO `transaction_message` VALUES ('78be6ac7d17c49fc92074740053c6ced', 0, NULL, NULL, '2019-08-27 14:10:42', '2019-08-27 14:10:42', 'deb351baff874272a1c6747ba0aa60df', '{\"age\":47,\"id\":\"9239dcf5943a4437b8ee19c13c8170f6\",\"userName\":\"ddba66\"}', 'saveUser', 'ACCOUNTING_NOTIFY', 0, 'NO', 'WAITING_CONFIRM', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `age` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1b2fb9b538274f4393e23ce6aaef0a3c', 'd47dfc', 74);
INSERT INTO `user` VALUES ('2d5e2f34bd0d429b96420d351f5f9da2', '6dc835', 57);
INSERT INTO `user` VALUES ('3d203e0ca62543049ffa925ba2bbf893', '0e1835', 58);
INSERT INTO `user` VALUES ('78f1afca3a284609ab9e08e137837dcc', '8264d8', 76);
INSERT INTO `user` VALUES ('7c99a5eadc5a4fa08aad783cd1108fbe', '2bf18e', 95);
INSERT INTO `user` VALUES ('9211484e9d5e45eeb40a5534578a00e1', '1a8d62', 44);
INSERT INTO `user` VALUES ('ec3226947b5845da8f4bcfdcde5e4d1e', '7f08f0', 11);
INSERT INTO `user` VALUES ('fe50e88862924479b262772f99918fb0', '973e4a', 30);

SET FOREIGN_KEY_CHECKS = 1;
