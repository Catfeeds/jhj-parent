package com.jhj.service.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.vo.chart.CoopUserOrderVo;
import com.jhj.vo.order.OrderQuerySearchVo;
import com.jhj.vo.order.OrderSearchVo;

public interface OrderStatService {
	
	List<Map<String, Object>> selectOrdersCountByYearAndMonth(Long orgStaffId,String start,String end);

	List<HashMap> totalByUserIds(List<Long> id);

	int totalIntimacyOrders(Map<String, Long> map);

	Long totalOrderInUserIds(List<Long> userIds);

	List<CoopUserOrderVo> totalUserAndOrder(List<Long> userIds);

	Long getTotalOrderCountByMouth(OrderQuerySearchVo searchVo);

	Long getTotalOrderCount(OrderSearchVo vo);

	BigDecimal getTotalOrderMoney(OrderSearchVo vo);

	BigDecimal getTotalOrderIncomeMoney(OrderSearchVo vo);

}
