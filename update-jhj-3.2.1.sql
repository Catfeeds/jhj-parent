ALTER TABLE `partner_service_type` ADD `staff_price_incoming` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '非会员价员工提成' AFTER `price`;


ALTER TABLE `partner_service_type` ADD `staff_mprice_incoming` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '会员价员工提成' AFTER `mprice`;