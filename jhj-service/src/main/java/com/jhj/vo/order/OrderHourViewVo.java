package com.jhj.vo.order;

import java.math.BigDecimal;
import com.jhj.po.model.order.Orders;

/**
 *
 * @author :hulj
 * @Date : 2015年8月5日上午10:53:35
 * @Description: 
 * 		钟点工 -- 订单详情  VO
 *	
 *		助理预约单，也用到了这个VO
 *			
 */
public class OrderHourViewVo extends Orders{
	
	//order_prices 表字段
	
	
	private  Long couponId;		//优惠券（支付页面，传值 为 couponId）
	
	private BigDecimal couponValue; //优惠券 面值（详情页面展示 ，为 value）
	
	private String couponName;
	
	private BigDecimal orderPay;	// 订单实际支付金额
	
	private BigDecimal orderMoney;
	
	private BigDecimal restMoney;	//用户余额
	
	
	//order_dispatches 表字段

	private String addrName;   		//服务地址名称
	
	private String orderStatusName; // 订单状态 名称
	
	private String orderTypeName; //  订单类型名称
	
	private String serviceTypeName; // 服务品类名称
	
	private String serviceDateStr;
	
	private String name;
	
	private String sex;
	
	private String overWorkStr;
	
	
	public BigDecimal getRestMoney() {
		return restMoney;
	}

	public void setRestMoney(BigDecimal restMoney) {
		this.restMoney = restMoney;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getOrderStatusName() {
		return orderStatusName;
	}

	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	public String getAddrName() {
		return addrName;
	}

	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public BigDecimal getCouponValue() {
		return couponValue;
	}

	public void setCouponValue(BigDecimal couponValue) {
		this.couponValue = couponValue;
	}

	public BigDecimal getOrderPay() {
		return orderPay;
	}

	public void setOrderPay(BigDecimal orderPay) {
		this.orderPay = orderPay;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public String getServiceDateStr() {
		return serviceDateStr;
	}

	public void setServiceDateStr(String serviceDateStr) {
		this.serviceDateStr = serviceDateStr;
	}

	public String getOverWorkStr() {
		return overWorkStr;
	}

	public void setOverWorkStr(String overWorkStr) {
		this.overWorkStr = overWorkStr;
	}

	
	
}
