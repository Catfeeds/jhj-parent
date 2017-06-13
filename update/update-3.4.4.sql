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

