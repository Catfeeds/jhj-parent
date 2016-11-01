ALTER TABLE `org_staffs` ADD `staff_code` VARCHAR(3) NOT NULL COMMENT '员工编号' ;

ALTER TABLE `users` ADD `is_vip` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '0 = 否（默认） 1= 是' ;