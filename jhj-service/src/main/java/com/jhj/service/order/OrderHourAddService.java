package com.jhj.service.order;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderPrices;

/**
 *
 * @author :hulj
 * @Date : 2015年7月23日下午3:50:58
 *
 */
public interface OrderHourAddService {
	
	OrderPrices getOrderPriceSum(Long serviceType, String serviceAddons, Short serviceHour);

	List<OrgStaffs> getBestOrgStaff(Long userId, Long orderId);

	Orgs getMatchOrgId(Long addrId);

	List<Long> getBadRateStaffIds(Long userId, Long orgId);
	
	List<Long> getMatchTagStaffIds(Long orderId, List<Long> staffIds);
	
	
	//2016年3月14日18:03:46  jhj2.1 计算 服务价格
	
	OrderPrices getNewOrderPrice(Long serviceType);
	
}
