ALTER TABLE `partner_service_type` ADD `staff_price` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '非会员价员工提成' AFTER `price`;


ALTER TABLE `partner_service_type` ADD `staff_mprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '会员价员工提成' AFTER `mprice`;


ALTER TABLE `partner_service_type` ADD `staff_pprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '非会员价员工提成' AFTER `pprice`;


ALTER TABLE `partner_service_type` ADD `staff_mpprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '会员价员工提成' AFTER `mpprice`;


ALTER TABLE `dict_service_addons` ADD `staff_price` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '会员价员工提成' AFTER `price`;


ALTER TABLE `dict_service_addons` ADD `staff_dis_price` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '会员价员工提成' AFTER `dis_price`;