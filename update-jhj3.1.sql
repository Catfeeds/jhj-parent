ALTER TABLE `org_staffs` ADD `staff_code` VARCHAR(3) NOT NULL COMMENT '员工编号' ;

ALTER TABLE `users` ADD `is_vip` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否会员 0 = 否 1 = 是' AFTER `user_type`;

ALTER TABLE `partner_service_type` ADD `mprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '会员价格' AFTER `price`;


ALTER TABLE `order_cards` ADD `referee` VARCHAR(20) NULL COMMENT '员工编号' ;

ALTER TABLE `order_price_ext` ADD `order_ext_type` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '类型 0 = 补交差价 1 = 加时' AFTER `order_no_ext`;

ALTER TABLE `order_price_ext` ADD `service_hour` DOUBLE NOT NULL DEFAULT '0' COMMENT '加时小时数' AFTER `order_ext_type`;

ALTER TABLE `org_staff_detail_pay` CHANGE `order_type` `order_type` TINYINT(1) NOT NULL COMMENT '订单类型 0 = 钟点工订单 1 = 深度保洁订单 2 = 助理订单 3 = 送酒订单 4 = 还款订单 5 = 提现 6 = 补贴 7 = 利息 8= 杂费 9 = 订单补时';
