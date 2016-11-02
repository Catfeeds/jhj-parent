ALTER TABLE `org_staffs` ADD `staff_code` VARCHAR(3) NOT NULL COMMENT '员工编号' ;

ALTER TABLE `users` ADD `is_vip` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '0 = 否（默认） 1= 是' ;

ALTER TABLE `partner_service_type` ADD `mprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '会员价格' AFTER `price`;

ALTER TABLE `order_cards` ADD `referee` VARCHAR(20) NULL COMMENT '员工编号' ;