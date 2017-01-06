SELECT distinct user_id FROM `order_cards` WHERE order_status = 1


select * from users where id not in (SELECT distinct user_id FROM `order_cards` WHERE order_status = 1
) and rest_money > 0


select mobile as 手机号, rest_money as 余额, from_unixtime(add_time) as 注册时间,
from_unixtime(update_time) as 最后更新时间 from users where id not in (SELECT distinct user_id FROM `order_cards` WHERE order_status = 1 ) and rest_money > 0



//10.20之后充值的用户
select * from user_id where id in (
select distinct user_id from order_cards where add_time >= 1476892800 and order_status = 1)

//找出10.29之后充值的用户，之前有充值记录的
select * from order_cards where order_status = 1 and add_time < 1476892800  and user_id in (select distinct user_id from order_cards where add_time >= 1476892800 and order_status = 1) 

//--------------------------用户明细整理sql
//清空用户余额，清空用户消费明细
update users set rest_money = 0;
truncate user_detail_pay;

//更新消费明细，充值之前的余额清0
update
(SELECT * FROM `user_detail_pay` where order_type = 1 ) as a,
user_detail_pay as b 
set b.rest_money = 0
where a.user_id = b.user_id and a.add_time > b.add_time

//余额为负数的，清0
update users set rest_money = 0 where rest_money < 0


CREATE TABLE `user_once` (
  `mobile` varchar(11) DEFAULT NULL,
  `rest_money` decimal(9,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `user_once`
--

INSERT INTO `user_once` (`mobile`, `rest_money`) VALUES
('13466330824', '365.00'),
('15001008009', '3260.00'),
('13906009127', '420.00'),
('15110271031', '1057.00'),
('13522229973', '886.00'),
('13810265737', '0.00'),
('13810815398', '700.00'),
('18910318907', '200.00'),
('18910010912', '0.00'),
('13801112336', '55.00'),
('13521172999', '1632.00'),
('15010569668', '16.00'),
('13683544156', '1907.00'),
('18610505099', '606.00'),
('18611911129', '1746.00'),
('18301185152', '0.00'),
('13641165288', '1306.00'),
('13501006327', '1055.00'),
('18600248405', '0.00'),
('18610562626', '463.00'),
('13699106584', '463.50');


select a.mobile, a.rest_money, b.rest_money as rest_money_excel, 
(case when a.rest_money > b.rest_money then b.rest_money else a.rest_money end) as rest_money_new

from users as a , user_once as b where a.mobile = b.mobile


update users as a , user_once as b set a.rest_money = b.rest_money where a.mobile = b.mobile and a.rest_money > b.rest_money











//-------------------------服务人员明细整理
update org_staff_finance set total_incoming = 0, total_dept = 0, total_cash = 0, rest_money = 0, is_black = 0;

truncate org_staff_detail_pay;
truncate org_staff_detail_dept;
truncate order_dispatch_prices;

update org_staff_finance set is_black = 1 where total_dept >= 1000;
update org_staff_finance set rest_money = total_incoming - total_cash;


//验证数据sql

select sum(totalOrderPay) from
(

	select sum(order_pay) as totalOrderPay from order_prices where order_id in 
	(
	select id from orders where order_status in (7,8) and update_time >=1480521600 and update_time <= 1483199999
	)
	union all
	select sum(order_pay) as totalOrderPay from order_price_ext where order_id in 
	(
	select id from orders where order_status in (7,8) and update_time >=1480521600 and update_time <= 1483199999
	) and order_status = 2
) as T

select sum(order_pay) as totalOrderPay from order_dispatch_prices where order_id in 
	(
	select id from orders where order_status in (7,8) and update_time >=1480521600 and update_time <= 1483199999
	)


//验证收入是否正确
select a.staff_id, a.total_incoming, b.total_incoming  from 
jhj_new.org_staff_finance as a, jhj.org_staff_finance  as b
where a.staff_id = b.staff_id and a.total_incoming <> b.total_incoming;

select sum(order_pay) from order_prices where order_id in (

select id from orders where order_id in (select order_id from order_dispatchs where staff_id = 73 and dispatch_status = 1) and order_status in (7,8) and add_time >= 1476892800
)



//增加数据库更新脚本
ALTER TABLE `user_detail_pay` ADD `rest_money` DECIMAL(9,2) NOT NULL DEFAULT '0' COMMENT '余额' AFTER `order_pay`;

UPDATE `admin_authority` SET `position` = '0' WHERE `admin_authority`.`id` = 20;
UPDATE `admin_authority` SET `position` = '1' WHERE `admin_authority`.`id` = 74;
UPDATE `admin_authority` SET `position` = '2' WHERE `admin_authority`.`id` = 75;
UPDATE `admin_authority` SET `position` = '3' WHERE `admin_authority`.`id` = 83;
UPDATE `admin_authority` SET `position` = '4' WHERE `admin_authority`.`id` = 94;
UPDATE `admin_authority` SET `position` = '5' WHERE `admin_authority`.`id` = 68;
UPDATE `admin_authority` SET `name` = '服务人员财务总表' WHERE `admin_authority`.`id` = 68;
UPDATE `admin_authority` SET `position` = '6' WHERE `admin_authority`.`id` = 66;
UPDATE `admin_authority` SET `position` = '7' WHERE `admin_authority`.`id` = 65;
UPDATE `admin_authority` SET `position` = '8' WHERE `admin_authority`.`id` = 67;
UPDATE `admin_authority` SET `position` = '9' WHERE `admin_authority`.`id` = 21;
UPDATE `admin_authority` SET `position` = '10' WHERE `admin_authority`.`id` = 22;
UPDATE `admin_authority` SET `position` = '11' WHERE `admin_authority`.`id` = 49;

CREATE TABLE `order_dispatch_prices` (
  `id` int(11) UNSIGNED NOT NULL COMMENT '主键',
  `user_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '用户ID',
  `mobile` char(11) NOT NULL COMMENT '用户手机号',
  `is_vip` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否会员',
  `order_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
  `order_type` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '订单类型',
  `service_type_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '服务类型',
  `order_status` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '订单状态',
  `addr_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '地址ID',
  `addr` varchar(255) NOT NULL COMMENT '详细地址',
  `order_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '下单时间',
  `service_date` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '服务日期',
  `service_hours` double NOT NULL DEFAULT '0' COMMENT '服务小时',
  `staff_num` smallint(4) UNSIGNED NOT NULL DEFAULT '0' COMMENT '派工人数',
  `org_id` int(11) UNSIGNED DEFAULT '0' COMMENT '云店ID',
  `parent_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '门店ID',
  `staff_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '工作人员ID,默认为0',
  `staff_name` varchar(20) NOT NULL COMMENT '工作人员姓名',
  `staff_mobile` char(11) NOT NULL COMMENT '工作人员手机号',
  `dispatch_status` tinyint(1) UNSIGNED NOT NULL DEFAULT '1' COMMENT '派工状态  1 = 有效  0 = 无效',
  `user_addr_distance` mediumint(8) UNSIGNED NOT NULL COMMENT '服务人员距离用户地址多远',
  `pay_type` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '支付方式',
  `order_money` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
  `order_pay` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '订单支付金额',
  `order_pay_incoming` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '订单支付金额收入',
  `coupon_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '优惠劵ID',
  `order_pay_coupon` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '优惠劵金额',
  `order_pay_coupon_incoming` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '优惠劵补贴',
  `order_pay_ext_diff` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '补差价收入',
  `order_pay_ext_diff_incoming` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '补差价收入',
  `order_pay_ext_overwork` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '补时收入',
  `order_pay_ext_overwork_incoming` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '订单加时收入',
  `incoming_percent` decimal(2,2) NOT NULL DEFAULT '0.00' COMMENT '回扣比例',
  `total_order_incoming` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '订单收入',
  `total_order_dept` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '订单欠款',
  `add_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '添加时间戳',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '更新时间戳'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `order_dispatch_prices`
--
ALTER TABLE `order_dispatch_prices`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `mobile` (`mobile`),
  ADD KEY `order_no` (`order_no`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `order_dispatch_prices`
--
ALTER TABLE `order_dispatch_prices`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键';



ALTER TABLE `order_dispatch_prices` ADD `total_order_money` DECIMAL(9,2) NOT NULL DEFAULT '0' COMMENT '订单总金额' AFTER `incoming_percent`;


ALTER TABLE `order_dispatch_prices` ADD `total_order_pay` DECIMAL(9,2) NOT NULL DEFAULT '0' COMMENT '订单总支付金额' AFTER `total_order_money`;

ALTER TABLE `order_dispatch_prices` ADD `order_pay_ext_diff_pay_type` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '补差价支付方式' AFTER `order_pay_ext_diff`;

update order_dispatch_prices set total_order_pay = order_pay, total_order_money = order_pay + order_pay_coupon;

update order_dispatch_prices set total_order_pay =  ROUND(total_order_pay / staff_num, 2);

update order_dispatch_prices as a, order_prices as b set a.order_pay = b.order_pay where a.order_id = b.order_id;

update order_dispatch_prices set order_pay = ROUND(order_pay / staff_num, 2);

update order_dispatch_prices as a, order_price_ext as b set a.order_pay_ext_diff_pay_type = b.pay_type where a.order_id = b.order_id and b.order_ext_type = 0 and b.order_status = 1




//验证sql

select order_pay, order_pay_ext_diff, order_pay_ext_overwork, total_order_pay, 
ROUND( (order_pay  +  order_pay_ext_diff + order_pay_ext_overwork) / staff_num ,2) as avg from order_dispatch_prices where  ROUND( (order_pay  +  order_pay_ext_diff + order_pay_ext_overwork) / staff_num ,2) <> total_order_pay 

