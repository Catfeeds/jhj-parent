ALTER TABLE order_log ADD `user_id` int(11) NOT NULL COMMENT '录入人ID';
ALTER TABLE order_log ADD `user_name` varchar(50) NOT NULL COMMENT '录入人';
ALTER table order_log ADD `user_type` smallint(4) NOT NULL COMMENT '用户类型，0：用户，1，服务人员，2：后台管理人员';
ALTER table order_log ADD `action` varchar(1000) NOT NULL COMMENT '操作';