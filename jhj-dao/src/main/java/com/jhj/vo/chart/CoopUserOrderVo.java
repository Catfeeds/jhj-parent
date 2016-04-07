package com.jhj.vo.chart;

/**
 *
 * @author :hulj
 * @Date : 2016年4月7日下午4:48:24
 * @Description: 
 *		
 *		合作商户 --旗下用户，订单数
 */
public class CoopUserOrderVo {
	
	private Long userId;
	
	private String userMobile;
	
	private Long userOrderNum;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public Long getUserOrderNum() {
		return userOrderNum;
	}

	public void setUserOrderNum(Long userOrderNum) {
		this.userOrderNum = userOrderNum;
	}
	
}
