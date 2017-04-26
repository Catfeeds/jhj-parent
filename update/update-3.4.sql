/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : jhj3.0

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2017-04-13 18:38:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for period_order
-- ----------------------------
DROP TABLE IF EXISTS `period_order`;
CREATE TABLE `period_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
  `user_id` int(11) unsigned NOT NULL,
  `mobile` char(11) NOT NULL COMMENT '用户手机号',
  `addr_id` int(11) NOT NULL COMMENT '地址id',
  `order_type` int(11) unsigned NOT NULL COMMENT '订单类型',
  `order_status` int(11) unsigned NOT NULL COMMENT '订单状态 0 = 已取消 1 = 服务中 2 = 服务完成·',
  `order_money` decimal(10,2) unsigned NOT NULL COMMENT '原价',
  `order_price` decimal(10,2) unsigned NOT NULL COMMENT '会员价',
  `user_coupons_id` int(11) unsigned NOT NULL COMMENT '优惠劵',
  `service_type_id` int(11) unsigned NOT NULL COMMENT '定制类型id',
  `order_from` int(11) NOT NULL COMMENT '订单来源',
  `remarks` varchar(500) NOT NULL COMMENT '备注',
  `add_time` int(11) unsigned NOT NULL,
  `update_time` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for period_order_addons
-- ----------------------------
DROP TABLE IF EXISTS `period_order_addons`;
CREATE TABLE `period_order_addons` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `period_order_id` int(11) NOT NULL COMMENT '定制订单id',
  `period_order_no` varchar(32) NOT NULL COMMENT '定制订单号',
  `service_type_id` int(11) NOT NULL COMMENT '定制服务类型id',
  `service_addon_id` int(11) NOT NULL COMMENT '定制服务子类型id',
  `price` decimal(10,2) NOT NULL,
  `vip_price` decimal(10,2) NOT NULL,
  `num` int(11) NOT NULL COMMENT '次数',
  `add_time` datetime NOT NULL COMMENT '添加时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for period_service_type
-- ----------------------------
DROP TABLE IF EXISTS `period_service_type`;
CREATE TABLE `period_service_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '定制名称',
  `service_type_id` int(11) unsigned NOT NULL COMMENT '定制类型',
  `service_addon_id` int(11) unsigned NOT NULL COMMENT '定制子类型',
  `price` decimal(10,2) unsigned NOT NULL COMMENT '原价',
  `vip_price` decimal(10,2) unsigned NOT NULL COMMENT '会员价',
  `num` int(11) unsigned NOT NULL COMMENT '次数',
  `punit` varchar(20) NOT NULL COMMENT '频次单位',
  `total` varchar(20) NOT NULL COMMENT '半年一次',
  `enbale` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '是否有效：0，有效；1:，无效',
  `remarks` varchar(500) NOT NULL COMMENT '备注',
  `add_time` int(11) unsigned NOT NULL COMMENT '添加时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
