package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffsMapper;
import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.user.UserAddrsMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderStatService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.orderReview.SettingService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.vo.chart.CoopUserOrderVo;
import com.jhj.vo.order.OrderQuerySearchVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrderStatServiceImpl implements OrderStatService {

	@Autowired
	private OrdersMapper ordersMapper;
	
	@Autowired
	private OrderDispatchsService orderDispatchService;
	
	@Autowired
	private OrdersService orderService;

	@Autowired
	private UserAddrsService userAddrsSerivce;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;

	@Override
	public List<Map<String, Object>> selectOrdersCountByYearAndMonth(Long orgStaffId, String start, String end) {
		Long startLong = TimeStampUtil.getMillisOfDay(start) / 1000;
		Long endLong = TimeStampUtil.getMillisOfDay(end) / 1000;
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("startTime", startLong);
		conditions.put("endTime", endLong);
		conditions.put("orgStaffId", orgStaffId);
		List<Map<String, Object>> list1 = orderDispatchService.totalByYearAndMonth(conditions);
		int total = 0;
		// boolean flag = true;
		for (Iterator<Map<String, Object>> iterator = list1.iterator(); iterator.hasNext();) {
			total = list1.size();
			Map<String, Object> map = iterator.next();
			Map<String, Object> map1 = new HashMap<String, Object>();
			// String serviceDate = (String)map.get("startTime");
			String orderNo = (String) map.get("order_no");
			Long staffId = (Long) map.get("staff_id");

			OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(staffId);
			Orders orders = orderService.selectByOrderNo(orderNo);
			if(orders!=null){
				Long updateTime = (Long) map.get("update_time");
				String serviceEnd = map.get("service_end").toString();
				if (orders.getOrderType().equals(Constants.ORDER_TYPE_1)) {
					if (orders.getOrderStatus() >= 7) {
						Long servicePlanEnd = orders.getServiceDate() + orders.getServiceHour() * 3600;
						if (updateTime < servicePlanEnd) {
							// %Y-%m-%d %H:%i:%s
							serviceEnd = TimeStampUtil.timeStampToDateStr(updateTime * 1000);
						}
					}
				}
				
				UserAddrs userAddrs = userAddrsSerivce.selectByPrimaryKey(orders.getAddrId());
				if (userAddrs != null) {
					String detailAddrs = userAddrs.getName() + userAddrs.getAddr();
					map1.put("id", map.get("id"));
					map1.put("start", map.get("serviceTime"));
					map1.put("end", map.get("service_end"));
					map1.put("title", orgStaffs.getName() + "有1个派工," + "  服务地址：" + detailAddrs);
					map1.put("color", "blue");
					
					Short orderType = orders.getOrderType();
					
					// 钟点工订单--钟点工订单列表
					if (orderType == Constants.ORDER_TYPE_0) {
						// map1.put("url","/jhj-oa/order/cal-list?name='agendaDay'&serviceDate="+serviceDate+"&staffId="+orgStaffId);
						
						map1.put("url", "/jhj-oa/order/order-hour-list?orderNo=" + orderNo);
					}
					
					// 助理订单--助理订单列表
					if (orderType == Constants.ORDER_TYPE_2) {
						map1.put("url", "/jhj-oa/order/order-am-list?orderNo=" + orderNo);
					}
				}
				listMap.add(map1);
			}
		}
		return listMap;
	}

	@Override
	public List<HashMap> totalByUserIds(List<Long> id) {
		return ordersMapper.totalByUserIds(id);
	}

	@Override
	public int totalIntimacyOrders(Map<String, Long> map) {
		return ordersMapper.totalIntimacyOrders(map);
	}
	
	@Override
	public Long totalOrderInUserIds(List<Long> userIds) {
		return ordersMapper.totalOrderInUserIds(userIds);
	}

	@Override
	public List<CoopUserOrderVo> totalUserAndOrder(List<Long> userIds) {
		return ordersMapper.totalUserAndOrder(userIds);
	}

	// . 根据开始时间，接收时间，staff_id ，得出订单总金额
	@Override
	public BigDecimal getTotalOrderMoney(OrderQuerySearchVo vo) {

		BigDecimal orderMoney = ordersMapper.getTotalOrderMoney(vo);

		if (orderMoney == null) {
			BigDecimal a = new BigDecimal(0);
			return a;
		}
		orderMoney = MathBigDecimalUtil.round(orderMoney, 2);
		return orderMoney;

	}
	
	// . 根据开始时间，接收时间，staff_id ，得出订单总收入金额
	@Override
	public BigDecimal getTotalOrderIncomeMoney(OrderQuerySearchVo vo) {

		// BigDecimal money = orderService.getTotalOrderIncomeMoney(vo);
		// 钟点工订单0订单总金额
		BigDecimal hourMoney = ordersMapper.getTotalOrderIncomeHourMoney(vo);
		// 深度保洁1订单总金额
		BigDecimal cleanMoney = ordersMapper.getTotalOrderIncomeCleanMoney(vo);
		// 助理订单2订单总金额
		BigDecimal staffMoney = ordersMapper.getTotalOrderIncomeStaffMoney(vo);
		// 配送订单3订单总金额
		BigDecimal disMoney = ordersMapper.getTotalOrderIncomeRunMoney(vo);

		Long staffId = vo.getStaffId();

		OrgStaffs staffs = orgStaffsService.selectByPrimaryKey(staffId);

		Short level = staffs.getLevel();
		String settingLevel = "-level-" + level.toString();

		String hoursettingType = "hour-ratio" + settingLevel;
		String cleansettingType = "deep-ratio" + settingLevel;
		String amsettingType = "am-ratio" + settingLevel;
		String dissettingType = "dis-ratio" + settingLevel;

		JhjSetting hour = settingService.selectBySettingType(hoursettingType);
		JhjSetting clean = settingService.selectBySettingType(cleansettingType);
		JhjSetting am = settingService.selectBySettingType(amsettingType);
		JhjSetting dis = settingService.selectBySettingType(dissettingType);

		// 钟点工提成
		BigDecimal hourRate = new BigDecimal(hour.getSettingValue());
		// 深度保洁提成
		BigDecimal cleanRate = new BigDecimal(clean.getSettingValue());
		// 助理提成
		BigDecimal amRate = new BigDecimal(am.getSettingValue());
		// 配送订单提成
		BigDecimal disRate = new BigDecimal(dis.getSettingValue());

		if (hourMoney == null) {
			hourMoney = new BigDecimal(0);
		}
		if (cleanMoney == null) {
			cleanMoney = new BigDecimal(0);
		}
		if (staffMoney == null) {
			staffMoney = new BigDecimal(0);
		}
		if (disMoney == null) {
			disMoney = new BigDecimal(0);
		}
		// 钟点工收入
		BigDecimal hourIncomingMoney = hourMoney.multiply(hourRate);
		// 深度保洁收入
		BigDecimal cleanIncomingMoney = cleanMoney.multiply(cleanRate);
		// 助理收入
		BigDecimal amIncomingMoney = staffMoney.multiply(amRate);
		// 配送订单收入
		BigDecimal disIncomingMoney = disMoney.multiply(disRate);
		// 订单总收入
		BigDecimal totalIncomingMoney = hourIncomingMoney.add(cleanIncomingMoney).add(amIncomingMoney).add(disIncomingMoney);

		if (totalIncomingMoney == null) {
			BigDecimal b = new BigDecimal(0);
			return b;
		}
		totalIncomingMoney = MathBigDecimalUtil.round(totalIncomingMoney, 2);

		return totalIncomingMoney;
	}
	
	// . 根据开始时间，接收时间，staff_id ，得出订单总数:
	@Override
	public Long getTotalOrderCount(OrderQuerySearchVo vo) {

		return ordersMapper.getTotalOrderCount(vo);
	}

	// 当月订单总数（order_status=7,8)
	@Override
	public Long getTotalOrderCountByMouth(OrderQuerySearchVo searchVo) {

		return ordersMapper.getTotalOrderCountByMouth(searchVo);
	}


}
