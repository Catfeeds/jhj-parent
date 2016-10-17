ALTER TABLE org_staff_leave ADD `leave_date_end` date NOT NULL COMMENT '请假结束时间';
ALTER TABLE org_staff_leave ADD `total_days` smallint(4) unsigned NOT NULL COMMENT '请假天数';
ALTER table org_staff_leave ADD `leave_status` char(4) NOT NULL DEFAULT '1' COMMENT '请假状态：1：请假中，2：请假结束';