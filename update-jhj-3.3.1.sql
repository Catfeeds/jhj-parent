ALTER TABLE `partner_service_type` ADD `aprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动加时价格' AFTER `staff_mpprice`, ADD `staff_aprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动加时服务人员提成' AFTER `aprice`, ADD `apprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动套餐价格' AFTER `staff_aprice`, ADD `staff_apprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动套餐价格服务人员提成' AFTER `apprice`;



ALTER TABLE `dict_service_addons` ADD `aprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动价' AFTER `staff_dis_price`, ADD `staff_aprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动价服务人员提成' AFTER `aprice`;



INSERT INTO `cooperative_business` (`id`, `business_name`, `app_name`, `business_login_name`, `business_pass_word`, `enable`, `role_id`, `add_time`, `update_time`) VALUES
(123, '微网站社区一', '微网站社区一', 'wwzsqy', '25d55ad283aa400af464c76d713c07ad', 1, 8, 1493798749, 1493798749),
(124, '微网站社区二', '微网站社区二', 'wwzsqe', '25d55ad283aa400af464c76d713c07ad', 1, 8, 1493798796, 1493798796),
(125, '微网站社区三', '微网站社区三', 'wwzsqs', '25d55ad283aa400af464c76d713c07ad', 1, 8, 1493798817, 1493798817),
(126, '微网站社区四', '微网站社区四', 'wwzsqsi', '25d55ad283aa400af464c76d713c07ad', 1, 8, 1493798850, 1493798850);