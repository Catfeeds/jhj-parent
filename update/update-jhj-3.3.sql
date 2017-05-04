ALTER TABLE `user_detail_pay` ADD `remarks` varchar(300) NOT NULL COMMENT '描述';


CREATE TABLE `user_sms_notice` (
  `id` int(11) UNSIGNED NOT NULL COMMENT '主键',
  `user_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '用户id',
  `mobile` char(11) NOT NULL COMMENT '手机号',
  `user_type` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '用户类型 0 = 服务人员 1 = 用户',
  `last_month` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '最近几个月发送的',
  `sms_template_id` varchar(20) NOT NULL COMMENT '短信发送模板ID',
  `remarks` varchar(128) NOT NULL COMMENT '备注',
  `is_suceess` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '0 = 发送失败 1 = 发送成功',
  `sms_return` varchar(255) NOT NULL COMMENT '短信平台返回信息.',
  `add_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '添加时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户发送验证码表';

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user_sms_notice`
--
ALTER TABLE `user_sms_notice`
  ADD PRIMARY KEY (`id`),
  ADD KEY `mobile` (`mobile`),
  ADD KEY `user_id` (`user_id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `user_sms_notice`
--
ALTER TABLE `user_sms_notice`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键';


alter table user_addrs add fulltext index(name,address) WITH PARSER ngram;

ALTER TABLE `orders` ADD `order_addr` VARCHAR(128) NOT NULL COMMENT '详细地址' AFTER `addr_id`;

update orders as a, user_addrs as b set a.order_addr = concat(b.name, b.address, b.addr) where a.addr_id = b.id;


ALTER TABLE `orders` ADD FULLTEXT(`order_addr`)  WITH PARSER ngram;