package com.jhj.vo.order;

import java.math.BigDecimal;
import java.util.List;

import com.jhj.po.model.bs.DictCoupons;


/**
 *
 * @author :hulj
 * @Date : 2015年7月31日下午3:03:08
 * @Description: 钟点工--支付页面 VO
 *
 */
public class OrderHourPayVo {
	
	private BigDecimal orderMoney; 		//总计金额
	
	private Long serviceDate;			//服务开始时间
	private Short serviceHour;			//服务时长。。二者 确定结束时间
	
	
	private String serviceAddr;		//服务地址
	private String remark;			//备注
	
	private List<DictCoupons> dictList;	//优惠券
	
	private Long realOrderMoney;	//实际支付金额
	private Short payType;			//支付方式
	
	
	
	public List<DictCoupons> getDictList() {
		return dictList;
	}
	public void setDictList(List<DictCoupons> dictList) {
		this.dictList = dictList;
	}
	public BigDecimal getOrderMoney() {
		return orderMoney;
	}
	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}
	
	
	public Long getServiceDate() {
		return serviceDate;
	}
	public void setServiceDate(Long serviceDate) {
		this.serviceDate = serviceDate;
	}
	public Short getServiceHour() {
		return serviceHour;
	}
	public void setServiceHour(Short serviceHour) {
		this.serviceHour = serviceHour;
	}
	public String getServiceAddr() {
		return serviceAddr;
	}
	public void setServiceAddr(String serviceAddr) {
		this.serviceAddr = serviceAddr;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
//	public List<UserCoupons> getCouponList() {
//		return couponList;
//	}
//	public void setCouponList(List<UserCoupons> couponList) {
//		this.couponList = couponList;
//	}
	public Long getRealOrderMoney() {
		return realOrderMoney;
	}
	public void setRealOrderMoney(Long realOrderMoney) {
		this.realOrderMoney = realOrderMoney;
	}
	public Short getPayType() {
		return payType;
	}
	public void setPayType(Short payType) {
		this.payType = payType;
	}
	
	
	
	
}
