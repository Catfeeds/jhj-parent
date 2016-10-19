

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

ALTER TABLE `partner_service_type` ADD `service_hour` DOUBLE NOT NULL DEFAULT '0' COMMENT '默认服务小时' AFTER `default_num`;

ALTER TABLE `dict_service_addons` ADD `service_hour` DOUBLE NOT NULL DEFAULT '0' COMMENT '参考服务小时' AFTER `service_type`;

update `org_staffs` set head_img = 'http://www.jia-he-jia.com/u/img/default-head-img.png' WHERE head_img = '';


ALTER TABLE `orders` ADD `order_op_from` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '运营平台下单类型 0 = 默认自助 1 = 来电下单 ' AFTER `order_from`;	

UPDATE `admin_authority` SET `url` = '/order/order-exp-list' WHERE `admin_authority`.`id` = 70;
UPDATE `admin_authority` SET `match_url` = '/order' WHERE `admin_authority`.`id` = 70;


update `orders` set order_type = 1 WHERE service_type in (SELECT service_type_id FROM `partner_service_type` WHERE parent_id = 26);

ALTER TABLE `order_prices` CHANGE `pay_type` `pay_type` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '付款方式 0 = 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付 4 = 上门刷卡（保留，站位） 6 = 现金支付 7 = 第三方支付';

delete from `admin_role_authority` where `authority_id` in (43,46, 55, 56, 57, 58, 71);

ALTER TABLE `orders` CHANGE `service_hour` `service_hour` DOUBLE  NOT NULL DEFAULT '0' COMMENT '服务时长';

ALTER TABLE `order_dispatchs` CHANGE `service_hours` `service_hours` DOUBLE NOT NULL DEFAULT '0' COMMENT '服务小时';

ALTER TABLE `org_staff_detail_pay` CHANGE `remarks` `remarks` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '备注 ： 服务费 、配送费、补贴、利息';

ALTER TABLE `org_staff_detail_dept` CHANGE `remarks` `remarks` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '备注 ： 服务费 、配送费、补贴、利息';

ALTER TABLE org_staff_leave ADD `leave_date_end` date NOT NULL COMMENT '请假结束时间';
ALTER TABLE org_staff_leave ADD `total_days` smallint(4) unsigned NOT NULL COMMENT '请假天数';
ALTER table org_staff_leave ADD `leave_status` char(4) NOT NULL DEFAULT '1' COMMENT '请假状态：1：请假中，2：请假结束';

ALTER TABLE `user_detail_pay` CHANGE `order_type` `order_type` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '0 = 订单支付 1 = 购买充值卡 2 = 手机话费类充值 3 = 订单补差价';

update `user_detail_pay` set order_type = 0 WHERE order_type = 1;

update `user_detail_pay` set order_type = 0 WHERE order_type = 2;

update `user_detail_pay` set order_type = 1 WHERE order_type = 4;


CREATE TABLE `order_price_ext` (
  `id` int(11) UNSIGNED NOT NULL COMMENT '主键',
  `user_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '用户ID',
  `mobile` char(11) NOT NULL COMMENT '用户手机号',
  `order_id` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '订单id',
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
  `order_no_ext` varchar(64) NOT NULL COMMENT '补差价订单编号',
  `pay_type` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '付款方式 0 = 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付 4 = 上门刷卡（保留，站位） 6 = 现金支付 7 = 第三方支付',
  `order_pay` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '订单实际支付金额',
  `order_status` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '0 = 已取消 1 = 支付中 2 = 完成支付 9 = 已关闭',
  `remarks` varchar(255) NOT NULL COMMENT '备注',
  `add_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '添加时间戳',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '更新时间戳'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单金额补差价表';

--
-- Indexes for dumped tables
--

--
-- Indexes for table `order_price_ext`
--
ALTER TABLE `order_price_ext`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `mobile` (`mobile`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `order_price_ext`
--
ALTER TABLE `order_price_ext`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键';
	


TRUNCATE TABLE `cooperative_business`;


INSERT INTO `cooperative_business` (`id`, `business_name`, `app_name`, `business_login_name`, `business_pass_word`, `enable`, `role_id`, `add_time`, `update_time`) VALUES
(101, '淘宝', 'taobao', 'taobao', '0c0038c82832082318e245a9b9704cc3', 1, 8, 1475303355, 1475303355),
(102, '京东', 'jd', 'jingdong', 'f8dbebe8a7824b0bc70d6a0a419d3ccd', 1, 8, 1475303426, 1475303426),
(103, '到位', '', 'daowei123', 'c6dfcbd7cb55655a6c685b1a4560bc70', 1, 8, 1475303463, 1475303463),
(104, '糯米', '', 'nuomi', '8c996203ac05e0e3d7cd2e1837b0a654', 1, 8, 1475303513, 1475303513),
(105, '国安社区', '', 'guoanshequ', '99fecfc30d9d843367d8612f741c9000', 1, 8, 1475303546, 1475303546),
(106, '大众点评', '', 'dazhongdianping', '0a3b9e92be6f54716dc83c6ed977525a', 1, 8, 1475303575, 1475303575),
(107, '美团', '', 'meituan', '4c36e840330d8420d16720cebfefe478', 1, 8, 1475303595, 1475303595),
(108, '葡萄生活', '', 'putaoshenghuo', '089f531e9f595ea0a3c1172f3a7bb88e', 1, 8, 1475303620, 1475303620),
(109, '居然之家', '', 'juranzhijia', 'eff2d25efe382fc5c4bbeddd913994d2', 1, 8, 1475303646, 1475303646),
(110, '百度', '', 'baidu', '818f104aa052408669bc76c4df5bc618', 1, 8, 1475303661, 1475303661),
(111, '朝阳门店', 'chaoyang', 'chaoyang', 'changyang', 1, 8, 1475303355, 1475303355),
(112, '海淀门店', 'haidian', 'haidian', 'haidian', 1, 8, 1475303355, 1475303355),
(113, '包月定制', 'baoyue', 'baoyue', 'baoyue', 1, 8, 1475303355, 1475303355);



TRUNCATE TABLE `partner_service_type`;
--
-- 转存表中的数据 `partner_service_type`
--

INSERT INTO `partner_service_type` (`service_type_id`, `name`, `parent_id`, `unit`, `default_num`, `service_hour`, `price`, `remarks`, `view_type`, `no`, `service_img_url`, `enable`, `service_property`, `service_times`, `service_content`, `is_auto`, `is_multi`) VALUES
(23, '金牌保洁', 0, '1', 1, 0, '12.00', '', 1, 0, '', 1, 0, 0.00, '', 1, 0),
(24, '厨娘烧饭', 0, '1', 1, 0, '10.00', '', 1, 0, '', 1, 0, 0.00, '', 1, 0),
(25, '贴心家事', 0, '1', 1, 0, '1.00', '', 1, 0, '', 0, 0, 0.00, '', 1, 0),
(26, '深度养护', 0, '1', 1, 0, '10.00', '', 1, 0, '', 1, 0, 0.00, '', 1, 0),
(27, '企业服务', 0, '1', 1, 0, '10.00', '', 1, 1, 'http://img.jia-he-jia.com:8080/b1f60d3c3fe9d27f4128e86e1981d29b', 1, 0, 0.00, '', 1, 0),
(28, '金牌保洁', 23, '元/3小时', 0, 3, '149.00', '一次超值的特价享受', 1, 123, 'http://img.jia-he-jia.com:8080/2f338d456349afa3f1c82636ffa8a5bc', 1, 0, 0.00, '', 1, 0),
(29, '厨娘烧饭', 24, '元/2小时', 0, 2, '99.00', '一次超值的解馋体验', 1, 0, 'http://img.jia-he-jia.com:8080/e6e4fa61e75510f76f39f574ad25cf8a', 1, 0, 0.00, '南方菜/北方菜（可代买食材）', 1, 0),
(30, '安心托管', 25, '元/次起', 1, 0, '25.00', '提供照顾老人、洗衣熨烫、接送孩子、婴幼儿看护、宠物代管代养、筹备宴请、房屋托管服务。', 1, 0, 'http://img.jia-he-jia.com:8080/4108c444b1a6b59119da9f6a4e37cda8', 0, 0, 0.00, '照顾老人，100/次；\r\n洗衣熨烫，49/次时； \r\n接送孩子，75/次；\r\n婴幼儿看护，100/次；\r\n宠物代管代养，75/次；\r\n筹备宴请，1040/次；\r\n房屋托管，25/次。', 1, 0),
(31, '跑腿代办', 25, '元/次起', 1, 0, '20.00', '提供代采购、跑腿、车辆过户年检车险、代缴交通违章、接送亲友、企业代缴社保公积金服务。', 1, 0, 'http://img.jia-he-jia.com:8080/bbfabfc7cd295ad77b31be296e39cef7', 0, 0, 0.00, '代采购，20/次；\r\n跑腿（ 代送礼），100/次；\r\n车辆过户年检车险，500/次；\r\n代缴交通违章，200/次；\r\n接送亲友，150/次；\r\n企业代缴社保公积金，260/次。', 1, 0),
(32, '陪伴', 25, '元/次', 0, 0, '150.00', '提供陪运动、陪同就医、陪同购物、陪聊服务。', 1, 0, 'http://img.jia-he-jia.com:8080/ec2d3e10b6f0129429e8c2fc14506401', 0, 0, 0.00, '陪运动，150/次；\r\n陪同就医，150/次；\r\n陪同购物，150/次；\r\n陪聊，150/次。', 1, 0),
(33, '便民服务', 25, '元/次', 0, 0, '0.00', '提供每日提醒、生活信息查询、智能手机使用、家庭简单维修、浇花、出租车代订、网上医院挂号、代缴水、电、煤气等费用、市内各大餐厅线上预定、代订报刊杂志、代收邮件包裹、代订奶、水、讲座沙龙、车船、机票酒店。', 1, 0, 'http://img.jia-he-jia.com:8080/c5134155c59406bc95f96921aa8737df', 0, 0, 0.00, '免费服务（挂号价格另议，预订紧俏票收费）', 1, 0),
(34, '床品除螨杀菌', 26, '元/套', 0, 0, '360.00', '【服务范围】 \r\n1枕头、被子、褥子、床垫；\r\n2沙发前后靠背、坐垫、两边扶手；\r\n3窗帘正反面除螨杀菌吸尘\r\n【服务流程】\r\n1除尘\r\n2清洁\r\n3除螨杀菌', 1, 0, 'http://img.jia-he-jia.com:8080/8316de14c116886e09b61ee89b4ffd2c', 1, 0, 0.00, '360元/次，含沙发窗帘，两居室460元，三居室560元。', 1, 0),
(35, '厨、卫高温消毒杀菌', 26, '元/次', 0, 3, '0.00', '【服务范围】 \r\n1厨房全面清洁，除污、除油、蒸汽无缝杀菌、橱柜及瓷砖面抑菌处理。\r\n2卫生间洁具、瓷砖除污垢、水纹、蒸汽无缝杀菌，卫浴设施全面清洁、消毒、抑菌处理。', 1, 0, 'http://img.jia-he-jia.com:8080/b519ae6995c8a93c8e534688000f0b2f', 1, 0, 0.00, '690元/次，一厨两卫930元；一厨三卫1170元。', 0, 1),
(36, '油烟机清洗', 26, '元/次', 0, 0, '120.00', '【服务范围】 \r\n油烟机身内外表面、油网、风轮、油盒、涡轮壳、高温蒸汽消毒杀菌\r\n【服务流程】\r\n1试机\r\n2防护\r\n3拆掉\r\n4清洁\r\n5高温消毒\r\n6组装\r\n7检查', 1, 0, 'http://img.jia-he-jia.com:8080/252193c1e05883779d4f401b07b93734', 1, 0, 0.00, '120元/次，欧式150元/台。', 1, 0),
(37, '中小企业保洁', 27, '元/次', 1, 0, '149.00', '金牌保洁是叮当到家首推的专业且专属的家庭保洁服务，以让美好生活不再有家务之忧为初衷，为日常家庭提供除尘、除污、清洗、收纳等室内保洁服务。', 1, 0, 'http://img.jia-he-jia.com:8080/4322f807d8156f2e672e1f55900986ce', 1, 0, 0.00, '149元/次，服务时间三小时，超出部分50元/小时。', 1, 0),
(38, '中小企业做饭', 27, '元/次', 1, 0, '99.00', '厨娘烧饭是叮当到家提供的一项保障日常饮食生活规律化的服务，主要解决因工作或时间等原因，导致日常饮食不规律、口味难满足等问题。', 1, 0, 'http://img.jia-he-jia.com:8080/25f1c008449d594225d04371c95a71c5', 1, 0, 0.00, '99元/次，服务时间两小时，超出部分50元/小时。', 1, 0),
(41, '简朴生活', 23, '元/月', 0, 0, '3874.00', '全年定制', 1, 0, 'http://img.jia-he-jia.com:8080/804a818e169ecc81555859baba103074', 0, 1, 0.50, '客厅、卧室、厨房、餐厅、卫生间、阳台六大区域\r\n除尘、除污、去油、拖洗、物品整理+擦拭+摆放等78项服务', 1, 0),
(42, '舒适生活', 23, '次', 0, 0, '7748.00', '全年定制', 0, 0, 'http://img.jia-he-jia.com:8080/150e5d2e56839f652aa480561c8557a1', 0, 1, 1.00, '客厅、卧室、厨房、餐厅、卫生间、阳台六大区域\r\n除尘、除污、去油、拖洗、物品整理+擦拭+摆放等78项服务', 1, 0),
(43, '怡然生活', 23, '次', 0, 0, '15496.00', '全年定制', 0, 0, 'http://img.jia-he-jia.com:8080/c891853e76f084be96b7946d6f267073', 0, 1, 2.00, '客厅、卧室、厨房、餐厅、卫生间、阳台六大区域\r\n除尘、除污、去油、拖洗、物品整理+擦拭+摆放等78项服务', 1, 0),
(44, '祥和生活', 23, '次', 0, 0, '23244.00', '全年定制', 0, 0, 'http://img.jia-he-jia.com:8080/7dfa2026848328ee7f796e0a7666c9ee', 0, 1, 3.00, '客厅、卧室、厨房、餐厅、卫生间、阳台六大区域\r\n除尘、除污、去油、拖洗、物品整理+擦拭+摆放等78项服务', 1, 0),
(45, '品质生活', 23, '次', 0, 0, '38740.00', '全年定制', 0, 0, 'http://img.jia-he-jia.com:8080/73f98a169e40028ddfbb32e50c9a213e', 0, 1, 5.00, '客厅、卧室、厨房、餐厅、卫生间、阳台六大区域\r\n除尘、除污、去油、拖洗、物品整理+擦拭+摆放等78项服务', 1, 0),
(46, '巧厨娘', 24, '次', 0, 0, '25740.00', '全年定制', 0, 0, 'http://img.jia-he-jia.com:8080/62b4f4c7dc7c62c8aedb26848d340cf8', 0, 1, 5.00, '南方菜/北方菜（可代买食材）', 1, 0),
(47, '俏厨娘', 24, '次', 0, 0, '30888.00', '全年定制', 0, 0, 'http://img.jia-he-jia.com:8080/7fe45ebcc1e4efd290c2d16e477cf2a1', 0, 1, 6.00, '南方菜/北方菜（可代买食材）', 1, 0),
(48, '贤厨娘', 24, '次', 0, 0, '51488.00', '全年定制', 0, 0, 'http://img.jia-he-jia.com:8080/132175a0d964b0bddec56f6941cf88ed', 0, 1, 10.00, '南方菜/北方菜（可代买食材）', 1, 0),
(49, '孝厨娘', 24, '次', 0, 0, '61776.00', '全年定制', 0, 0, 'http://img.jia-he-jia.com:8080/8c75a4588ea268327029febdbdb2f058', 0, 1, 12.00, '南方菜/北方菜（可代买食材）', 1, 0),
(50, '冰箱清洗', 26, '元/次', 0, 0, '200.00', '【服务范围】 \r\n1冰箱内部霜除冰、零部件清洗、冰箱内外擦拭清洗、高温消毒杀菌\r\n2洗衣机内筒、外壁、排水口、密封圈、接水管、排水管、显示屏干净光亮。', 0, 0, 'http://img.jia-he-jia.com:8080/b7e1809aca64b68b7e2233afb57c6bd0', 1, 0, 0.00, '200元/次，冰箱、洗衣机各100元/台。', 1, 0),
(51, '空调清洗', 26, '元/次', 0, 0, '100.00', '【服务范围】 \r\n1杀菌、除味、清洗过滤网\r\n2检测：包含缺氟+电源线+包扎带等\r\n【服务流程】\r\n1试机\r\n2拆卸\r\n3清洁\r\n4高温消毒\r\n5组装\r\n6检查', 0, 0, 'http://img.jia-he-jia.com:8080/80039b16969c5f578169a8de7d822169', 1, 0, 0.00, '100元/次，柜式150元/台，挂式100元/台。', 1, 0),
(52, '地板保养打蜡', 26, '元/次', 20, 0, '750.00', '【服务范围】 \r\n家具保养；实木地板；复合地板\r\n【服务流程】\r\n1半湿抹布擦拭灰尘\r\n2软布温水擦净\r\n3干布擦干\r\n4涂上油蜡', 0, 0, 'http://img.jia-he-jia.com:8080/e090cee2fa450cae031f52a409e2b045', 1, 0, 0.00, '750元/次，50㎡以内，超出部分10元/㎡。', 1, 0),
(53, '房屋大扫除', 26, '元/次', 60, 4, '660.00', '玻璃清洗，厨房，卫生间，客厅，卧室，家具，墙面掸尘，家庭死角清理。', 0, 0, 'http://img.jia-he-jia.com:8080/4ec2e407b39e1336101425bcb36225b9', 1, 0, 0.00, '80-120㎡，660元；121-150㎡，840元；151-200㎡，1080元。', 0, 1),
(54, '擦玻璃', 26, '元/次', 10, 0, '140.00', '【服务范围】 \r\n玻璃内外；窗户边框；槽道尘土清理；操作区域干净整洁\r\n【服务流程】\r\n1清除边框灰尘及污渍\r\n2喷玻璃清洁剂\r\n3双面玻璃清洁器擦净\r\n4玻璃刮擦干水痕\r\n5检查清洗效果', 0, 0, 'http://img.jia-he-jia.com:8080/aa2a2601fd225bc6ad581079352ea599', 1, 0, 0.00, '140元/次，10平以内，超出部分14元/平。', 0, 1),
(55, '整理衣橱(含室内整理)', 26, '元/次', 0, 0, '260.00', '【服务范围】 \r\n物品的分类、整理\r\n【服务流程】\r\n1分类整理\r\n2合理规划衣橱空间', 0, 0, 'http://img.jia-he-jia.com:8080/b7fd6b59049f58c749d08807bda2138a', 0, 0, 0.00, '260元/次，8平方以内，超过部分25元/平。', 1, 0),
(56, '新居开荒', 26, '元/平米', 60, 0, '10.00', '擦玻璃、整体除尘养护、地面彻底清洁、装修痕迹清理、厕所除污消毒。', 0, 0, 'http://img.jia-he-jia.com:8080/1464c63e6f1009ec72a5cfaf42a6763f', 1, 0, 0.00, '10元/平米', 0, 1),
(57, '母婴到家', 0, '元', 0, 0, '1999.00', '', 0, 0, '', 1, 0, 0.00, '', 1, 0),
(59, '孕', 27, '元', 0, 0, '1.00', '', 0, 0, '', 1, 0, 0.00, '', 1, 0),
(60, '洗衣机清洗', 26, '元/台', 0, 0, '0.00', '', 0, 0, '', 1, 0, 0.00, '', 1, 0),
(61, '家务包月', 26, '元/1月4次', 0, 3, '596.00', '', 0, 0, '', 1, 0, 0.00, '', 0, 1),
(62, '月子房', 57, '元/次', 0, 3, '399.00', '', 0, 0, '', 1, 0, 0.00, '', 1, 0),
(63, '孕家洁', 57, '元/次', 0, 6, '999.00', '', 0, 0, '', 1, 0, 0.00, '', 0, 1),
(64, '洁宝宝', 57, '元/次', 0, 2, '399.00', '', 0, 0, '', 1, 0, 0.00, '', 1, 0),
(65, '安居宝', 57, '元/次', 0, 3, '299.00', '', 0, 0, '', 1, 0, 0.00, '', 1, 0);


TRUNCATE TABLE `dict_service_addons`;
--
-- 转存表中的数据 `dict_service_addons`
--

INSERT INTO `dict_service_addons` (`service_addon_id`, `service_type`, `service_hour`, `name`, `keyword`, `tips`, `price`, `dis_price`, `desc_url`, `item_unit`, `default_num`, `add_time`, `update_time`, `enable`) VALUES
(1, 1, 0, '洗衣', 'wash-clothes', '洗衣', '0.00', '0.00', '', '小时', 2, 1416394476, 1416394476, 1),
(2, 1, 0, '做饭', 'cook', '做饭', '0.00', '0.00', '', '小时', 2, 1416394476, 1416394476, 1),
(3, 2, 0, '卧室', 'exp-clean-room', '深度保洁-卧室', '330.00', '0.00', '', '间', 1, 1416394476, 1416394476, 1),
(4, 2, 0, '客厅', 'exp-clean-saloon', '深度保洁-客厅', '380.00', '0.00', '', '间', 1, 1416394476, 1416394476, 1),
(5, 2, 0, '厨房', 'exp-clean-cookroom', '深度保洁-厨房', '400.00', '0.00', '', '间', 1, 1416394476, 1416394476, 1),
(6, 2, 0, '卫生间', 'exp-clean-toilet', '深度保洁-卫生间', '200.00', '0.00', '', '间', 1, 1416394476, 1416394476, 1),
(7, 2, 0, '开荒', 'exp-clean-new', '深度保洁-开荒', '6.00', '0.00', '', '平方米', 5, 1416394476, 1416394476, 1),
(8, 1, 0, '清洁用品', 'clean-tools', '清洁用品', '5.00', '5.00', '', '小时', 2, 416394476, 416394476, 1),
(9, 2, 0, '擦玻璃', 'clean-glass', '深度保洁-擦玻璃', '12.00', '0.00', '', '平方米', 5, 1416394476, 1416394476, 1),
(10, 34, 1, '单人床', '', '', '128.00', '100.00', '', '元/张', 0, 1474885916, 1475400860, 1),
(11, 34, 1, '双人床', '', '', '168.00', '130.00', '', '元/张', 0, 1474885916, 1475400860, 1),
(12, 34, 1, '布艺沙发', '', '', '90.00', '60.00', '', '元/座', 0, 1474885916, 1475400860, 1),
(13, 35, 3, '厨房深度清洁杀菌', '', '', '560.00', '450.00', '', '元/间', 0, 1474886413, 1475910815, 1),
(14, 35, 1, '卫生间深度清洁杀菌', '', '', '350.00', '240.00', '', '元/间', 0, 1474886413, 1475910815, 1),
(15, 36, 1.5, '中式油烟机', '', '', '150.00', '120.00', '', '元/台', 0, 1474886640, 1475400917, 1),
(16, 36, 1.5, '欧式油烟机', '', '', '180.00', '150.00', '', '元/台', 0, 1474886640, 1475400917, 1),
(17, 36, 1.5, '侧吸油烟机', '', '', '200.00', '160.00', '', '元/台', 0, 1474886640, 1475400917, 1),
(18, 36, 1.5, '油机灶具套餐', '', '', '260.00', '200.00', '', '元/台', 0, 1474886640, 1475400917, 1),
(19, 50, 1.5, '单开门冰箱', '', '', '120.00', '100.00', '', '元/台', 0, 1474886892, 1475400947, 1),
(20, 50, 1.5, '双开门冰箱', '', '', '150.00', '120.00', '', '元/台', 0, 1474886892, 1475400947, 1),
(21, 60, 1.5, '波轮洗衣机', '', '', '120.00', '100.00', '', '元/台', 0, 1474887006, 1475401140, 1),
(22, 60, 1.5, '滚筒洗衣机', '', '', '150.00', '120.00', '', '元/张', 0, 1474887006, 1475401140, 1),
(23, 51, 1, '挂式空调', '', '', '128.00', '100.00', '', '元', 0, 1474887151, 1475400976, 1),
(24, 51, 1, '柜式空调', '', '', '188.00', '150.00', '', '元', 0, 1474887151, 1475400976, 1),
(25, 52, 3, '地板液体打蜡', '', '', '12.00', '10.00', '', '元/平米', 20, 1474887481, 1475401008, 1),
(26, 52, 3, '地板固体打蜡', '', '', '20.00', '15.00', '', '元/平米', 20, 1474887481, 1475401008, 1),
(27, 53, 4, '房屋大扫除', '', '', '15.00', '10.00', '', '元/平米', 60, 1474887636, 1475401049, 1),
(28, 54, 2, '擦玻璃', '', '', '14.00', '12.00', '', '元/平米', 10, 1474887714, 1475401079, 1),
(29, 56, 3.5, '新居开荒', '', '', '10.00', '7.00', '', '元/平米', 60, 1474887830, 1475401112, 1),
(30, 34, 3, '金牌保洁', '', '', '199.00', '149.00', '', '元/小时', 0, 1475400860, 1475400860, 1),
(31, 35, 3, '金牌保洁', '', '', '199.00', '149.00', '', '元/小时', 0, 1475400887, 1475910815, 1),
(32, 36, 3, '金牌保洁', '', '', '199.00', '149.00', '', '元/小时', 0, 1475400917, 1475400917, 1),
(33, 50, 3, '金牌保洁', '', '', '199.00', '149.00', '', '元/小时', 0, 1475400947, 1475400947, 1),
(34, 51, 3, '金牌保洁', '', '', '199.00', '149.00', '', '元/小时', 0, 1475400976, 1475400976, 1),
(35, 52, 3, '金牌保洁', '', '', '199.00', '149.00', '', '元/小时', 0, 1475401008, 1475401008, 1),
(36, 53, 3, '金牌保洁', '', '', '199.00', '149.00', '', '元/小时', 0, 1475401049, 1475401049, 1),
(37, 54, 3, '金牌保洁', '', '', '199.00', '149.00', '', '元/小时', 0, 1475401079, 1475401079, 1),
(38, 56, 3, '金牌保洁', '', '', '199.00', '149.00', '', '元/小时', 0, 1475401112, 1475401112, 1),
(39, 60, 3, '金牌保洁', '', '', '199.00', '149.00', '', '元/小时', 0, 1475401140, 1475401140, 1),
(40, 61, 3, '家务包月', '', '', '596.00', '499.00', '', '元/1月4次', 0, 1476513836, 1476513836, 1),
(41, 62, 3, '月子房', '', '', '399.00', '299.00', '', '元/次', 0, 1476514293, 1476514293, 1),
(42, 63, 6, '孕家洁', '', '', '999.00', '699.00', '', '元/次', 0, 1476514359, 1476514359, 1),
(43, 64, 2, '洁宝宝', '', '', '399.00', '299.00', '', '元/次', 0, 1476514405, 1476514405, 1),
(44, 65, 3, '设计方案', '', '', '299.00', '220.00', '', '元/次', 0, 1476515423, 1476515423, 1),
(45, 65, 3, '施工费', '', '', '199.00', '150.00', '', '元/次', 0, 1476515423, 1476515423, 1);

