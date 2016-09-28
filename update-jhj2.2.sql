

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
where a.staff_id = b.staff_id;


update `partner_service_type` set enable = 0 WHERE parent_id = 23 and service_type_id <> 28;

update `partner_service_type` set enable = 0 WHERE parent_id = 24 and service_type_id <> 29;	

UPDATE `partner_service_type` SET `name` = '金牌保洁' WHERE `partner_service_type`.`service_type_id` = 28;

UPDATE `partner_service_type` SET `name` = '厨娘烧饭' WHERE `partner_service_type`.`service_type_id` = 29;

update `partner_service_type` set enable = 0 WHERE parent_id = 25 or service_type_id=25;


ALTER TABLE `partner_service_type` ADD `is_auto` TINYINT(1) UNSIGNED NOT NULL DEFAULT '1' COMMENT '是否自动派工 0 = 否 1 = 是' AFTER `service_content`, ADD `is_multi` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '一单多人 0 = 否 1 = 是' AFTER `is_auto`;


update `org_staffs` set head_img = 'http://www.jia-he-jia.com/u/img/default-head-img.png' WHERE head_img = '';


ALTER TABLE `orders` ADD `order_op_from` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '运营平台下单类型 0 = 默认自助 1 = 来电下单 ' AFTER `order_from`;	

UPDATE `admin_authority` SET `url` = '/order/order-exp-list' WHERE `admin_authority`.`id` = 70;
UPDATE `admin_authority` SET `match_url` = '/order' WHERE `admin_authority`.`id` = 70;


update `orders` set order_type = 1 WHERE service_type in (SELECT service_type_id FROM `partner_service_type` WHERE parent_id = 26);

	ALTER TABLE `order_prices` CHANGE `pay_type` `pay_type` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '付款方式 0 = 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付 4 = 上门刷卡（保留，站位） 6 = 现金支付 7 = 第三方支付';