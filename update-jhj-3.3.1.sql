ALTER TABLE `partner_service_type` ADD `aprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动加时价格' AFTER `staff_mpprice`, ADD `staff_aprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动加时服务人员提成' AFTER `aprice`, ADD `apprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动套餐价格' AFTER `staff_aprice`, ADD `staff_apprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动套餐价格服务人员提成' AFTER `apprice`;



ALTER TABLE `dict_service_addons` ADD `aprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动价' AFTER `staff_dis_price`, ADD `staff_aprice` DECIMAL(5,2) NOT NULL DEFAULT '0' COMMENT '活动价服务人员提成' AFTER `aprice`;



INSERT INTO `cooperative_business` (`id`, `business_name`, `app_name`, `business_login_name`, `business_pass_word`, `enable`, `role_id`, `add_time`, `update_time`) VALUES
(123, '微网站社区一', '微网站社区一', 'wwzsqy', '25d55ad283aa400af464c76d713c07ad', 1, 8, 1493798749, 1493798749),
(124, '微网站社区二', '微网站社区二', 'wwzsqe', '25d55ad283aa400af464c76d713c07ad', 1, 8, 1493798796, 1493798796),
(125, '微网站社区三', '微网站社区三', 'wwzsqs', '25d55ad283aa400af464c76d713c07ad', 1, 8, 1493798817, 1493798817),
(126, '微网站社区四', '微网站社区四', 'wwzsqsi', '25d55ad283aa400af464c76d713c07ad', 1, 8, 1493798850, 1493798850);


INSERT INTO `partner_service_type` (`service_type_id`, `name`, `parent_id`, `unit`, `default_num`, `service_hour`, `price`, `staff_price`, `mprice`, `staff_mprice`, `pprice`, `staff_pprice`, `mpprice`, `staff_mpprice`, `aprice`, `staff_aprice`, `apprice`, `staff_apprice`, `remarks`, `view_type`, `no`, `service_img_url`, `enable`, `service_property`, `service_times`, `service_content`, `is_auto`, `is_multi`) VALUES
(80, '家电清洗', 26, '元/台', 10, 0, '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '', 0, 0, '', 1, 0, 0.00, '', 0, 0);


INSERT INTO `dict_service_addons` (`service_addon_id`, `service_type`, `service_hour`, `name`, `keyword`, `tips`, `price`, `staff_price`, `dis_price`, `staff_dis_price`, `aprice`, `staff_aprice`, `desc_url`, `item_unit`, `default_num`, `add_time`, `update_time`, `enable`) VALUES
(69, 80, 2, '擦玻璃', '', '', '13.00', '0.00', '11.00', '0.00', '12.00', '0.00', '', '元/平米', 10, 1493827262, 1493827452, 1),
(70, 80, 3, '金牌保洁', '', '', '189.00', '0.00', '135.00', '0.00', '149.00', '0.00', '', '元/小时', 0, 1493827262, 1493827452, 1),
(71, 80, 3, '基础保洁', '', '', '149.00', '0.00', '109.00', '0.00', '119.00', '0.00', '', '元/小时', 0, 1493827262, 1493827452, 1),
(72, 80, 1.5, '滚筒洗衣机', '', '', '140.00', '0.00', '130.00', '0.00', '84.00', '0.00', '', '元/张', 0, 1493827262, 1493827452, 1),
(73, 80, 1.5, '波轮洗衣机(拆开)', '', '', '180.00', '0.00', '160.00', '0.00', '108.00', '0.00', '', '元/台', 0, 1493827262, 1493827452, 1),
(74, 80, 1.5, '波轮洗衣机(免拆)', '', '', '120.00', '0.00', '110.00', '0.00', '72.00', '0.00', '', '元/台', 0, 1493827262, 1493827452, 1),
(75, 80, 1.5, '油机灶具套餐', '', '', '210.00', '0.00', '190.00', '0.00', '126.00', '0.00', '', '元/台', 0, 1493827262, 1493827452, 1),
(76, 80, 1.5, '侧吸油烟机', '', '', '190.00', '0.00', '170.00', '0.00', '114.00', '0.00', '', '元/台', 0, 1493827262, 1493827452, 1),
(77, 80, 1.5, '欧式油烟机', '', '', '170.00', '0.00', '150.00', '0.00', '102.00', '0.00', '', '元/台', 0, 1493827262, 1493827452, 1),
(78, 80, 1.5, '中式油烟机', '', '', '140.00', '0.00', '130.00', '0.00', '84.00', '0.00', '', '元/台', 0, 1493827262, 1493827452, 1),
(79, 80, 1.5, '中央空调', '', '', '100.00', '0.00', '90.00', '0.00', '60.00', '0.00', '', '元/樘', 0, 1493827262, 1493827452, 1),
(80, 80, 1, '柜式空调', '', '', '180.00', '0.00', '160.00', '0.00', '108.00', '0.00', '', '元', 0, 1493827262, 1493827452, 1),
(81, 80, 1, '挂式空调', '', '', '130.00', '0.00', '110.00', '0.00', '78.00', '0.00', '', '元', 0, 1493827262, 1493827452, 1),
(82, 80, 1.5, '三开门冰箱', '', '', '200.00', '0.00', '180.00', '0.00', '120.00', '0.00', '', '元/台', 0, 1493827452, 1493827452, 1),
(83, 80, 1.5, '对开门冰箱', '', '', '220.00', '0.00', '200.00', '0.00', '132.00', '0.00', '', '元/台', 0, 1493827452, 1493827452, 1),
(84, 80, 1.5, '双开门冰箱', '', '', '150.00', '0.00', '140.00', '0.00', '90.00', '0.00', '', '元/台', 0, 1493827453, 1493827453, 1),
(85, 80, 1.5, '单开门冰箱', '', '', '120.00', '0.00', '100.00', '0.00', '72.00', '0.00', '', '元/台', 0, 1493827453, 1493827453, 1);


delete from org_staff_skill where service_type_id = 80;
insert into org_staff_skill
select 0, staff_id, 80, 1493832062, org_id, parent_org_id
FROM org_staffs where staff_id in (
SELECT distinct staff_id FROM `org_staff_skill` WHERE service_type_id in (50, 51, 36, 60, 54,28, 68) and status = 1
)

