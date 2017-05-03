ALTER TABLE `partner_service_type` ADD `aprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动加时价格' AFTER `staff_mpprice`, ADD `staff_aprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动加时服务人员提成' AFTER `aprice`, ADD `apprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动套餐价格' AFTER `staff_aprice`, ADD `staff_apprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动套餐价格服务人员提成' AFTER `apprice`;



ALTER TABLE `dict_service_addons` ADD `aprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动价' AFTER `staff_dis_price`, ADD `staff_aprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动价服务人员提成' AFTER `aprice`;