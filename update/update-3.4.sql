/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : jhj3.0

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2017-05-05 16:52:43
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
  `order_status` int(11) unsigned NOT NULL COMMENT '订单状态 0 = 已取消 1 = 未支付 2 = 已支付，3未完成，4已完成',
  `pay_type` int(11) NOT NULL COMMENT '支付方式',
  `order_money` decimal(10,2) unsigned NOT NULL COMMENT '原价',
  `order_price` decimal(10,2) unsigned NOT NULL COMMENT '会员价',
  `user_coupons_id` int(11) unsigned NOT NULL COMMENT '优惠劵',
  `package_type` int(11) unsigned NOT NULL COMMENT '定制套餐id',
  `order_from` int(11) NOT NULL COMMENT '订单来源：0 = APP  1 = 微网站  2 = 管理后台',
  `remarks` varchar(500) NOT NULL COMMENT '备注',
  `add_time` int(11) unsigned NOT NULL,
  `update_time` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of period_order
-- ----------------------------
INSERT INTO `period_order` VALUES ('1', '860327161301565440', '4867', '', '4', '1', '1', '0', '8568.00', '0.01', '0', '1', '1', '', '1493952954', '1493952954');
INSERT INTO `period_order` VALUES ('2', '860327943807696896', '4867', '15201023689', '4', '1', '1', '0', '9928.00', '0.01', '0', '1', '1', '', '1493953140', '1493953140');
INSERT INTO `period_order` VALUES ('3', '860328485330092032', '4867', '15201023689', '4', '1', '2', '0', '8348.00', '0.01', '0', '1', '1', '', '1493953269', '1493953273');
INSERT INTO `period_order` VALUES ('4', '860329044091076608', '4867', '15201023689', '4', '1', '1', '0', '10888.00', '0.01', '0', '1', '1', '', '1493953402', '1493953402');
INSERT INTO `period_order` VALUES ('5', '860329702953320448', '4867', '15201023689', '4', '1', '1', '2', '8698.00', '0.01', '0', '1', '1', '', '1493953559', '1493953559');
INSERT INTO `period_order` VALUES ('6', '860339874924855296', '4867', '15201023689', '4', '1', '1', '0', '8308.00', '0.01', '0', '1', '1', '', '1493955985', '1493955985');

-- ----------------------------
-- Table structure for period_order_addons
-- ----------------------------
DROP TABLE IF EXISTS `period_order_addons`;
CREATE TABLE `period_order_addons` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `period_order_id` int(11) NOT NULL COMMENT '定制订单id',
  `period_order_no` varchar(32) NOT NULL COMMENT '定制订单号',
  `service_type_id` int(11) NOT NULL COMMENT '定制服务类型id',
  `service_addon_id` int(11) NOT NULL COMMENT '定制服务子类型id',
  `period_service_addon_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '定制服务配置ID',
  `price` decimal(10,2) NOT NULL,
  `vip_price` decimal(10,2) NOT NULL,
  `num` int(11) NOT NULL COMMENT '次数',
  `add_time` datetime NOT NULL COMMENT '添加时间',
  `package_type` int(11) DEFAULT '0' COMMENT '套餐id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of period_order_addons
-- ----------------------------
INSERT INTO `period_order_addons` VALUES ('1', '1', '1', '860327161301565440', '28', '0', '5', '149.00', '130.00', '52', '2017-05-05 10:55:54', '1');
INSERT INTO `period_order_addons` VALUES ('2', '1', '1', '860327161301565440', '36', '15', '9', '140.00', '120.00', '4', '2017-05-05 10:55:54', '1');
INSERT INTO `period_order_addons` VALUES ('3', '1', '1', '860327161301565440', '51', '23', '10', '130.00', '110.00', '2', '2017-05-05 10:55:54', '1');
INSERT INTO `period_order_addons` VALUES ('4', '4867', '2', '860327943807696896', '28', '0', '5', '149.00', '130.00', '52', '2017-05-05 10:59:01', '1');
INSERT INTO `period_order_addons` VALUES ('5', '4867', '2', '860327943807696896', '34', '11', '7', '320.00', '280.00', '6', '2017-05-05 10:59:01', '1');
INSERT INTO `period_order_addons` VALUES ('6', '4867', '2', '860327943807696896', '51', '23', '10', '130.00', '110.00', '2', '2017-05-05 10:59:01', '1');
INSERT INTO `period_order_addons` VALUES ('7', '4867', '3', '860328485330092032', '28', '0', '5', '149.00', '130.00', '52', '2017-05-05 11:01:10', '1');
INSERT INTO `period_order_addons` VALUES ('8', '4867', '3', '860328485330092032', '50', '0', '11', '150.00', '130.00', '4', '2017-05-05 11:01:10', '1');
INSERT INTO `period_order_addons` VALUES ('9', '4867', '4', '860329044091076608', '28', '0', '5', '149.00', '130.00', '52', '2017-05-05 11:03:23', '1');
INSERT INTO `period_order_addons` VALUES ('10', '4867', '4', '860329044091076608', '34', '11', '7', '320.00', '280.00', '6', '2017-05-05 11:03:23', '1');
INSERT INTO `period_order_addons` VALUES ('11', '4867', '4', '860329044091076608', '51', '23', '10', '130.00', '110.00', '2', '2017-05-05 11:03:23', '1');
INSERT INTO `period_order_addons` VALUES ('12', '4867', '4', '860329044091076608', '50', '0', '11', '150.00', '130.00', '4', '2017-05-05 11:03:23', '1');
INSERT INTO `period_order_addons` VALUES ('13', '4867', '4', '860329044091076608', '60', '21', '12', '140.00', '130.00', '2', '2017-05-05 11:03:23', '1');
INSERT INTO `period_order_addons` VALUES ('14', '4867', '5', '860329702953320448', '28', '0', '5', '149.00', '130.00', '52', '2017-05-05 11:06:00', '1');
INSERT INTO `period_order_addons` VALUES ('15', '4867', '5', '860329702953320448', '54', '28', '8', '130.00', '110.00', '3', '2017-05-05 11:06:00', '1');
INSERT INTO `period_order_addons` VALUES ('16', '4867', '5', '860329702953320448', '36', '15', '9', '140.00', '120.00', '4', '2017-05-05 11:06:00', '1');
INSERT INTO `period_order_addons` VALUES ('17', '4867', '6', '860339874924855296', '28', '0', '5', '149.00', '130.00', '52', '2017-05-05 11:46:25', '1');
INSERT INTO `period_order_addons` VALUES ('18', '4867', '6', '860339874924855296', '36', '15', '9', '210.00', '190.00', '9', '2017-05-05 11:46:25', '1');

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
  `package_type` varchar(50) NOT NULL COMMENT '套餐类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of period_service_type
-- ----------------------------
INSERT INTO `period_service_type` VALUES ('5', '金牌保洁', '28', '0', '149.00', '130.00', '2', '次/周', '52', '0', '定制1', '1493017891', '1');
INSERT INTO `period_service_type` VALUES ('6', '金牌保洁', '28', '0', '149.00', '130.00', '1', '次/周', '26', '0', '定制2', '1493019937', '2');
INSERT INTO `period_service_type` VALUES ('7', '床铺除螨2张双人床', '34', '11', '320.00', '280.00', '6', '次/年', '6', '0', '定制1', '1493021066', '1');
INSERT INTO `period_service_type` VALUES ('8', '擦玻璃', '54', '28', '130.00', '110.00', '3', '次/年', '3', '0', '定制1', '1493028226', '1');
INSERT INTO `period_service_type` VALUES ('9', '清洗油烟机中式', '36', '15', '140.00', '120.00', '4', '次/年', '4', '0', '定制1', '1493028452', '1');
INSERT INTO `period_service_type` VALUES ('10', '清洗空调挂机', '51', '23', '130.00', '110.00', '2', '次/年', '2', '0', '定制1', '1493028770', '1');
INSERT INTO `period_service_type` VALUES ('11', '清洗冰箱再开', '50', '0', '150.00', '130.00', '4', '次/年', '4', '0', '定制1', '1493028908', '1');
INSERT INTO `period_service_type` VALUES ('12', '清洗洗衣机波轮', '60', '21', '180.00', '150.00', '2', '次/年', '2', '0', '定制1', '1493197040', '1');
INSERT INTO `period_service_type` VALUES ('13', '床铺除螨2张双人床', '34', '11', '320.00', '280.00', '4', '次/年', '2', '0', '定制2，3', '1493197258', '2,3');
INSERT INTO `period_service_type` VALUES ('14', '擦玻璃', '54', '0', '130.00', '110.00', '2', '次/年', '1', '0', '定制2,3,4', '1493197452', '2,3,4');
INSERT INTO `period_service_type` VALUES ('15', '床铺除螨2张双人床', '34', '11', '320.00', '280.00', '2', '次/年', '1', '0', '定制4', '1493197574', '4');
INSERT INTO `period_service_type` VALUES ('16', '清洗油烟机中式', '36', '15', '140.00', '120.00', '4', '次/年', '2', '0', '定制2', '1493197829', '2');
INSERT INTO `period_service_type` VALUES ('17', '清洗油烟机中式', '36', '15', '140.00', '120.00', '2', '次/年', '1', '0', '定制3,4', '1493197878', '3,4');
INSERT INTO `period_service_type` VALUES ('18', '清洗空调挂机', '51', '23', '130.00', '110.00', '2', '次/年', '1', '0', '定制2', '1493197979', '2');
INSERT INTO `period_service_type` VALUES ('19', '清洗空调挂机', '51', '23', '130.00', '110.00', '1', '次/年', '1', '0', '定制3,4', '1493198026', '3,4');
INSERT INTO `period_service_type` VALUES ('20', '清洗冰箱再开', '50', '20', '150.00', '130.00', '4', '次/年', '2', '0', '定制2', '1493198134', '2');
INSERT INTO `period_service_type` VALUES ('21', '清洗冰箱再开', '50', '20', '150.00', '130.00', '2', '次/年', '1', '0', '定制3,4', '1493198179', '3,4');
INSERT INTO `period_service_type` VALUES ('22', '清洗洗衣机波轮', '60', '21', '180.00', '150.00', '2', '次/年', '2', '0', '定制2', '1493198254', '2');
INSERT INTO `period_service_type` VALUES ('23', '清洗洗衣机波轮', '60', '21', '180.00', '150.00', '2', '次/年', '1', '0', '定制3,4', '1493198294', '3,4');
INSERT INTO `period_service_type` VALUES ('24', '金牌保洁', '28', '0', '149.00', '130.00', '1', '次/月', '6', '0', '定制4', '1493707268', '4');
INSERT INTO `period_service_type` VALUES ('25', '金牌保洁', '28', '0', '149.00', '130.00', '1', '次/半月', '13', '0', '定制3', '1493707687', '3');
INSERT INTO `period_service_type` VALUES ('26', '清洗微波炉', '78', '0', '100.00', '80.00', '2', '次/年', '2', '0', '定制1,2', '1493967258', '1,2');
INSERT INTO `period_service_type` VALUES ('27', '清洗微波炉', '78', '0', '100.00', '80.00', '1', '次/年', '1', '0', '定制3,4', '1493967297', '3,4');
INSERT INTO `period_service_type` VALUES ('28', '清洗烤箱', '79', '0', '100.00', '80.00', '2', '次/年', '2', '0', '定制1,2', '1493967336', '1,2');
INSERT INTO `period_service_type` VALUES ('29', '清洗烤箱', '79', '0', '100.00', '80.00', '1', '次/年', '1', '0', '定制3,4', '1493967371', '3,4');
INSERT INTO `period_service_type` VALUES ('30', '贴心家事', '80', '0', '100.00', '100.00', '5', '小时/年', '2.5', '0', '定制1', '1493968286', '1');
INSERT INTO `period_service_type` VALUES ('31', '贴心家事', '80', '0', '100.00', '100.00', '3', '小时/年', '1.5', '0', '定制2', '1493968334', '2');
INSERT INTO `period_service_type` VALUES ('32', '贴心家事', '80', '0', '100.00', '100.00', '2', '小时/年', '1', '0', '定制3', '1493968366', '3');
INSERT INTO `period_service_type` VALUES ('33', '贴心家事', '80', '0', '100.00', '100.00', '1', '小时/年', '0.5', '0', '定制4', '1493968397', '4');
INSERT INTO `period_service_type` VALUES ('34', '全屋掸尘清理死角', '81', '0', '150.00', '150.00', '1', '次/年', '1', '0', '定制1,2,3,4', '1493968765', '1,2,3,4');

ALTER TABLE `orders` ADD `period_order_id` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '定制服务订单id' AFTER `remarks_bussiness_confirm`;
