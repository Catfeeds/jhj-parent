ALTER TABLE `order_dispatchs` ADD `allocate` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '派工依据 0 = 合理分配 1 = 效率优先' AFTER `apply_time`, ADD `allocate_reason` VARCHAR(20) NOT NULL COMMENT '派工依据说明' AFTER `allocate`;


update order_dispatchs set allocate = 1, allocate_reason = '效率优先';