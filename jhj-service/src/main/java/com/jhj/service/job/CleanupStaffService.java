package com.jhj.service.job;

/**
 *
 * @author :hulj
 * @Date : 2015年8月24日下午4:15:34
 * @Description:
 *		定时任务 service
 */
public interface CleanupStaffService {

	void rebuildOrder();

	void rebuildStaffPayDep();

	void rebuildStaffCash();

}
