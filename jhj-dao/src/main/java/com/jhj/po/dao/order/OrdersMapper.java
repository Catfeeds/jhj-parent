package com.jhj.po.dao.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.order.Orders;
import com.jhj.vo.OaOrderSearchVo;
import com.jhj.vo.OaPhoneChargeOrderSearchVo;
import com.jhj.vo.OaRemindOrderSearchVo;
import com.jhj.vo.OrderQuerySearchVo;
import com.jhj.vo.OrderSearchVo;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.jhj.vo.chart.CoopUserOrderVo;

public interface OrdersMapper {
	
    int deleteByPrimaryKey(Long id);

    Long insert(Orders record);

    int insertSelective(Orders record);

    Orders selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);
    
    List<Orders> selectBadOrderRateByUserId(Long userId);

	Orders selectByUserId(Long userId);

	List<Orders> selectByUserIdList(Long userId);

	int updateByUpdateTimeSelective(Orders orders);

    Orders selectByOrderNo(String orderNo);

    List<Orders> selectByListPage(OrderSearchVo orderSearchVo);

    List<Orders> selectByStatus(Short orderStatus);

    List<Orders> selectByStatuses(List<Short> orderStatus);

	List<String> selectByDistinctMobileLists();

	//List<Orders> selectByUserId(Long userId);
	
	Orders selectByUser(Long userId);
	
	List<Orders> selectAmOrderList(Long amId);

	List<Orders> selectByListPages();

	int updateByCleanUpdateTimeSelective(Orders orders);
	
	List<Orders> selectByUserListPage(OrderSearchVo orderSearchVo);
	
	List<Orders> selectNowOrderHourByListPage(OrderSearchVo orderSearchVo);
	
	List<Orders> selectOldOrderHourByListPage(OrderSearchVo orderSearchVo);

	Orders selectByAmId(Long amId);

	List<Orders> selectOaOrderByListPage(OaOrderSearchVo oaOrderSearchVo);
	
	List<HashMap> totalByUserIds(List<Long> id);

	List<Orders> selectByAmIdGroupByUserId(Long amId);

	int getIntimacyOrders(Map<String, Long> map);
	
	List<Orders> selectByAmIdAndOrderType(@Param("amId")Long amId,@Param("format") String format);

	List<Orders> selectBadOrderRateByUserId(Long userId, Long orgId);
	
	
	List<Orders> selectAllAmOrder(Long amId);
	
	
	List<Orders> selectOaRemindOrderByListPage(OaRemindOrderSearchVo searchVo);
	
	
	
	/*
	 *  提醒类 订单
	 */
	
	//我的提醒（当前提醒） 用户端
	List<Orders>  selectNowRemind(OrderSearchVo orderSearchVo);
	
	//历史提醒（历史提醒）用户端
	List<Orders> selectOldRemind(OrderSearchVo orderSearchVo);	
	
	// 已预约提醒 （助理端）
	List<Orders> selectNowRemindAm(OrderSearchVo orderSearchVo);
	
	// 已完成 和 已取消 提醒 （助理端）
	List<Orders> selectOldRemindAm(OrderSearchVo orderSearchVo);
	
	//当前已预约订单的数量
	Long getRemindCountToDo(Long userId);
	
	/*
	 *	话费充值订单 
	 */
	List<Orders>  selectPhoneOrderListPage(OaPhoneChargeOrderSearchVo searchVo);
	
	
	
	/*
	 * 定时任务
	 */
	
	List<Orders> selectBeforeService();
	
	List<Orders> selectDuringService();
	
	List<Orders> selectAfterService();
	
	List<Orders> selectOverTimeNotPay();
	
	List<Orders> selectOverSevenDay();
	
	//,助理 24小时，已支付，变 完成
	List<Orders> selectAmOrderOverOneDay();
	
	//助理和 深度 , 待确认的订单, 超过 3小时变为  已关闭
	List<Orders> selectChangeToClose();
	
	//提醒类 订单 ，超过服务时间，变为 提醒已完成
	List<Orders> selectRemindOverServiceDate();
	
	//提醒类订单，服务时间 前 30分钟 时，给助理发短信
	List<Orders> selectRemindBeforeHalfHour();
	
	//TODO 话费充值订单, 支付时间 超过 1小时, 订单状态由 13(支付中) 变为  16 (取消)
	List<Orders> selectPhoneOrderOverOneHour();
	
	//订单开始服务超过8小时还没点击完成，服务，则需要系统自动变成完成服务
	List<Orders> selectByOrderStatus();
	
	/********************************************
	  				统		计
	 *********************************************/
	
	/*
	 *  市场订单图表
	 */
	//按天统计
	List<ChartMapVo> statByDay(ChartSearchVo chartSearchVo);
	
	//按月统计
	List<ChartMapVo> statByMonth(ChartSearchVo chartSearchVo);
	
	//按季度统计
	List<ChartMapVo> statByQuarter(ChartSearchVo chartSearchVo);
	
	//按天统计--退单数
	List<ChartMapVo> statOrderCancelByDay(ChartSearchVo chartSearchVo);
	
	//按月统计--退单数
	List<ChartMapVo> statOrderCancelByMonth(ChartSearchVo chartSearchVo);	

	//按季度统计--退单数
	List<ChartMapVo> statOrderCancelByQuarter(ChartSearchVo chartSearchVo);		
	
	//订单统计 订单数求和
	Map<String,Integer> statTotalOrder(ChartSearchVo chartSearchVo);

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
	List<ChartMapVo> chartTypeRevenueByDay(ChartSearchVo chartSearchVo);
	
	List<ChartMapVo> chartTypeRevenueByMonth(ChartSearchVo chartSearchVo);
	
	List<ChartMapVo> chartTypeRevenueByQuarter(ChartSearchVo chartSearchVo);
	
	/*
	 * 助理品类图表
	 */
 	List<ChartMapVo> statChartAmTypeByDay(ChartSearchVo chartSearchVo);
  	
  	List<ChartMapVo> statChartAmTypeByMonth(ChartSearchVo chartSearchVo);
  	
  	List<ChartMapVo> statChartAmTypeByQuarter(ChartSearchVo chartSearchVo);
  	
  	/*
  	 *助理品类收入 
  	 */
  	List<ChartMapVo> statChartServiceTypeByDay(ChartSearchVo chartSearchVo);
  	
  	List<ChartMapVo> statServiceTypeByMonth(ChartSearchVo chartSearchVo);
  	
  	List<ChartMapVo> statServiceTypeByQuarter(ChartSearchVo chartSearchVo);
  	
  	/*
  	 * 2015-10-29 16:37:53  
  	 * 		话费订单图表（数量）
  	 */
  	 List<ChartMapVo> phoneRechargeByDay(ChartSearchVo chartSearchVo);
  	 
  	 List<ChartMapVo> phoneRechargeByMonth(ChartSearchVo chartSearchVo);
  	 
  	 List<ChartMapVo> phoneRechargeByQuarter(ChartSearchVo chartSearchVo);

	BigDecimal getTotalOrderMoney(OrderQuerySearchVo vo);

	BigDecimal getTotalOrderIncomeMoney(OrderQuerySearchVo vo);

	Long getTotalOrderCount(OrderQuerySearchVo vo);

	Long getTotalOrderCountByMouth(OrderQuerySearchVo searchVo);

	
	//钟点工订单0订单总金额
	BigDecimal getTotalOrderIncomeHourMoney(OrderQuerySearchVo vo);
	//深度保洁1订单总金额
	BigDecimal getTotalOrderIncomeCleanMoney(OrderQuerySearchVo vo);
	//助理订单2订单总金额
	BigDecimal getTotalOrderIncomeStaffMoney(OrderQuerySearchVo vo);
	//配送订单3订单总金额
	BigDecimal getTotalOrderIncomeRunMoney(OrderQuerySearchVo vo);

	List<HashMap> userTotalByMonth(OrderSearchVo orderSearchVo);
	
	List<HashMap> userAllTotalByMonth(OrderSearchVo orderSearchVo);
	
	Long totalOrderInUserIds(List<Long> userIds);
	
	List<CoopUserOrderVo> totalUserAndOrder(List<Long> userIds);
	
	List<Orders> selectByMap(Map<String,Long> map);
}