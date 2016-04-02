--
-- 插入之前先把表清空（truncate） `admin_account`
--

TRUNCATE TABLE `admin_account`;
--
-- 转存表中的数据 `admin_account`
--

INSERT INTO `admin_account` (`id`, `version`, `enable`, `name`, `email`, `username`, `password`, `register_time`, `role_id`, `organization_id`, `im_username`, `nickname`) VALUES
(1, 1, 1, 'admin', 'admin@jhj.com', '徐sir', '7832545dc101bc98f65ccde5f93e5d24', '2014-04-02 23:26:40', 1, 1, NULL, '徐sir'),
(2, 0, 1, '店长', 'shuangjing@jia-he-jia.com', 'shuangjing', '5a08b4f7c131be9f692b6c36a201f731', '2015-09-17 14:59:41', 2, 0, NULL, '呵呵'),
(3, 0, 1, '邵毅夫', 'shaoyifu@jia-he-jia.com', 'shaoyifu', '5a08b4f7c131be9f692b6c36a201f731', '2015-09-27 09:52:02', 3, 0, NULL, 'shaoyifu'),
(4, 0, 1, 'tianjiangtao', 'tianjiangtao@jia-he-jia.com', 'tianjiangtao', '5a08b4f7c131be9f692b6c36a201f731', '2015-10-08 18:04:38', 4, 0, NULL, 'tianjiangtao'),
(5, 0, 1, '苗全国', 'miaoquanguo@jia-he-jia.com', 'miaoquanguo', '5a08b4f7c131be9f692b6c36a201f731', '2015-10-31 14:47:43', 5, 0, NULL, 'miaoquanguo'),
(6, 0, 1, 'sujuan', 'sujuan@jia-he-jia.com', 'sujuan', '5a08b4f7c131be9f692b6c36a201f731', '2015-11-04 10:33:12', 6, 0, NULL, 'sujuan'),
(7, 0, 1, '值班账户', 'info@jia-he-jia.com', 'jiahejia', '550c1beab72ec2d3ab345f3b6a225ec5', '2016-01-30 15:01:12', 7, 0, NULL, '值班账户');

--
-- 插入之前先把表清空（truncate） `admin_authority`
--

TRUNCATE TABLE `admin_authority`;
--
-- 转存表中的数据 `admin_authority`
--

INSERT INTO `admin_authority` (`id`, `version`, `enable`, `name`, `level_code`, `position`, `the_value`, `url`, `match_url`, `item_icon`, `parent_id`) VALUES
(1, 3, 0, '欢迎使用', '1', 1, '1', '/home', '^/home$', 'icon-home', 0),
(2, 2, 0, '首页', '1,2', 0, '1', '/home/index', '/home/index', '', 1),
(3, 2, 0, '系统设置', '3', 2, '2', '/setting', '^/setting$', 'icon-cogs', 0),
(4, 4, 0, '用户管理', '3,4', 1, '1', '/account/list', '/account/list|/account/register', '', 3),
(7, 1, 0, '角色管理', '3,7', 2, '2', '/role/list', '/role/list|/role/toRoleForm', '', 3),
(10, 1, 0, '权限管理', '3,10', 3, '3', '/authority/chain', '/authority/chain', '', 3),
(15, 1, 1, '基础配置', '15', 3, '2', '/base/', '/base', 'icon-archive', 0),
(16, 1, 1, '服务类型', '15,16', 1, '1', '/base/service-list', '/base/service-list|/base/serviceTypeForm|/base/serviceAdd-list|/base/serviceAddForm', '', 15),
(17, 1, 1, '广告位管理', '15,17', 2, '1', '/base/ad-list', '/base/ad-list|/base/adForm', '', 15),
(18, 1, 1, '充值卡管理', '15,18', 3, '1', '/base/card-list', '/base/card-list|/base/cardForm', '', 15),
(19, 1, 1, '业务管理', '19', 5, '2', '/bs/', '/bs', 'icon-building', 0),
(20, 1, 1, '门店管理', '19,20', 2, '1', '/bs/org-list', '/bs/org-list|/bs/orgForm', '', 79),
(21, 1, 1, '服务人员', '19,21', 5, '1', '/bs/staff-list', '/bs/staff-list|/bs/orgStaffForm|/order/order-scheduling|/bs/doOrgStaffForm', '', 79),
(22, 1, 1, '助理人员', '19,21', 6, '1', '/bs/am-list', '/bs/am-list|/bs/staffAsForm|/bs/doOrgStaffAsForm', '', 79),
(23, 1, 1, '标签库', '19,23', 7, '1', '/bs/tag-list', '/bs/tag-list|/bs/tagForm|/bs/doTagForm', '', 19),
(24, 1, 1, '赠送优惠券', '19,24', 8, '1', '/bs/recharge-coupon-list', '/bs/recharge-coupon-list|/bs/toRechargeCouponForm|/bs/rechargeCouponForm| /bs/toRechargeCouponUserList', '', 19),
(25, 1, 1, '外部资源', '19,25', 11, '1', '/bs/outsrc-list', '/bs/outsrc-list', '', 19),
(26, 1, 1, '会员管理', '26', 6, '2', '/user/', '/user', 'icon-user', 0),
(27, 1, 1, '会员列表', '26,27', 1, '1', '/user/user-list', '/user/user-list|/user/charge-form|/user/coupons-list', '', 26),
(28, 1, 1, '会员消费明细', '26,28', 2, '1', '/user/user-pay-detail', '/user/user-pay-detail', '', 26),
(29, 1, 1, '订单管理', '29', 7, '2', '/order/', '/order', 'icon-shopping-cart', 0),
(30, 1, 1, '订单列表', '29,30', 7, '1', '/order/order-list', '/order/order-list|/order/orderView', '', 29),
(31, 1, 1, '派工列表', '29,31', 8, '1', '/order/cal-list', '/order/cal-list|', '', 29),
(33, 1, 1, '内容管理', '33', 8, '2', '/cp', '/cp', ' icon-folder-open', 0),
(36, 1, 1, '兑换码优惠券', '36,19', 9, '1', '/bs/convert-coupon-list', '/bs/convert-coupon-list|/bs/toConvertCouponForm|/bs/convertCouponForm', '', 19),
(37, 1, 1, '礼包', '37,19', 10, '1', '/bs/gifts-list', '/bs/gifts-list|/bs/toGiftsForm|/bs/giftsForm', '', 19),
(38, 1, 1, '统计图表', '38', 9, '1', '/chart', '/chart', 'icon-building', 0),
(39, 1, 1, '市场用户图表', '39,38', 1, '1', '/chart/chartUser', '/chart/chartUser', '', 38),
(40, 1, 1, '用户活跃度图表', '40,38', 2, '', '/chart/userLive', '/chart/userLive', '', 38),
(41, 1, 1, '市场订单图表', '41,38', 3, '1', '/chart/chartOrder', '/chart/chartOrder', '', 38),
(42, 1, 1, '市场品类图表', '42,38', 4, '1', '/chart/chartType', '/chart/chartType', '', 38),
(43, 1, 1, '助理品类图表', '43,38', 5, '', '/chart/chartAmType', '/chart/chartAmType', '', 38),
(44, 1, 1, '订单收入图表', '44,38', 6, '1', '/chart/chartOrderRevenue', '/chart/chartOrderRevenue', '', 38),
(45, 1, 1, '品类收入图表', '45,38', 7, '', '/chart/chartTypeRevenue', '/chart/chartTypeRevenue', '', 38),
(46, 1, 1, '助理品类收入图表', '46,38', 8, '', '/chart/chartServiceType', '/chart/chartServiceType', '', 38),
(47, 1, 1, '充值卡销售图表', '47,38', 9, '1', '/chart/chartSaleCard', '/chart/chartSaleCard', '', 38),
(48, 1, 1, '用户余额图表', '48,38', 10, '1', '/chart/chartMoreCard', '/chart/chartMoreCard', '', 38),
(49, 1, 1, '排班情况', '49,31', 9, '1', '/bs/staff-list', '/order', '', 79),
(50, 1, 1, '社区活动列表', '50,33', 1, '1', '/socials/socials-list', '/socials/socials-list|/socials/socials-form', '', 33),
(51, 1, 1, '社区活动记录列表', '51,33', 2, '1', '/socials/social-call-list', '/socials/social-call-list', '', 33),
(53, 1, 1, '验证码列表', '52,3', 4, '', '/user/token-list', '/user/token-list', '', 3),
(55, 1, 1, '提醒订单', '54,29', 4, '1', '/order/remind-order-list', '/order/remind-order-list', '', 29),
(56, 1, 1, '话费订单', '56,29', 5, '', '/order/phone_charge_order_list', '/order/phone_charge_order_list', '', 29),
(57, 1, 1, '话费订单图表', '57,38', 11, '', '/chart/phoneRechargeOrder', '/chart/phoneRechargeOrder', '', 38),
(58, 1, 1, '话费订单收入图表', '58,38', 12, '', '/chart/phoneRechargeRevenue', '/chart/phoneRechargeRevenue', '', 38),
(59, 1, 1, '助理类型分类管理', '59,15', 4, '', '/base/degree_type_list', '/base/degree_type_list', '', 15),
(60, 1, 1, '叮当大学', '60', 10, '2', '/university/', '/university', 'icon-list-alt', 0),
(61, 1, 1, '服务类别列表', '61,60', 0, '2', '/university/partner_list', '/university/partner_list', '', 60),
(62, 1, 1, '培训学习', '62,60', 1, '2', '/university/study_list', '/university/study_list', '', 60),
(63, 1, 1, '题库管理', '63,60', 2, '', '/university/bank_list', '/university/bank_list', '', 60),
(64, 1, 1, '考题管理', '64,60', 3, '', '/university/question_list', '/university/question_list', '', 60),
(65, 1, 1, '服务人员提现列表', '65,29', 10, '', '/staff/cash-list', '/staff/cash-list', '', 79),
(66, 1, 1, '服务人员消费明细表', '66,29', 11, '', '/staff/staffPay-list', '/staff/staffPay-list', '', 79),
(67, 1, 1, '黑名单列表', '67,29', 12, '', '/staff/staffBlack-list', '/staff/staffBlack-list', '', 79),
(68, 1, 1, '服务人员欠款明细列表', '68,29', 13, '', '/staff/staffPayDept-list', '/staff/staffPayDept-list', '', 79),
(69, 1, 1, '消息列表', '69,29', 9, '', '/msg/list', '/msg/list', '', 29),
(70, 1, 1, '助理订单列表', '70,29', 3, '1', '/order/order-am-list', '/order/order-am-list|/order/order-am-view', '', 29),
(71, 1, 1, '配送订单列表', '71,29', 4, '1', '/order/order-del-list', '/order/order-del-list|/order-order-del-view', '', 29),
(72, 1, 1, '钟点工订单列表', '72,29', 1, '1', '/order/order-hour-list', '/order/order-hour-list|/order/order-hour-view', '', 29),
(73, 1, 1, '深度保洁订单列表', '73,29', 2, '1', '/order/order-exp-list', '/order/order-exp-list|/order/order-exp-view', '', 29),
(74, 1, 1, '云店管理', '74,19', 3, '', '/group/group_list', '/group/group_list', '', 79),
(75, 1, 1, '工作人员', '75,19', 4, '', '/newbs/new_staff_list', '/newbs/new_staff_list', '', 79),
(76, 1, 1, '服务类别管理', '76,19', 1, '', '/newbs/service_type_list', '/newbs/service_type_list', '', 19),
(77, 1, 1, '会员充值', '77,26', 3, '', '/user/finance_user-list', '/user', '', 26),
(78, 1, 1, '财务充值记录', '78,26', 4, '', '/user/finace_recharge_list', '/user/finace_recharge_list', '', 26),
(79, 1, 1, '门店管理', '79', 4, '', 'org', 'org', '', 0),
(80, 1, 1, '合作商户', '80', 11, '', 'p', 'p', '', 0);

--
-- 插入之前先把表清空（truncate） `admin_organization`
--

TRUNCATE TABLE `admin_organization`;
--
-- 转存表中的数据 `admin_organization`
--

INSERT INTO `admin_organization` (`id`, `version`, `enable`, `name`, `level_code`, `position`, `the_value`, `parent_id`) VALUES
(1, 1, 0, '总机构', '1', 0, '1', 0),
(2, 1, 0, '分机构1-1', '1,2', 0, '1', 1),
(3, 1, 0, '分机构', '1,3', 0, '', 1),
(4, 1, 0, '总机构2', '4', 0, '2', 0),
(5, 2, 0, '组织机构2-1', '4,5', 0, '1', 4);

--
-- 插入之前先把表清空（truncate） `admin_ref_org`
--

TRUNCATE TABLE `admin_ref_org`;
--
-- 转存表中的数据 `admin_ref_org`
--

INSERT INTO `admin_ref_org` (`id`, `admin_id`, `org_id`, `add_time`) VALUES
(2, 3, 0, 1443318722),
(3, 2, 1, 1443420278),
(4, 1, 0, 1444281540),
(5, 4, 0, 1444298677),
(6, 5, 0, 1446274063),
(7, 6, 0, 1446604391),
(8, 7, 0, 1454137272);

--
-- 插入之前先把表清空（truncate） `admin_role`
--

TRUNCATE TABLE `admin_role`;
--
-- 转存表中的数据 `admin_role`
--

INSERT INTO `admin_role` (`id`, `version`, `name`, `enable`) VALUES
(1, 4, '系统管理员', 1),
(2, 1, '店长', 1),
(3, 1, '总部运营', 1),
(4, 1, '运营账号', 1),
(5, 1, '客服', 1),
(6, 1, 'HR', 1),
(7, 1, '值班账号', 1);

--
-- 插入之前先把表清空（truncate） `admin_role_authority`
--

TRUNCATE TABLE `admin_role_authority`;
--
-- 转存表中的数据 `admin_role_authority`
--

INSERT INTO `admin_role_authority` (`id`, `role_id`, `authority_id`) VALUES
(951, 4, 1),
(952, 4, 2),
(953, 4, 3),
(954, 4, 53),
(955, 4, 19),
(956, 4, 36),
(957, 4, 26),
(958, 4, 27),
(959, 4, 28),
(1262, 6, 1),
(1263, 6, 2),
(1264, 6, 33),
(1265, 6, 50),
(1266, 6, 51),
(2335, 2, 1),
(2336, 2, 2),
(2337, 2, 19),
(2338, 2, 29),
(2339, 2, 31),
(2340, 2, 55),
(2341, 2, 56),
(2342, 2, 70),
(2343, 2, 71),
(2344, 2, 72),
(2345, 2, 73),
(2346, 2, 38),
(2347, 2, 41),
(2348, 2, 42),
(2349, 2, 43),
(2350, 2, 44),
(2351, 2, 46),
(2352, 2, 79),
(2353, 2, 20),
(2354, 2, 49),
(2355, 2, 65),
(2356, 2, 66),
(2357, 2, 67),
(2358, 2, 68),
(2359, 2, 75),
(2421, 5, 1),
(2422, 5, 2),
(2423, 5, 3),
(2424, 5, 53),
(2425, 5, 29),
(2426, 5, 31),
(2427, 5, 70),
(2428, 5, 71),
(2429, 5, 72),
(2430, 5, 73),
(2431, 5, 33),
(2432, 5, 50),
(2433, 5, 51),
(2434, 5, 79),
(2435, 5, 49),
(2436, 7, 1),
(2437, 7, 2),
(2438, 7, 3),
(2439, 7, 53),
(2440, 7, 29),
(2441, 7, 70),
(2442, 7, 71),
(2443, 7, 72),
(2444, 7, 73),
(2445, 7, 33),
(2446, 7, 51),
(2447, 7, 57),
(2448, 7, 60),
(2449, 7, 62),
(2450, 7, 63),
(2451, 7, 64),
(2452, 7, 79),
(2453, 7, 66),
(2454, 7, 67),
(2455, 7, 68),
(2456, 1, 1),
(2457, 1, 2),
(2458, 1, 3),
(2459, 1, 4),
(2460, 1, 7),
(2461, 1, 10),
(2462, 1, 53),
(2463, 1, 15),
(2465, 1, 17),
(2466, 1, 18),
(2467, 1, 19),
(2468, 1, 23),
(2469, 1, 24),
(2470, 1, 25),
(2471, 1, 36),
(2472, 1, 37),
(2473, 1, 76),
(2474, 1, 26),
(2475, 1, 27),
(2476, 1, 28),
(2477, 1, 29),
(2478, 1, 31),
(2479, 1, 55),
(2480, 1, 56),
(2481, 1, 69),
(2482, 1, 70),
(2483, 1, 71),
(2484, 1, 72),
(2485, 1, 73),
(2486, 1, 33),
(2487, 1, 50),
(2488, 1, 51),
(2489, 1, 38),
(2490, 1, 39),
(2491, 1, 40),
(2492, 1, 41),
(2493, 1, 42),
(2494, 1, 43),
(2495, 1, 44),
(2496, 1, 45),
(2497, 1, 46),
(2498, 1, 47),
(2499, 1, 48),
(2500, 1, 57),
(2501, 1, 58),
(2502, 1, 60),
(2503, 1, 62),
(2504, 1, 63),
(2505, 1, 64),
(2506, 1, 79),
(2507, 1, 20),
(2508, 1, 49),
(2509, 1, 65),
(2510, 1, 66),
(2511, 1, 67),
(2512, 1, 68),
(2513, 1, 74),
(2514, 1, 75),
(2515, 1, 80),
(2516, 3, 1),
(2517, 3, 2),
(2518, 3, 3),
(2519, 3, 4),
(2520, 3, 7),
(2521, 3, 10),
(2522, 3, 53),
(2523, 3, 15),
(2525, 3, 17),
(2526, 3, 18),
(2527, 3, 19),
(2528, 3, 23),
(2529, 3, 24),
(2530, 3, 25),
(2531, 3, 36),
(2532, 3, 37),
(2533, 3, 76),
(2534, 3, 26),
(2535, 3, 27),
(2536, 3, 28),
(2537, 3, 77),
(2538, 3, 78),
(2539, 3, 29),
(2540, 3, 31),
(2541, 3, 55),
(2542, 3, 56),
(2543, 3, 69),
(2544, 3, 70),
(2545, 3, 71),
(2546, 3, 72),
(2547, 3, 73),
(2548, 3, 33),
(2549, 3, 50),
(2550, 3, 51),
(2551, 3, 38),
(2552, 3, 39),
(2553, 3, 40),
(2554, 3, 41),
(2555, 3, 42),
(2556, 3, 43),
(2557, 3, 44),
(2558, 3, 45),
(2559, 3, 46),
(2560, 3, 47),
(2561, 3, 48),
(2562, 3, 57),
(2563, 3, 58),
(2564, 3, 60),
(2565, 3, 62),
(2566, 3, 63),
(2567, 3, 64),
(2568, 3, 79),
(2569, 3, 20),
(2570, 3, 49),
(2571, 3, 65),
(2572, 3, 66),
(2573, 3, 67),
(2574, 3, 68),
(2575, 3, 74),
(2576, 3, 75),
(2577, 3, 80);



CREATE TABLE IF NOT EXISTS `dict_degree_type` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `name` varchar(20) NOT NULL COMMENT '级别名称/针对助理订单类型的归类',
  `enable` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否可用 0=不可用  1=可用',
  `add_time` int(11) unsigned NOT NULL COMMENT '添加时间时间戳',
  `update_time` int(11) unsigned NOT NULL COMMENT '修改时间时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;


ALTER TABLE `dict_degree_type`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `dict_degree_type`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=6;


ALTER TABLE `dict_service_types` ADD `subhead_huodong` VARCHAR(60) NOT NULL COMMENT '（微官网）限时活动列表页,副标题2015-11-26 10:24:53' AFTER `tips`;

ALTER TABLE `dict_service_types` ADD `subhead_am` VARCHAR(60) NOT NULL COMMENT '（微官网）助理订单类型列表页，副标题2015-11-26 10:26:38' AFTER `subhead_huodong`;

ALTER TABLE `dict_service_types` ADD `service_relative` VARCHAR(500) NOT NULL COMMENT '（微官网）助理订单类型详情页，服务范围2015-11-26 10:28:40' AFTER `subhead_am`;


ALTER TABLE `dict_service_types` ADD `service_feature` VARCHAR(500) NOT NULL COMMENT '（微官网）助理订单类型详情页，服务特色 2015-11-26 10:30:51' AFTER `service_relative`;


CREATE TABLE IF NOT EXISTS `msg` (
`msg_id` int(11) unsigned NOT NULL COMMENT '主键',
  `title` varchar(64) NOT NULL COMMENT '标题',
  `summary` varchar(225) NOT NULL COMMENT '摘要',
  `content` text NOT NULL COMMENT '详细内容',
  `goto_url` varchar(225) NOT NULL COMMENT '跳转URL',
  `user_type` tinyint(1) NOT NULL COMMENT '用户类型 0 = 用户 1 = 服务人员',
  `send_time` int(11) NOT NULL COMMENT '发送时间  ，如果为0 则立即发送',
  `is_send` tinyint(1) NOT NULL COMMENT '是否发送',
  `is_enable` tinyint(1) NOT NULL COMMENT '是否可用 0 = 不可用 1 = 可用 默认为 1',
  `app_type` varchar(32) NOT NULL COMMENT '应用类型  jhj-am/jhj-u',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='消息表';


ALTER TABLE `msg`
 ADD PRIMARY KEY (`msg_id`);


ALTER TABLE `msg`
MODIFY `msg_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;



CREATE TABLE IF NOT EXISTS `msg_readed` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `user_type` tinyint(1) NOT NULL COMMENT '用户类型 0 = 用户 1 = 服务人员',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `msg_id` int(11) NOT NULL COMMENT '消息id',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='已读消息表';


ALTER TABLE `msg_readed`
 ADD PRIMARY KEY (`id`);


ALTER TABLE `msg_readed`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;


ALTER TABLE `orders` ADD `remarks_confirm` VARCHAR(32) NOT NULL COMMENT '与客户确认后信息' AFTER `remarks`;


ALTER TABLE `order_dispatchs` ADD `pick_addr_name` VARCHAR(128) NOT NULL COMMENT '取货地址名称' AFTER `dispatch_status`, ADD `pick_addr_lat` VARCHAR(32) NOT NULL COMMENT '取货地址纬度' AFTER `pick_addr_name`, ADD `pick_addr_lng` VARCHAR(32) NOT NULL COMMENT '取货地址经度' AFTER `pick_addr_lat`, ADD `pick_addr` VARCHAR(128) NOT NULL COMMENT '取货地址门牌号' AFTER `pick_addr_lng`, ADD `pick_distance` MEDIUMINT(8) UNSIGNED NOT NULL COMMENT '服务人员距离取货地址多远' AFTER `pick_addr`, ADD `user_addr_distance` MEDIUMINT(8) UNSIGNED NOT NULL COMMENT '服务人员距离用户地址多远' AFTER `pick_distance`;


CREATE TABLE IF NOT EXISTS `org_staff_auth` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `staff_id` int(11) NOT NULL COMMENT '服务人员ID',
  `service_type_id` int(11) NOT NULL COMMENT '服务大类ID 0 = 身份验证',
  `aut_status` tinyint(1) NOT NULL COMMENT '认证状态  0 = 未验证  1 = 验证通过',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳',
  `update_time` int(11) NOT NULL COMMENT '更新时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='服务人员认证表';


ALTER TABLE `org_staff_auth`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `org_staff_auth`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;


CREATE TABLE IF NOT EXISTS `org_staff_black` (
`id` int(10) unsigned NOT NULL COMMENT '主键',
  `staff_id` int(11) NOT NULL COMMENT '服务人员ID',
  `mobile` char(11) NOT NULL COMMENT '手机号',
  `black_type` tinyint(1) NOT NULL COMMENT '类型 0 = 系统判断  1 = 人工加入',
  `remarks` varchar(64) NOT NULL COMMENT '加入原因',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='黑名单表';


ALTER TABLE `org_staff_black`
 ADD PRIMARY KEY (`id`);


ALTER TABLE `org_staff_black`
MODIFY `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键';


CREATE TABLE IF NOT EXISTS `org_staff_cash` (
`order_id` int(11) unsigned NOT NULL COMMENT '主键',
  `order_no` varchar(23) NOT NULL COMMENT '申请订单号',
  `staff_id` int(11) NOT NULL COMMENT '服务人员ID',
  `mobile` char(11) NOT NULL COMMENT '服务人员手机号',
  `order_money` decimal(9,2) NOT NULL COMMENT '申请提现金额',
  `order_status` tinyint(1) NOT NULL COMMENT '状态 0 = 申请中  1 = 财务处理中  2 = 申请被驳回 3 = 已打款',
  `account` varchar(32) NOT NULL COMMENT '提现账户',
  `remarks` varchar(128) NOT NULL COMMENT '备注信息',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳',
  `update_time` int(11) NOT NULL COMMENT '更新时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='服务人员提现申请表';

ALTER TABLE `org_staff_cash`
 ADD PRIMARY KEY (`order_id`);

ALTER TABLE `org_staff_cash`
MODIFY `order_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;


CREATE TABLE IF NOT EXISTS `org_staff_detail_dept` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `staff_id` int(11) NOT NULL COMMENT '服务人员ID',
  `mobile` char(11) NOT NULL COMMENT '服务人员手机号',
  `order_type` tinyint(1) NOT NULL COMMENT '订单类型  0 = 钟点工订单 1 = 深度保洁订单 2 = 助理订单 3 = 送酒订单 4 = 还款订单 5 = 提现 6 = 补贴 7 = 利息 8= 各项核减 9 = 推荐奖励',
  `order_id` int(11) NOT NULL COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
  `order_money` decimal(9,2) NOT NULL COMMENT '订单金额',
  `order_dept` decimal(9,2) NOT NULL COMMENT '欠款金额',
  `order_status_str` varchar(32) NOT NULL COMMENT '订单状态中文描述',
  `remarks` varchar(32) NOT NULL COMMENT '备注  ： 服务费 、配送费、补贴、利息',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='服务人员欠款明细表';

CREATE TABLE IF NOT EXISTS `org_staff_detail_pay` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `staff_id` int(11) NOT NULL COMMENT '服务人员ID',
  `mobile` char(11) NOT NULL COMMENT '服务人员手机号',
  `order_type` tinyint(1) NOT NULL COMMENT '订单类型  0 = 钟点工订单 1 = 深度保洁订单 2 = 助理订单 3 = 送酒订单 4 = 还款订单 5 = 提现 6 = 补贴 7 = 利息 8= 杂费',
  `order_id` int(11) NOT NULL COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
  `order_money` decimal(9,2) NOT NULL COMMENT '订单金额/还款金额/提现金额/补贴金额/利息金额',
  `order_pay` decimal(9,2) NOT NULL COMMENT '订单收入/还款金额/提现金额/补贴金额/利息金额',
  `order_status_str` varchar(32) NOT NULL COMMENT '订单状态中文描述',
  `remarks` varchar(32) NOT NULL COMMENT '备注  ： 服务费 、配送费、补贴、利息',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='服务人员财务明细表';

CREATE TABLE IF NOT EXISTS `org_staff_finance` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `staff_id` int(11) NOT NULL COMMENT '服务人员ID',
  `mobile` char(11) NOT NULL COMMENT '手机号',
  `total_incoming` decimal(9,2) NOT NULL COMMENT '总收入',
  `total_dept` decimal(9,2) NOT NULL COMMENT '总欠款',
  `total_cash` decimal(9,2) NOT NULL COMMENT '总体现',
  `rest_money` decimal(9,2) NOT NULL COMMENT '剩余金额',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳',
  `update_time` int(11) NOT NULL COMMENT '更新时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='服务人员财务表';

CREATE TABLE IF NOT EXISTS `org_staff_invite` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `staff_id` int(11) NOT NULL COMMENT '推荐人ID',
  `invite_mobile` char(21) NOT NULL COMMENT '推荐手机号',
  `invite_staff_id` int(11) NOT NULL COMMENT '推荐手机号注册后的员工ID',
  `invite_order_count` smallint(4) NOT NULL COMMENT '推荐手机号在有效天数内完成的订单数',
  `invite_status` tinyint(1) NOT NULL COMMENT '推荐状态   0 = 未注册 1 = 已注册  2 = 已奖励',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='邀请推荐表';

CREATE TABLE IF NOT EXISTS `org_staff_online` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `staff_id` int(11) NOT NULL COMMENT '服务人员ID',
  `is_work` tinyint(1) NOT NULL COMMENT '开工标志 0 = 收工 1 = 开工',
  `lat` varchar(32) NOT NULL COMMENT '点击开工或者收工时间点所处的地理位置-纬度',
  `lng` varchar(32) NOT NULL COMMENT '点击开工或者收工时间点所处的地理位置-经度',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='服务人员在线情况表';

CREATE TABLE IF NOT EXISTS `org_staff_pay_dept` (
`order_id` int(11) unsigned NOT NULL COMMENT '主键支付欠款订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '支付欠款订单号码',
  `pay_type` tinyint(1) NOT NULL COMMENT '1 = 支付宝支付 2 = 微信支付',
  `staff_id` int(11) NOT NULL COMMENT '服务人员ID',
  `mobile` char(11) NOT NULL COMMENT '服务人员手机号',
  `order_money` decimal(9,2) NOT NULL COMMENT '支付金额',
  `order_status` tinyint(1) NOT NULL COMMENT '0 = 已取消 1 = 支付中 2 = 完成支付 9 = 已关闭',
  `pay_account` varchar(64) NOT NULL COMMENT '支付账户,支付成功后才有值',
  `trade_id` varchar(64) NOT NULL COMMENT '交易ID,支付成功后才有值',
  `trade_status` varchar(64) NOT NULL COMMENT '交易状态，支付成功才有值',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='服务人员欠款明细表';


ALTER TABLE `org_staff_detail_dept`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `org_staff_detail_pay`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `org_staff_finance`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `org_staff_invite`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `org_staff_online`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `org_staff_pay_dept`
 ADD PRIMARY KEY (`order_id`);


ALTER TABLE `org_staff_detail_dept`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;
ALTER TABLE `org_staff_detail_pay`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;
ALTER TABLE `org_staff_finance`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;
ALTER TABLE `org_staff_invite`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;
ALTER TABLE `org_staff_online`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;
ALTER TABLE `org_staff_pay_dept`
MODIFY `order_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键支付欠款订单ID',AUTO_INCREMENT=1;


CREATE TABLE IF NOT EXISTS `study_bank` (
`bank_id` int(11) unsigned NOT NULL COMMENT '主键',
  `service_type_id` int(11) unsigned NOT NULL COMMENT 'partner_service_type表的 service_type_id',
  `name` varchar(32) NOT NULL COMMENT '题库名称',
  `total_need` smallint(4) unsigned NOT NULL COMMENT '需答对题数',
  `description` varchar(128) DEFAULT NULL COMMENT '题库描述',
  `add_time` int(11) unsigned NOT NULL COMMENT '添加时间戳',
  `random_q_num` smallint(4) unsigned NOT NULL COMMENT '考试时从该题库随机取的题目的数量'
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

INSERT INTO `study_bank` (`bank_id`, `service_type_id`, `name`, `total_need`, `description`, `add_time`, `random_q_num`) VALUES
(8, 12, '钟点工题库', 10, '', 1453356310, 11),
(10, 14, '助理题库', 1, '', 1453357793, 2),
(11, 15, '快送题库', 2, '', 1453357863, 2),
(12, 15, '快送题库2', 1, '1111', 1453707139, 2),
(13, 14, '助理题库2', 1, '232', 1453707152, 2);

CREATE TABLE IF NOT EXISTS `study_learning` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `service_type_id` int(11) unsigned NOT NULL COMMENT 'partner_service_type表 service_type_id',
  `content` text COMMENT '学习内容',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

INSERT INTO `study_learning` (`id`, `service_type_id`, `content`, `add_time`) VALUES
(4, 12, '<p>\r\n	（1）我是钟点工的服务说明\r\n</p>\r\n<p>\r\n	（2）第2行\r\n</p>', 1453350578),
(5, 13, '我是深度保洁的说明', 1453350688),
(6, 14, '我是助理的学习资料说明', 1453350840),
(7, 15, '<p>\r\n	我是快送的培训学习资料说明\r\n</p>\r\n<p>\r\n	马上答题，很快就能接单赚钱啦~！\r\n</p>', 1453350863);

CREATE TABLE IF NOT EXISTS `study_question` (
`q_id` int(11) unsigned NOT NULL COMMENT '题目 id',
  `bank_id` int(11) unsigned NOT NULL COMMENT '题库ID',
  `service_type_id` int(11) unsigned NOT NULL COMMENT '服务类别ID',
  `title` varchar(128) NOT NULL COMMENT '题干',
  `description` varchar(128) NOT NULL COMMENT '题目备注',
  `is_multi` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否多选 0 = 单选 1 = 多选',
  `is_need` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否必考 0 = 否 1 = 是',
  `answer` varchar(32) NOT NULL COMMENT '答案',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

INSERT INTO `study_question` (`q_id`, `bank_id`, `service_type_id`, `title`, `description`, `is_multi`, `is_need`, `answer`, `add_time`) VALUES
(5, 10, 14, '助理题库就这一道题', '', 0, 1, 'D', 1453357845),
(6, 11, 15, '快送题1', '', 0, 1, 'B', 1453357899),
(7, 11, 15, '快送题2（就这2题）', '', 0, 1, 'D', 1453357937),
(11, 8, 12, '以下哪种问好方式是错误的', '单选', 0, 1, 'D', 1453466926),
(12, 8, 12, '以下哪种礼貌用语是错误的', '单选', 0, 1, 'D', 1453466993),
(13, 8, 12, '接电话的礼貌用语是', '单选', 0, 1, 'C', 1453467059),
(14, 8, 12, '进门的礼仪中错误的是', '单选', 0, 1, 'D', 1453469243),
(15, 8, 12, '五色抹布使用错误的是', '单选', 0, 0, 'E', 1453529507),
(16, 8, 12, '打扫卫生的顺序', '单选', 0, 1, 'D', 1453529656),
(17, 8, 12, '下面不在工作中四轻原则的是', '', 0, 1, 'A', 1453529710),
(18, 8, 12, '正确的厨房保洁顺序是', '', 0, 1, 'B', 1453529772),
(19, 8, 12, '正确的卫生间保洁顺序是', '', 0, 1, 'B', 1453529824),
(20, 8, 12, '正确的居室保洁顺序是', '', 0, 1, 'A', 1453530266),
(21, 10, 14, '错误的洗衣服的顺序是', '', 0, 1, 'D', 1453530315),
(22, 8, 12, '五色布的作用', '单选', 0, 0, 'C', 1453542017),
(23, 12, 15, '快送题库2题目一', '', 0, 0, 'A', 1453707195),
(24, 13, 14, '助理题库2题目', '', 0, 0, 'C', 1453707226);

CREATE TABLE IF NOT EXISTS `study_question_option` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `q_id` int(11) unsigned NOT NULL COMMENT '题目Id',
  `bank_id` int(11) unsigned NOT NULL COMMENT '题库id',
  `service_type_id` int(11) unsigned NOT NULL COMMENT '服务类别ID',
  `no` varchar(10) NOT NULL COMMENT '序号 枚举 ＡＢＣ',
  `title` varchar(128) NOT NULL COMMENT '选项描述'
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8;

INSERT INTO `study_question_option` (`id`, `q_id`, `bank_id`, `service_type_id`, `no`, `title`) VALUES
(21, 6, 11, 15, 'D', '快送题1D'),
(22, 6, 11, 15, 'A', '快送题1A'),
(23, 6, 11, 15, 'B', '快送题1B(正确答案)'),
(24, 6, 11, 15, 'C', '快送题1C'),
(25, 7, 11, 15, 'D', '快送题2D(正确答案)'),
(26, 7, 11, 15, 'A', '快送题2A'),
(27, 7, 11, 15, 'B', '快送题2B'),
(28, 7, 11, 15, 'C', '快送题2C'),
(37, 11, 8, 12, 'D', '热情拥抱'),
(38, 11, 8, 12, 'A', '您好'),
(39, 11, 8, 12, 'B', '早上/下午好'),
(40, 11, 8, 12, 'C', '点头微笑'),
(41, 12, 8, 12, 'D', '走你'),
(42, 12, 8, 12, 'A', '对不起'),
(43, 12, 8, 12, 'B', '再见'),
(44, 12, 8, 12, 'C', '没关系'),
(45, 13, 8, 12, 'D', '你找谁'),
(46, 13, 8, 12, 'A', '喂'),
(47, 13, 8, 12, 'B', '唉'),
(48, 13, 8, 12, 'C', '您好'),
(49, 14, 8, 12, 'D', '换工装'),
(50, 14, 8, 12, 'A', '摇铃铛'),
(51, 14, 8, 12, 'B', '自我介绍'),
(52, 14, 8, 12, 'C', '换拖鞋/鞋套'),
(53, 15, 8, 12, 'D', '紫色（厨房下）'),
(54, 15, 8, 12, 'E', '棕色（厕所上）'),
(55, 15, 8, 12, 'A', '绿色（客厅卧室的上面）'),
(56, 15, 8, 12, 'B', '蓝色（客厅卧室的下面）'),
(57, 15, 8, 12, 'C', '玫红色（厨房上）'),
(58, 5, 0, 0, 'D', '助理题D(正确答案)'),
(59, 5, 0, 0, 'A', '助理题A'),
(60, 5, 0, 0, 'B', '助理题B'),
(61, 5, 0, 0, 'C', '助理题C'),
(62, 16, 8, 12, 'D', '从左到右'),
(63, 16, 8, 12, 'A', '从里到外'),
(64, 16, 8, 12, 'B', '从上到下'),
(65, 16, 8, 12, 'C', '从易到难'),
(66, 17, 8, 12, 'D', '关门轻'),
(67, 17, 8, 12, 'E', '说话轻'),
(68, 17, 8, 12, 'A', '用力轻'),
(69, 17, 8, 12, 'B', '走路轻'),
(70, 17, 8, 12, 'C', '操作轻'),
(71, 18, 8, 12, 'D', '抽油烟机和排风扇--厨房窗户与灯具--灶具—橱柜--墙壁—餐饮用具—厨具—洗涤池—地面'),
(72, 18, 8, 12, 'A', '抽油烟机和排风扇--厨房窗户与灯具--墙壁--灶具—橱柜—餐饮用具—厨具—洗涤池—地面'),
(73, 18, 8, 12, 'B', '抽油烟机和排风扇--厨房窗户与灯具--灶具—墙壁—橱柜—餐饮用具—厨具—洗涤池—地面'),
(74, 18, 8, 12, 'C', '抽油烟机和排风扇--厨房窗户与灯具--橱柜--灶具—墙壁—餐饮用具—厨具—洗涤池—地面'),
(75, 19, 8, 12, 'D', '洗脸盆—卫生间的墙面—浴缸（或淋浴间）--座便器或者蹲便器—地面'),
(76, 19, 8, 12, 'A', '洗脸盆—浴缸（或淋浴间）--座便器或者蹲便器—卫生间的墙面—地面'),
(77, 19, 8, 12, 'B', '卫生间的墙面—洗脸盆—浴缸（或淋浴间）--座便器或者蹲便器—地面'),
(78, 19, 8, 12, 'C', '浴缸（或淋浴间）--卫生间的墙面—洗脸盆—座便器或者蹲便器—地面'),
(79, 20, 8, 12, 'D', '墙壁与天花板—家具及用品—门窗—地面'),
(80, 20, 8, 12, 'A', '墙壁与天花板—门窗—家具及用品—地面'),
(81, 20, 8, 12, 'B', '门窗—墙壁与天花板—家具及用品—地面'),
(82, 20, 8, 12, 'C', '家具及用品—墙壁与天花板—门窗—地面'),
(83, 21, 10, 14, 'D', '先洗便宜的衣物 再洗贵重的衣物'),
(84, 21, 10, 14, 'A', '先洗浅色衣物 再洗深色衣物'),
(85, 21, 10, 14, 'B', '先洗牢固度强的衣物 再洗牢固度弱'),
(86, 21, 10, 14, 'C', '先洗新衣物 再洗旧衣物'),
(90, 23, 12, 15, 'A', '选项A(√)'),
(91, 23, 12, 15, 'B', '选项B'),
(92, 23, 12, 15, 'C', '选项C'),
(93, 24, 13, 14, 'A', '选项A'),
(94, 24, 13, 14, 'B', '选项B'),
(95, 24, 13, 14, 'C', '选项C(√)'),
(96, 22, 0, 0, 'A', '黄色，谢谢谢谢'),
(97, 22, 0, 0, 'B', '白色。谢谢谢谢'),
(98, 22, 0, 0, 'C', '对对对');

CREATE TABLE IF NOT EXISTS `study_staff_pass` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `staff_id` int(11) NOT NULL COMMENT '服务人员ID',
  `service_type_id` int(11) NOT NULL COMMENT '服务大类ID',
  `bank_id` int(11) NOT NULL COMMENT '题库ID',
  `total_need` smallint(4) NOT NULL COMMENT '需要答对的题目书',
  `total_right` smallint(4) NOT NULL COMMENT '答对的题目数',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳',
  `update_time` int(11) NOT NULL COMMENT '更新时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='服务人员考试通过表';



CREATE TABLE IF NOT EXISTS `study_staff_test` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `staff_id` int(11) NOT NULL COMMENT '员工ID',
  `bank_id` int(11) NOT NULL COMMENT '题库ID',
  `q_id` int(11) NOT NULL COMMENT '题目ID',
  `test_answer` varchar(32) NOT NULL COMMENT '考试选择',
  `is_right` tinyint(1) NOT NULL COMMENT '是否正确',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='服务人员考试记录表';




ALTER TABLE `study_bank`
 ADD PRIMARY KEY (`bank_id`);

ALTER TABLE `study_learning`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `study_question`
 ADD PRIMARY KEY (`q_id`);

ALTER TABLE `study_question_option`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `study_staff_pass`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `study_staff_test`
 ADD PRIMARY KEY (`id`);


ALTER TABLE `study_bank`
MODIFY `bank_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=14;
ALTER TABLE `study_learning`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=8;
ALTER TABLE `study_question`
MODIFY `q_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '题目 id',AUTO_INCREMENT=25;
ALTER TABLE `study_question_option`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=99;
ALTER TABLE `study_staff_pass`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;
ALTER TABLE `study_staff_test`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `user_push_bind` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `user_type` tinyint(1) NOT NULL COMMENT '用户类型 0 = 用户 1 = 服务人员',
  `device_type` varchar(23) NOT NULL COMMENT '设备类型  android/ios',
  `client_id` varchar(64) NOT NULL COMMENT '设备号',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户推送设备信息表';


ALTER TABLE `user_push_bind`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `user_push_bind`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;


CREATE TABLE IF NOT EXISTS `user_trail_history` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `user_type` tinyint(1) NOT NULL COMMENT '用户类型 0 = 用户 1 = 服务人员',
  `lat` varchar(64) NOT NULL COMMENT '纬度',
  `lng` varchar(64) NOT NULL COMMENT '经度',
  `poi_name` varchar(128) NOT NULL COMMENT '地理位置名称',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT=' 用户地理位置历史表';

CREATE TABLE IF NOT EXISTS `user_trail_real` (
`id` int(11) unsigned NOT NULL COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `user_type` tinyint(1) NOT NULL COMMENT '用户类型 0 = 用户 1 = 服务人员',
  `lat` varchar(64) NOT NULL COMMENT '纬度',
  `lng` varchar(64) NOT NULL COMMENT '经度',
  `poi_name` varchar(128) NOT NULL COMMENT '地理位置名称',
  `add_time` int(11) NOT NULL COMMENT '最新一次的时间戳'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户实时地理位置信息表';


ALTER TABLE `user_trail_history`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `user_trail_real`
 ADD PRIMARY KEY (`id`);


ALTER TABLE `user_trail_history`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;
ALTER TABLE `user_trail_real`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',AUTO_INCREMENT=1;


ALTER TABLE `dict_service_types` ADD `degree_type` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '微官网）在助理订单类型列表页,分不同层级展示' , ADD `to_date` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '（微官网）该类型的 上架截止日期';

--
-- Database: `jhj`
--




--
-- 表的结构 `partner_service_type`
--

CREATE TABLE `partner_service_type` (
  `service_type_id` int(11) UNSIGNED NOT NULL COMMENT '服务类别表',
  `name` varchar(32) NOT NULL COMMENT '服务名称',
  `parent_id` int(11) UNSIGNED NOT NULL COMMENT '父级ID',
  `unit` varchar(16) NOT NULL COMMENT '单位',
  `default_num` smallint(4) NOT NULL COMMENT '默认',
  `price` decimal(9,2) UNSIGNED NOT NULL COMMENT '单价',
  `remarks` varchar(255) NOT NULL COMMENT '备注',
  `view_type` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '0=类别 1=商品',
  `no` tinyint(1) UNSIGNED NOT NULL COMMENT '列表排序',
  `service_img_url` varchar(255) NOT NULL COMMENT '微网站服务的图标图片',
  `enable` tinyint(1) UNSIGNED NOT NULL DEFAULT '1' COMMENT '服务是否可用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `partner_service_type`
--

INSERT INTO `partner_service_type` (`service_type_id`, `name`, `parent_id`, `unit`, `default_num`, `price`, `remarks`, `view_type`, `no`, `service_img_url`, `enable`) VALUES
(23, '金牌保洁', 0, '1', 1, '12.00', '', 1, 0, '', 1),
(24, '厨娘烧饭', 0, '1', 1, '10.00', '', 1, 0, '', 1),
(25, '贴心家事', 0, '1', 1, '1.00', '', 1, 0, '', 1),
(26, '深度养护', 0, '1', 1, '10.00', '', 1, 0, '', 1),
(27, '企业服务', 0, '1', 1, '10.00', '', 1, 1, 'http://img.jia-he-jia.com:8080/b1f60d3c3fe9d27f4128e86e1981d29b', 1),
(28, '金牌保洁初体验', 23, '元/次', 1, '149.00', '初次体验金牌保洁', 1, 123, 'http://img.jia-he-jia.com:8080/b1f60d3c3fe9d27f4128e86e1981d29b', 1),
(29, '厨娘烧饭初体验', 24, '元/次', 1, '99.00', '我是厨娘烧饭*初体验', 1, 0, '2.png', 1),
(30, '贴心家事', 25, '1', 1, '10.00', '', 1, 0, 'http://img.jia-he-jia.com:8080/a973ba54285e3493b26abb5e4b11ccc7', 1),
(31, '跑腿代办', 25, '1', 1, '10.00', '', 1, 0, 'http://img.jia-he-jia.com:8080/b1f60d3c3fe9d27f4128e86e1981d29b', 0),
(32, '陪伴', 25, '1', 0, '10.00', '', 1, 0, '', 0),
(33, '便民服务', 25, '1', 0, '10.00', '', 1, 0, '', 1),
(34, '床铺除螨杀菌', 26, '次', 1, '360.00', '', 1, 0, '', 1),
(35, '厨房高温消毒杀菌', 26, '1', 1, '690.00', '说明', 1, 0, '', 1),
(36, '油烟机清洗', 26, '1', 1, '10.00', '', 1, 0, '', 1),
(37, '中小企业保洁', 27, '1', 1, '10.00', '', 1, 0, '', 1),
(38, '中小企业做饭', 27, '1', 1, '10.00', '', 1, 0, '', 1),
(41, '简朴生活', 23, '元/月', 0, '307.00', '全年2周1次金牌保洁', 1, 0, '', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `partner_service_type`
--
ALTER TABLE `partner_service_type`
  ADD PRIMARY KEY (`service_type_id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `partner_service_type`
--
ALTER TABLE `partner_service_type`
  MODIFY `service_type_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '服务类别表', AUTO_INCREMENT=42;


ALTER TABLE `org_staffs` ADD `parent_org_id` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '对应地区门店ID' AFTER `org_id`;

ALTER TABLE `org_staffs` ADD `level` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '员工等级 1 = 一级  2 = 二级  3 = 三级  4 = 四级' AFTER `staff_type`;


--
-- 表的结构 `org_staff_skill`
--

CREATE TABLE `org_staff_skill` (
  `id` int(11) UNSIGNED NOT NULL COMMENT '主键，服务人员技能表',
  `staff_id` int(11) UNSIGNED NOT NULL COMMENT '服务人员id',
  `service_type_id` int(11) UNSIGNED NOT NULL COMMENT '服务类别id',
  `add_time` int(11) UNSIGNED NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `org_staff_skill`
--
ALTER TABLE `org_staff_skill`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `org_staff_skill`
--
ALTER TABLE `org_staff_skill`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，服务人员技能表';

ALTER TABLE `orgs` ADD `parent_id` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '上级门店，默认 = 0;' AFTER `update_time`;


CREATE TABLE `finance_recharge` (
  `id` int(10) UNSIGNED NOT NULL COMMENT '财务充值记录表',
  `user_id` int(11) UNSIGNED NOT NULL COMMENT '充值目标用户Id',
  `recharge_value` decimal(9,2) UNSIGNED NOT NULL DEFAULT '0.00' COMMENT '充值金额',
  `rest_money_before` decimal(9,2) UNSIGNED NOT NULL COMMENT '充值前金额',
  `rest_money_after` decimal(9,2) UNSIGNED NOT NULL COMMENT '充值后金额',
  `admin_id` int(11) UNSIGNED NOT NULL COMMENT '充值人员Id',
  `admin_name` varchar(20) DEFAULT NULL COMMENT '充值人员姓名',
  `admin_mobile` char(11) NOT NULL COMMENT '充值人员手机号',
  `approve_mobile` varchar(20) NOT NULL COMMENT '审批手机号',
  `approve_token` varchar(20) NOT NULL COMMENT '审批验证码',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `jhj_setting` (
  `id` int(11) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(32) NOT NULL COMMENT '钟点工收入比例 |  助理收入比例  | 统一客服电话 | 统一服务人员客服电话 | 推荐奖励 | 推荐有效天数 | | 推荐完成单数',
  `setting_type` varchar(32) NOT NULL COMMENT 'hour_incoming|  am_incoming | user_service_tel | staff_service_tel | invite_price | invite_days | invite_order_count',
  `setting_value` varchar(128) NOT NULL,
  `add_time` int(11) NOT NULL COMMENT '添加时间戳'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='相关配置表';

--
-- 转存表中的数据 `jhj_setting`
--

INSERT INTO `jhj_setting` (`id`, `name`, `setting_type`, `setting_value`, `add_time`) VALUES
(1, '钟点工提成', 'hour-ratio', '0.75', 1454121811),
(2, '助理提成', 'am-ratio', '0.85', 1454121811),
(3, '深度保洁提成', 'deep-ratio', '0', 1454121811),
(4, '配送订单提成', 'dis-ratio', '0', 1454121811),
(5, '统一客服电话', 'tel-u', '010-58734880', 1454121811),
(6, '服务人员客服电话', 'tell-staff', '15210316330', 1454121811),
(7, '欠款金额达到进入黑名单条件', 'total-dept-blank', '1000', 1454121811),
(8, '后台充值验证手机号', 'recharge_mobile', '18611289885', 1459247137),
(9, '初级服务人员钟点工提成', 'hour-ratio-level-1', '0.71', 1459249945),
(10, '中级服务人员钟点工提成', 'hour-ratio-level-2', '0.75', 1459249945),
(11, '金牌服务人员钟点工提成', 'hour-ratio-level-3', '0.79', 1459249945),
(12, 'VIP服务人员钟点工提成', 'hour-ratio-level-4', '0.84', 1459249945),
(13, '初级服务人员助理单提成', 'am-ratio-level-1', '0.71', 1459249945),
(14, '中级服务人员助理单提成', 'am-ratio-level-2', '0.75', 1459249945),
(15, '金牌服务人员助理单提成', 'am-ratio-level-3', '0.79', 1459249945),
(16, 'VIP服务人员助理单提成', 'am-ratio-level-4', '0.84', 1459249945),
(17, '初级服务人员配送单提成', 'dis-ratio-level-1', '0.71', 1459249945),
(18, '中级服务人员配送单提成', 'dis-ratio-level-2', '0.75', 1459249945),
(19, '金牌服务人员配送单提成', 'dis-ratio-level-3', '0.79', 1459249945),
(20, 'VIP服务人员配送单提成', 'dis-ratio-level-4', '0.84', 1459249945),
(21, '初级服务人员深度养护提成', 'deep-ratio-level-1', '0', 1459247137),
(22, '中级服务人员深度养护提成', 'deep-ratio-level-2', '0', 1459247137),
(23, '金牌服务人员深度养护提成', 'deep-ratio-level-3', '0', 1459247137),
(24, 'VIP服务人员深度养护提成', 'deep-ratio-level-4', '0', 1459247137);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `jhj_setting`
--
ALTER TABLE `jhj_setting`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `jhj_setting`
--
ALTER TABLE `jhj_setting`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键', AUTO_INCREMENT=25;

update `org_staffs` set `head_img` = 'http://www.jia-he-jia.com/u/img/default-head-img.png' 


CREATE TABLE `order_customization_year` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '全年订制服务的订单',
  `user_id` int(11) unsigned NOT NULL COMMENT '用户id',
  `service_type_id` int(11) unsigned NOT NULL COMMENT '所订制服务的id',
  `add_time` int(11) unsigned NOT NULL COMMENT '添加时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


ALTER TABLE `partner_service_type` ADD `service_property` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '服务性质 0 = 单品(如跑腿、做饭) 1= 全年定制(全年定制)' AFTER `enable`, ADD `service_times` DOUBLE(6,2) UNSIGNED NOT NULL DEFAULT '0' COMMENT '每周服务次数' AFTER `service_property`, ADD `service_content` VARCHAR(255) NOT NULL COMMENT '服务内容' AFTER `service_times`;