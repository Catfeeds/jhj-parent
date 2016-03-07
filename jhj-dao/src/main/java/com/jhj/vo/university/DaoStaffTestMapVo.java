package com.jhj.vo.university;

/**
 *
 * @author :hulj
 * @Date : 2016年1月25日下午3:48:56
 * @Description: 
 *		
 *		用户最新最近的考试时间,返回的 结果集Vo
 *		
 */
public class DaoStaffTestMapVo {
	
	private Long latestAddTime; //最新的考试时间
	private Long latestBankId;	//题库id
	private Long latestStaffId;	//服务 人员id


	public Long getLatestAddTime() {
		return latestAddTime;
	}
	public void setLatestAddTime(Long latestAddTime) {
		this.latestAddTime = latestAddTime;
	}
	public Long getLatestBankId() {
		return latestBankId;
	}
	public void setLatestBankId(Long latestBankId) {
		this.latestBankId = latestBankId;
	}
	public Long getLatestStaffId() {
		return latestStaffId;
	}
	public void setLatestStaffId(Long latestStaffId) {
		this.latestStaffId = latestStaffId;
	}
	
}



