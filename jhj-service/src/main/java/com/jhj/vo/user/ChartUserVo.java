package com.jhj.vo.user;

import com.jhj.po.model.user.Users;


/**
 * @description：
 * @author： kerryg
 * @date:2015年8月28日 
 */
public class ChartUserVo extends Users {
	
	private String statTime;
	
	private int growthRate;
	
	private int fromAppTotal;
	
	private int fromWxTotal;
	
	private int newUsersTotal;
	
	private int transformRate;

	public String getStatTime() {
		return statTime;
	}

	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}

	public int getGrowthRate() {
		return growthRate;
	}

	public void setGrowthRate(int growthRate) {
		this.growthRate = growthRate;
	}

	public int getFromAppTotal() {
		return fromAppTotal;
	}

	public void setFromAppTotal(int fromAppTotal) {
		this.fromAppTotal = fromAppTotal;
	}

	public int getFromWxTotal() {
		return fromWxTotal;
	}

	public void setFromWxTotal(int fromWxTotal) {
		this.fromWxTotal = fromWxTotal;
	}

	public int getNewUsersTotal() {
		return newUsersTotal;
	}

	public void setNewUsersTotal(int newUsersTotal) {
		this.newUsersTotal = newUsersTotal;
	}

	public int getTransformRate() {
		return transformRate;
	}

	public void setTransformRate(int transformRate) {
		this.transformRate = transformRate;
	}
	
	


	
	
	
	
	

}
