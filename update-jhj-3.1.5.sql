/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : jhj3.0

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2016-12-12 16:35:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for market_sms
-- ----------------------------
DROP TABLE IF EXISTS `market_sms`;
CREATE TABLE `market_sms` (
  `market_sms_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sms_temp_id` int(11) NOT NULL COMMENT '短信模板',
  `user_group_type` smallint(4) NOT NULL COMMENT ' 0 = 全部用户 1 = 会员用户 2 = 非会员用户 3 = 1个月内未使用用户 4 = 3个月内未使用用户 5= 6个月内未使用用户 6 = 注册未使用用户 ',
  `total_send` mediumint(8) NOT NULL DEFAULT '0' COMMENT '应发送人数',
  `total_sended` mediumint(8) NOT NULL DEFAULT '0' COMMENT '已发送人数',
  `total_fail` mediumint(8) NOT NULL DEFAULT '0' COMMENT '失败人数',
  `add_time` int(11) NOT NULL,
  `update_time` int(11) NOT NULL,
  PRIMARY KEY (`market_sms_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员营销表';

-- ----------------------------
-- Table structure for market_sms_fail
-- ----------------------------
DROP TABLE IF EXISTS `market_sms_fail`;
CREATE TABLE `market_sms_fail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `market_sms_id` int(11) NOT NULL COMMENT '会员营销ID',
  `user_id` int(11) NOT NULL COMMENT '用户Id',
  `mobile` char(11) NOT NULL COMMENT '用户手机号',
  `sms_result` varchar(20) NOT NULL COMMENT '发送返回',
  `sms_msg` varchar(64) NOT NULL COMMENT '发送失败信息',
  `add_time` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员营销表发送失败明细';
