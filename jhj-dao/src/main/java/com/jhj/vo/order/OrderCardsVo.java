package com.jhj.vo.order;

import java.math.BigDecimal;
import java.util.List;

import com.jhj.po.model.order.OrderCards;

/**
 *充值卡查询vo
 * 
 * */
public class OrderCardsVo extends OrderCards{
	
	private Long addStartTime;
	
	private Long addEndTime;
	
	private List<Long> userIds;
	
	private String staffCode;
	
	private String staffName;
	
	private String userName;
	
	private BigDecimal userRestMoney;
	
	private Integer score;
	
	private Short userType;
	
	private Short addFrom;
	
	private String addStartTimeStr;
	
	private String addEndTimeStr;
	
	private int isVip;

	public Long getAddStartTime() {
		return addStartTime;
	}

	public void setAddStartTime(Long addStartTime) {
		this.addStartTime = addStartTime;
	}

	public Long getAddEndTime() {
		return addEndTime;
	}

	public void setAddEndTime(Long addEndTime) {
		this.addEndTime = addEndTime;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getUserRestMoney() {
		return userRestMoney;
	}

	public void setUserRestMoney(BigDecimal userRestMoney) {
		this.userRestMoney = userRestMoney;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Short getUserType() {
		return userType;
	}

	public void setUserType(Short userType) {
		this.userType = userType;
	}

	public Short getAddFrom() {
		return addFrom;
	}

	public void setAddFrom(Short addFrom) {
		this.addFrom = addFrom;
	}

	public String getAddStartTimeStr() {
		return addStartTimeStr;
	}

	public void setAddStartTimeStr(String addStartTimeStr) {
		this.addStartTimeStr = addStartTimeStr;
	}

	public String getAddEndTimeStr() {
		return addEndTimeStr;
	}

	public void setAddEndTimeStr(String addEndTimeStr) {
		this.addEndTimeStr = addEndTimeStr;
	}

	public int getIsVip() {
		return isVip;
	}

	public void setIsVip(int isVip) {
		this.isVip = isVip;
	}
	
}
