

ALTER TABLE `org_staff_finance` ADD `is_black` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否在黑名单 0 = 否 1 = 是' AFTER `rest_money`;

ALTER TABLE `user_push_bind` CHANGE `user_type` `user_type` TINYINT(1) NOT NULL COMMENT '用户类型 0 = 服务人员 1 = 用户';

ALTER TABLE `msg` CHANGE `user_type` `user_type` TINYINT(1) NOT NULL COMMENT '用户类型 0 = 服务人员 1 = 用户';

ALTER TABLE `msg_readed` CHANGE `user_type` `user_type` TINYINT(1) NOT NULL COMMENT '用户类型 0 = 服务人员 1 = 用户';

ALTER TABLE `user_trail_real` CHANGE `user_type` `user_type` TINYINT(1) NOT NULL COMMENT '用户类型 0 = 服务人员 1 = 用户';

update `msg` set user_type = 0;

update `msg_readed` set user_type = 0;

update `user_trail_real` set user_type = 0;


ALTER TABLE `org_staff_skill` ADD `org_id` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '云店ID' AFTER `add_time`, ADD `parent_id` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '门店ID' AFTER `org_id`;


update org_staff_skill as a, org_staffs as b 
set a.org_id = b.org_id, a.parent_id = b.parent_org_id
where a.staff_id = b.staff_id;


ALTER TABLE `order_dispatchs` ADD `parent_id` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '门店ID' AFTER `org_id`;

update order_dispatchs as a, org_staffs as b 
set a.parent_id = b.parent_org_id
where a.staff_id = b.staff_id