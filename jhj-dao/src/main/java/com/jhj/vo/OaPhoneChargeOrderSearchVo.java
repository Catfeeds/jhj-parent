package com.jhj.vo;

import java.util.List;

/**
 *
 * @author :hulj
 * @Date : 2015年10月24日下午4:27:55
 * @Description: 
 *	
 *		话费充值 订单 列表--查询条件
 */
public class OaPhoneChargeOrderSearchVo {
	
	//用户手机号
	private String userMobile;
	
	//充值手机号
	private String chargeMobile;
	
	//充值金额
	private String money;
	
	//页面搜索字段
	private Short searchStatus;
	
	//充值订单状态, 数据库搜索条件,对应 order_status
	private List<Short> staList;
	
	//备注字段, 数据库搜索条件，不作为用户输入参数
	private List<String> remarkList;
	
//	/*
//	 * 	2015-11-2 16:24:24
//	 * 	为实现精准查询, 处理  话费充值订单的 微信支付问题
//	 * 	
//	 * 	存储key-value :  status(订单状态) : remarks (备注)
//	 * 
//	 * 		微信支付失败				13:""  ||  16:"" 
//	 * 		微信支付成功、未充值			13:"wxSuccess" || 16:"wxSuccess"
//	 * 		微信支付成功、充值成功		14:"apixSuccess"
//	 * 		微信支付成功、充值失败		15:"apixFail"
//	 */
//	private Map<List<Short>, List<String>> statusMap;
	
	/*
	 * 统计图表参数
	 */
	private Long startTime;	//选择的天 的 开始和结束 时间 时间戳
	private Long endTime;
	
	private String moneyStr;
	
	//页面传参，及回显 所用的 参数
	private String startTimeStr;
	private String endTimeStr;
	
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	/**
	 * @return the endTimeStr
	 */
	public String getEndTimeStr() {
		return endTimeStr;
	}
	/**
	 * @param endTimeStr the endTimeStr to set
	 */
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public String getChargeMobile() {
		return chargeMobile;
	}
	public void setChargeMobile(String chargeMobile) {
		this.chargeMobile = chargeMobile;
	}
	
	
	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	/**
	 * @return the money
	 */
	public String getMoney() {
		return money;
	}
	/**
	 * @param money the money to set
	 */
	public void setMoney(String money) {
		this.money = money;
	}
	/**
	 * @return the moneyStr
	 */
	public String getMoneyStr() {
		return moneyStr;
	}
	/**
	 * @param moneyStr the moneyStr to set
	 */
	public void setMoneyStr(String moneyStr) {
		this.moneyStr = moneyStr;
	}
//	/**
//	 * @return the statusMap
//	 */
//	public Map<List<Short>, List<String>> getStatusMap() {
//		return statusMap;
//	}
//	/**
//	 * @param statusMap the statusMap to set
//	 */
//	public void setStatusMap(Map<List<Short>, List<String>> statusMap) {
//		this.statusMap = statusMap;
//	}
	
	/**
	 * @return the searchStatus
	 */
	public Short getSearchStatus() {
		return searchStatus;
	}
	/**
	 * @param searchStatus the searchStatus to set
	 */
	public void setSearchStatus(Short searchStatus) {
		this.searchStatus = searchStatus;
	}
	/**
	 * @return the staList
	 */
	public List<Short> getStaList() {
		return staList;
	}
	/**
	 * @param staList the staList to set
	 */
	public void setStaList(List<Short> staList) {
		this.staList = staList;
	}
	/**
	 * @return the remarkList
	 */
	public List<String> getRemarkList() {
		return remarkList;
	}
	/**
	 * @param remarkList the remarkList to set
	 */
	public void setRemarkList(List<String> remarkList) {
		this.remarkList = remarkList;
	}
	
	
	
	
}
