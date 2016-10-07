package com.jhj.vo.order;

import com.jhj.po.model.order.OrderDispatchs;

/**
 *
 * @author :hulj
 * @Date : 2015年8月14日下午8:42:05
 * @Description: 
 *		运营平台--订单管理模块--下单 列表页 VO
 */
public class OrderDispatchVo extends OrderDispatchs{

	private  String userName;	//用户名
	
	private String addrName;	//服务地址
	
	private String parentOrgName; //门店名称
	
	private String orgName;		//云店名称
	
	private String amName;		//助理名称
	
	private String amMobile;	//助理手机号

	private Short orderType;	//订单类型，决定 在派工列表点击进入 不同 的订单详情页
	
	private Short orderStatus;  //订单状态
	
	// 是否接单状态;
	private String applyStatus;

	private String applyTimeStr;
	
	
	
	public Short getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Short getOrderType() {
		return orderType;
	}

	public void setOrderType(Short orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the addrName
	 */
	public String getAddrName() {
		return addrName;
	}

	/**
	 * @param addrName the addrName to set
	 */
	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * @return the amName
	 */
	public String getAmName() {
		return amName;
	}

	/**
	 * @param amName the amName to set
	 */
	public void setAmName(String amName) {
		this.amName = amName;
	}

	/**
	 * @return the amMobile
	 */
	public String getAmMobile() {
		return amMobile;
	}

	/**
	 * @param amMobile the amMobile to set
	 */
	public void setAmMobile(String amMobile) {
		this.amMobile = amMobile;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	public String getApplyTimeStr() {
		return applyTimeStr;
	}

	public void setApplyTimeStr(String applyTimeStr) {
		this.applyTimeStr = applyTimeStr;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}
	
}
