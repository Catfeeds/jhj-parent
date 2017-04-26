ALTER TABLE order_log ADD `user_id` int(11) NOT NULL COMMENT '录入人ID';
ALTER TABLE order_log ADD `user_name` varchar(50) NOT NULL COMMENT '录入人';
ALTER table order_log ADD `user_type` smallint(4) NOT NULL COMMENT '用户类型，0：用户，1，服务人员，2：后台管理人员';
ALTER table order_log ADD `action` varchar(1000) NOT NULL COMMENT '操作';

ALTER TABLE `orders` ADD `order_done_time` INT(11) UNSIGNED NULL DEFAULT '0' COMMENT '订单完成时间' AFTER `update_time`;

update `orders` set order_done_time = update_time where order_status in (7,8);


ALTER TABLE `order_prices` ADD `order_origin_price` DECIMAL(9,2) NOT NULL DEFAULT '0' COMMENT '订单原价' AFTER `coupon_id`;

ALTER TABLE `order_prices` ADD `order_prime_price` DECIMAL(9,2) NOT NULL DEFAULT '0' COMMENT '订单原价' AFTER `order_origin_price`;


update order_prices set order_origin_price = order_pay;
update order_prices set order_prime_price = order_pay;


