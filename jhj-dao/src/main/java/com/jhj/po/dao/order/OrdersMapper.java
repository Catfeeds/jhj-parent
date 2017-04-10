package com.jhj.po.dao.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.order.Orders;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.jhj.vo.chart.CoopUserOrderVo;
import com.jhj.vo.order.OrderQuerySearchVo;
import com.jhj.vo.order.OrderSearchVo;

public interface OrdersMapper {

	int deleteByPrimaryKey(Long id);

	Long insert(Orders record);

	int insertSelective(Orders record);

	int updateByPrimaryKeySelective(Orders record);

	int updateByPrimaryKey(Orders record);

	int updateByUpdateTimeSelective(Orders orders);

	int updateByCleanUpdateTimeSelective(Orders orders);

	Orders selectByPrimaryKey(Long id);

	Orders selectByOrderNo(String orderNo);

	List<Orders> selectBySearchVo(OrderSearchVo searchVo);

	List<Orders> selectByListPage(OrderSearchVo searchVo);
	
	//查询最新一条记录
	List<Orders> getNewestOrder(Orders order);

	/********************************************
	 * 统 计
	 *********************************************/

	List<HashMap> totalByUserIds(List<Long> id);

	int totalIntimacyOrders(Map<String, Long> map);

	// 当前已预约订单的数量
	Long totalRemindCountToDo(Long userId);

	/*
	 * 市场订单图表
	 */
	// 按天统计
	List<ChartMapVo> statByDay(ChartSearchVo chartSearchVo);

	// 按天统计--退单数
	List<ChartMapVo> statOrderCancelByDay(ChartSearchVo chartSearchVo);

	// 按月统计--退单数
	List<ChartMapVo> statOrderCancelByMonth(ChartSearchVo chartSearchVo);

	// 按季度统计--退单数
	List<ChartMapVo> statOrderCancelByQuarter(ChartSearchVo chartSearchVo);

	// 订单统计 订单数求和
	Map<String, Integer> statTotalOrder(ChartSearchVo chartSearchVo);

	/*
	 * 订单收入图表
	 */

	List<ChartMapVo> orderRevenueByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> orderRevenueByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> orderRevenueByQuarter(ChartSearchVo chartSearchVo);

	/*
	 * 市场品类图表
	 */
	List<ChartMapVo> chartTypeByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> chartTypeByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> chartTypeByQuarter(ChartSearchVo chartSearchVo);
	
	

	/*
	 * 不同时间粒度，统计下过单子的用户个数
	 */
	List<Orders> statUserByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> totalByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> totalByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> totalByQuarter(ChartSearchVo chartSearchVo);

	/*
	 * 品类收入图表
	 */
	List<ChartMapVo> chartTypeRevenue(ChartSearchVo chartSearchVo);

	/*
	 * 助理品类图表
	 */
	List<ChartMapVo> statChartAmTypeByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> statChartAmTypeByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> statChartAmTypeByQuarter(ChartSearchVo chartSearchVo);

	/*
	 * 助理品类收入
	 */
	List<ChartMapVo> statChartServiceTypeByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> statServiceTypeByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> statServiceTypeByQuarter(ChartSearchVo chartSearchVo);

	/*
	 * 2015-10-29 16:37:53
	 * 话费订单图表（数量）
	 */
	List<ChartMapVo> phoneRechargeByDay(ChartSearchVo chartSearchVo);

	List<ChartMapVo> phoneRechargeByMonth(ChartSearchVo chartSearchVo);

	List<ChartMapVo> phoneRechargeByQuarter(ChartSearchVo chartSearchVo);

	BigDecimal getTotalOrderMoney(OrderSearchVo vo);
	
	BigDecimal getTotalOrderMoneyExt(OrderSearchVo vo);

	BigDecimal getTotalOrderPay(OrderSearchVo vo);
	
	BigDecimal getTotalOrderPayExt(OrderSearchVo vo);

	Long getTotalOrderCount(OrderSearchVo vo);

	Long getTotalOrderCountByMouth(OrderQuerySearchVo searchVo);

	// 钟点工订单0订单总金额
	BigDecimal getTotalOrderIncomeHourMoney(OrderSearchVo vo);
	
	BigDecimal getTotalOrderIncomeHourMoneyExt(OrderSearchVo vo);

	// 助理订单2订单总金额
	BigDecimal getTotalOrderIncomeStaffMoney(OrderQuerySearchVo vo);

	// 配送订单3订单总金额
	BigDecimal getTotalOrderIncomeRunMoney(OrderQuerySearchVo vo);

	List<HashMap> userTotalByMonth(OrderSearchVo orderSearchVo);

	List<HashMap> userAllTotalByMonth(OrderSearchVo orderSearchVo);

	Long totalOrderInUserIds(List<Long> userIds);

	List<CoopUserOrderVo> totalUserAndOrder(List<Long> userIds);
	
	//订单来源统计
	List<ChartMapVo> getOrderSrc(ChartSearchVo chartSearchVo);
	
	//统计复购率
	List<ChartMapVo> totalByRate(ChartSearchVo chartSearchVo);
	
	//订单复购率
	List<ChartMapVo> totalByRateOrder(ChartSearchVo chartSearchVo);
	
	List<ChartMapVo> statByTotal(ChartSearchVo chartSearchVo);
	
	//查询用户的最后下单情况
	List<Long> getLastOrder(OrderSearchVo searchVo);
}