package com.jhj.vo.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.Orders;

/**
 *
 * @author :hulj
 * @Date : 2015年8月11日上午10:47:12
 * @Description: 
 *		
 *		运营平台--订单详情展示页
 */
/**
 *
 * @author :hulj
 * @Date : 2015年8月19日下午3:29:03
 *
 */
public class OaOrderListVo extends Orders {
	
	//order_prices
	private BigDecimal orderMoney;	//订单总金额
	private BigDecimal orderPay;	//订单实际支付金额
	
	private Short payType;			//支付方式
	
	private String payTypeName;		//支付方式名称
	
	private String orderStatusName; //订单状态名称
	
//	//order_dispatchs
	private String staffName;		//工作人员姓名
	private String staffMobile;		//工作人员手机号
	
	private String orderAddress;	//服务地址
	
	private BigDecimal couponValue;	//优惠券金额
	
	private String couponName;		//优惠券 名称 ： ex: 50元钟点工代金券
	
	
	private String userName;		//用户姓名
	
	private String cityName;		//城市名称
	
	
	private String orderDate;		//下单时间
	
	
	private List<OrgStaffs> staffList;	// 当前门店 下 的  阿姨 列表
	
	private Long staffId;			// 对于  已经有 派工的 订单， 则 该字段表示  已派工的 服务人员id	
	
	private Map<Long, String> staMap;	// 列表展示
	
	private String orgName;			// 当前订单的门店名称
	
	private String disStatusName;	//派工状态名称
	
	private Short disStatus;	//派工状态
	
	private List<OrgStaffsNewVo> voList;	// 可用 服务人员  VO 

	private String poiLongitude;//钟点工取货地址经度
	
	private String poiLatitude;//钟点工取货地址维度
	
	private String pickAddrName;//取货地址名称
	
	private String pickAddr;//取货门牌号
	
	private String flag;//是否显示阿姨列表0=不显示，1=显示
	
	private int userAddrDistance;//距离用户距离
	
	private Map<String,String> userAddrMap;
	
	private String userAddrKey;//用户选中地址
	
	
	private String serviceDateStr;	//服务时间 字符串，页面需要
	
	private String serviceDateStartStr;	// 服务开始时间，
	
	private String serviceDateEndStr;	//服务结束时间
	
	
	
	//2016年3月29日17:36:47  jhj2.1 之后，都具体了具体的 服务类型。如 钟点工-->金牌保洁初体验。金牌保洁深度体验、、、、
	private String orderTypeName;
	
	private String cloudOrgName; // 云店名称
	
	
	private Short disWay;	//派工方案标识。方案一，修改服务时间    方案二，根据 云店下拉选择加载服务人员
	
	
	//2016年5月13日17:35:13
	private Long parentServiceTypeId;	//对于  助理类--深度养护--需要有 开始时间
	
	
	
	
	public String getServiceDateStartStr() {
		return serviceDateStartStr;
	}
	public void setServiceDateStartStr(String serviceDateStartStr) {
		this.serviceDateStartStr = serviceDateStartStr;
	}
	public String getServiceDateEndStr() {
		return serviceDateEndStr;
	}
	public void setServiceDateEndStr(String serviceDateEndStr) {
		this.serviceDateEndStr = serviceDateEndStr;
	}
	public Long getParentServiceTypeId() {
		return parentServiceTypeId;
	}
	public void setParentServiceTypeId(Long parentServiceTypeId) {
		this.parentServiceTypeId = parentServiceTypeId;
	}
	public Short getDisWay() {
		return disWay;
	}
	public void setDisWay(Short disWay) {
		this.disWay = disWay;
	}
	public String getCloudOrgName() {
		return cloudOrgName;
	}
	public void setCloudOrgName(String cloudOrgName) {
		this.cloudOrgName = cloudOrgName;
	}
	public String getOrderTypeName() {
		return orderTypeName;
	}
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	public String getServiceDateStr() {
		return serviceDateStr;
	}
	public void setServiceDateStr(String serviceDateStr) {
		this.serviceDateStr = serviceDateStr;
	}
	public String getUserAddrKey() {
		return userAddrKey;
	}
	public void setUserAddrKey(String userAddrKey) {
		this.userAddrKey = userAddrKey;
	}
	public Map<String, String> getUserAddrMap() {
		return userAddrMap;
	}
	public void setUserAddrMap(Map<String, String> userAddrMap) {
		this.userAddrMap = userAddrMap;
	}
	public int getUserAddrDistance() {
		return userAddrDistance;
	}
	public void setUserAddrDistance(int userAddrDistance) {
		this.userAddrDistance = userAddrDistance;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getPickAddrName() {
		return pickAddrName;
	}
	public void setPickAddrName(String pickAddrName) {
		this.pickAddrName = pickAddrName;
	}
	public String getPickAddr() {
		return pickAddr;
	}
	public void setPickAddr(String pickAddr) {
		this.pickAddr = pickAddr;
	}
	public String getPoiLatitude() {
		return poiLatitude;
	}
	public void setPoiLatitude(String poiLatitude) {
		this.poiLatitude = poiLatitude;
	}
	public String getPoiLongitude() {
		return poiLongitude;
	}
	public void setPoiLongitude(String poiLongitude) {
		this.poiLongitude = poiLongitude;
	}
	public List<OrgStaffsNewVo> getVoList() {
		return voList;
	}
	public void setVoList(List<OrgStaffsNewVo> voList) {
		this.voList = voList;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public Short getDisStatus() {
		return disStatus;
	}
	public void setDisStatus(Short disStatus) {
		this.disStatus = disStatus;
	}
	public String getDisStatusName() {
		return disStatusName;
	}
	public void setDisStatusName(String disStatusName) {
		this.disStatusName = disStatusName;
	}
	
	
	public Long getStaffId() {
		return staffId;
	}
	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
	public Map<Long, String> getStaMap() {
		return staMap;
	}
	public void setStaMap(Map<Long, String> staMap) {
		this.staMap = staMap;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public List<OrgStaffs> getStaffList() {
		return staffList;
	}
	public void setStaffList(List<OrgStaffs> staffList) {
		this.staffList = staffList;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getStaffMobile() {
		return staffMobile;
	}
	public void setStaffMobile(String staffMobile) {
		this.staffMobile = staffMobile;
	}
	public String getOrderStatusName() {
		return orderStatusName;
	}
	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public BigDecimal getCouponValue() {
		return couponValue;
	}
	public void setCouponValue(BigDecimal couponValue) {
		this.couponValue = couponValue;
	}
	public String getOrderAddress() {
		return orderAddress;
	}
	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}
	public Short getPayType() {
		return payType;
	}
	public void setPayType(Short payType) {
		this.payType = payType;
	}
	public BigDecimal getOrderMoney() {
		return orderMoney;
	}
	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}
	public BigDecimal getOrderPay() {
		return orderPay;
	}
	public void setOrderPay(BigDecimal orderPay) {
		this.orderPay = orderPay;
	}
//	public String getStaffName() {
//		return staffName;
//	}
//	public void setStaffName(String staffName) {
//		this.staffName = staffName;
//	}
//	public String getStaffMobile() {
//		return staffMobile;
//	}
//	public void setStaffMobile(String staffMobile) {
//		this.staffMobile = staffMobile;
//	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
		result = prime * result + ((couponName == null) ? 0 : couponName.hashCode());
		result = prime * result + ((couponValue == null) ? 0 : couponValue.hashCode());
		result = prime * result + ((disStatus == null) ? 0 : disStatus.hashCode());
		result = prime * result + ((disStatusName == null) ? 0 : disStatusName.hashCode());
		result = prime * result + ((orderAddress == null) ? 0 : orderAddress.hashCode());
		result = prime * result + ((orderDate == null) ? 0 : orderDate.hashCode());
		result = prime * result + ((orderMoney == null) ? 0 : orderMoney.hashCode());
		result = prime * result + ((orderPay == null) ? 0 : orderPay.hashCode());
		result = prime * result + ((orderStatusName == null) ? 0 : orderStatusName.hashCode());
		result = prime * result + ((orgName == null) ? 0 : orgName.hashCode());
		result = prime * result + ((payType == null) ? 0 : payType.hashCode());
		result = prime * result + ((payTypeName == null) ? 0 : payTypeName.hashCode());
		result = prime * result + ((staMap == null) ? 0 : staMap.hashCode());
		result = prime * result + ((staffId == null) ? 0 : staffId.hashCode());
		result = prime * result + ((staffList == null) ? 0 : staffList.hashCode());
		result = prime * result + ((staffMobile == null) ? 0 : staffMobile.hashCode());
		result = prime * result + ((staffName == null) ? 0 : staffName.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OaOrderListVo other = (OaOrderListVo) obj;
		if (cityName == null) {
			if (other.cityName != null)
				return false;
		} else if (!cityName.equals(other.cityName))
			return false;
		if (couponName == null) {
			if (other.couponName != null)
				return false;
		} else if (!couponName.equals(other.couponName))
			return false;
		if (couponValue == null) {
			if (other.couponValue != null)
				return false;
		} else if (!couponValue.equals(other.couponValue))
			return false;
		if (disStatus == null) {
			if (other.disStatus != null)
				return false;
		} else if (!disStatus.equals(other.disStatus))
			return false;
		if (disStatusName == null) {
			if (other.disStatusName != null)
				return false;
		} else if (!disStatusName.equals(other.disStatusName))
			return false;
		if (orderAddress == null) {
			if (other.orderAddress != null)
				return false;
		} else if (!orderAddress.equals(other.orderAddress))
			return false;
		if (orderDate == null) {
			if (other.orderDate != null)
				return false;
		} else if (!orderDate.equals(other.orderDate))
			return false;
		if (orderMoney == null) {
			if (other.orderMoney != null)
				return false;
		} else if (!orderMoney.equals(other.orderMoney))
			return false;
		if (orderPay == null) {
			if (other.orderPay != null)
				return false;
		} else if (!orderPay.equals(other.orderPay))
			return false;
		if (orderStatusName == null) {
			if (other.orderStatusName != null)
				return false;
		} else if (!orderStatusName.equals(other.orderStatusName))
			return false;
		if (orgName == null) {
			if (other.orgName != null)
				return false;
		} else if (!orgName.equals(other.orgName))
			return false;
		if (payType == null) {
			if (other.payType != null)
				return false;
		} else if (!payType.equals(other.payType))
			return false;
		if (payTypeName == null) {
			if (other.payTypeName != null)
				return false;
		} else if (!payTypeName.equals(other.payTypeName))
			return false;
		if (staMap == null) {
			if (other.staMap != null)
				return false;
		} else if (!staMap.equals(other.staMap))
			return false;
		if (staffId == null) {
			if (other.staffId != null)
				return false;
		} else if (!staffId.equals(other.staffId))
			return false;
		if (staffList == null) {
			if (other.staffList != null)
				return false;
		} else if (!staffList.equals(other.staffList))
			return false;
		if (staffMobile == null) {
			if (other.staffMobile != null)
				return false;
		} else if (!staffMobile.equals(other.staffMobile))
			return false;
		if (staffName == null) {
			if (other.staffName != null)
				return false;
		} else if (!staffName.equals(other.staffName))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	
	
	
}
