package com.jhj.vo.staff;

import com.jhj.po.model.bs.OrgStaffDetailPay;


public class OrgStaffDetailPayOaVo extends OrgStaffDetailPay{
	
	//private Long staffId;
	//交易来源 订单收入 = order_type = 0  1 2 3 还款金额 = order_type = 4 提现金额 = order_type = 5 
	//补贴金额 = order_type = 6 利息金额 = order_type = 7 各项核减 = order_type = 8
	private String orderTypeName;
	//服务人员姓名
	private String name;
	//金额， 注意有 +  -  号，根据order_type来（只有4和5 为 -），
	//加号情况为    0 = 钟点工订单    1 = 深度保洁订单    2 = 助理订单  3 = 送酒订单  5 = 提现   7 = 利息  9 = 推荐奖励
	//减号情况为    4 = 还款订单   6 = 补贴    8= 各项核减
	//private String OrderPay;
	//下单用户手机号，有为空的情况，比如还款订单和提现，不需要用户手机号
	//private String mobile;
//	private String addTimeStr;
	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
