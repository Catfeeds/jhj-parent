ALTER TABLE `org_staffs` ADD `staff_code` VARCHAR(10) NOT NULL COMMENT '员工编号' ;

ALTER TABLE `users` ADD `is_vip` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否会员 0 = 否 1 = 是' AFTER `user_type`;

ALTER TABLE `partner_service_type` ADD `mprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '会员价格' AFTER `price`;

ALTER TABLE `partner_service_type` ADD `pprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '套餐价格' AFTER `mprice`;

ALTER TABLE `partner_service_type` CHANGE `service_hour` `service_hour` DOUBLE NOT NULL DEFAULT '0' COMMENT '起步服务小时';

ALTER TABLE `partner_service_type` CHANGE `mprice` `mprice` DECIMAL(5,2) NOT NULL DEFAULT '0.00' COMMENT '会员价格(每小时)';

ALTER TABLE `partner_service_type` ADD `mpprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '会员套餐价' AFTER `pprice`;


ALTER TABLE `order_cards` ADD `referee` VARCHAR(20) NULL COMMENT '员工编号' ;

ALTER TABLE `order_price_ext` ADD `order_ext_type` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '类型 0 = 补交差价 1 = 加时' AFTER `order_no_ext`;

ALTER TABLE `order_price_ext` ADD `service_hour` DOUBLE NOT NULL DEFAULT '0' COMMENT '加时小时数' AFTER `order_ext_type`;

ALTER TABLE `org_staff_detail_pay` CHANGE `order_type` `order_type` TINYINT(1) NOT NULL COMMENT '订单类型 0 = 钟点工订单 1 = 深度保洁订单 2 = 助理订单 3 = 送酒订单 4 = 还款订单 5 = 提现 6 = 补贴 7 = 利息 8= 杂费 9 = 订单补时';

ALTER TABLE `order_cards` ADD `parent_id` INT(11) NOT NULL COMMENT '门店ID' ;
ALTER TABLE `order_cards` ADD `org_id` INT(11) NOT NULL COMMENT '云店ID' ;

ALTER TABLE `orders` ADD `staff_nums` SMALLINT(4) UNSIGNED NOT NULL DEFAULT '0' COMMENT '派工人数' AFTER `service_hour`;


update orders as a, (SELECT order_no, count(*) as c FROM `order_dispatchs` WHERE dispatch_status = 1 group by order_no) as b set a.staff_nums = b.c where a.order_no = b.order_no;

ALTER TABLE `order_rates` CHANGE `am_id` `staff_id` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '服务人员ID';


ALTER TABLE `order_rates` CHANGE `rate_type` `rate_arrival` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '到达时间 0 = 准时 1 = 迟到';


ALTER TABLE `order_rates` CHANGE `rate_value` `rate_attitude` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '服务态度 1-5';


ALTER TABLE `order_rates` ADD `rate_skill` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '服务技能 1-5' AFTER `rate_attitude`;


ALTER TABLE `order_rates` CHANGE `rate_content` `rate_content` VARCHAR(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评价内容';  


CREATE TABLE `order_rate_img` (
  `id` int(11) UNSIGNED NOT NULL COMMENT '主键',
  `order_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '订单ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `user_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '用户ID',
  `mobile` char(11) NOT NULL COMMENT '手机号',
  `img_url` varchar(1024) NOT NULL COMMENT '图片地址',
  `add_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '添加时间戳'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `order_rate_img`
--
ALTER TABLE `order_rate_img`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `order_rate_img`
--
ALTER TABLE `order_rate_img`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键';



CREATE TABLE `order_appoint` (
  `id` int(11) UNSIGNED NOT NULL COMMENT '主键',
  `order_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '订单ID',
  `user_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '用户ID',
  `staff_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '员工ID',
  `add_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '添加时间戳'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单指定派工人员表';

--
-- Indexes for dumped tables
--

--
-- Indexes for table `order_appoint`
--
ALTER TABLE `order_appoint`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `order_appoint`
--
ALTER TABLE `order_appoint`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键';


update `user_trail_history` set user_type = 0;

update `user_trail_real` set user_type = 0;

update users set is_vip = 1 where id in ( SELECT user_id FROM `order_cards` WHERE card_money >= 1000 and order_status = 1 );


update`org_staffs` set staff_code = (1000 + staff_id);  
