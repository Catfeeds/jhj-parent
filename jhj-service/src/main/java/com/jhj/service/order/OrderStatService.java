package com.jhj.service.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.vo.chart.CoopUserOrderVo;
import com.jhj.vo.order.OrderQuerySearchVo;

public interface OrderStatService {
	
	List<Map<String, Object>> selectOrdersCountByYearAndMonth(Long orgStaffId,String start,String end);

	List<HashMap> totalByUserIds(List<Long> id);

	int totalIntimacyOrders(Map<String, Long> map);

	Long totalOrderInUserIds(List<Long> userIds);

	List<CoopUserOrderVo> totalUserAndOrder(List<Long> userIds);

	BigDecimal getTotalOrderMoney(OrderQuerySearchVo vo);

	BigDecimal getTotalOrderIncomeMoney(OrderQuerySearchVo vo);

	Long getTotalOrderCount(OrderQuerySearchVo vo);

	Long getTotalOrderCountByMouth(OrderQuerySearchVo searchVo);


}
