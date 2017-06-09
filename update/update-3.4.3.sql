CREATE TABLE `order_extend` (
  `id` int(11) UNSIGNED NOT NULL COMMENT '主键',
  `user_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '用户ID',
  `order_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
  `group_code` varchar(64) NOT NULL COMMENT '团购卷',
  `add_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '添加时间戳'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `order_extend`
--
ALTER TABLE `order_extend`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `order_extend`
--
ALTER TABLE `order_extend`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键';



alter table orders add `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识：0：正常，1：删除';