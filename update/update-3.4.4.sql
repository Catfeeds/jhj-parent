CREATE TABLE `order_share` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL,
  `order_no` varchar(32) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `share_id` int(11) DEFAULT NULL,
  `send_coupons_id` int(11) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;



alter table cooperative_business add `broker` varchar(50) DEFAULT NULL COMMENT '合作对接人';

update cooperative_business SET broker='阴福祥' WHERE id in (107,103,105,118,115,110);

update cooperative_business SET broker='马力华' WHERE id in (122,127,128);

update cooperative_business SET broker='陈浩' WHERE id in (104,109,101);

update cooperative_business SET broker='任俊涛' WHERE id in (102,108,114);

update cooperative_business SET broker='卢登辉' WHERE id in (116);

