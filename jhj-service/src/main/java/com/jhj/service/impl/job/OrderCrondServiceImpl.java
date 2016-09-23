package com.jhj.service.impl.job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jhj.common.Constants;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.OrderRates;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.job.OrderCrondService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderRatesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.JsonOrderRateItemVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月24日下午4:17:01
 * @Description:
 *               定时任务
 */
@Service
public class OrderCrondServiceImpl implements OrderCrondService {

	@Autowired
	private OrdersService orderService;

	@Autowired
	private OrderDispatchsService orderDispatchsService;

	@Autowired
	private UsersService userService;

	@Autowired
	private OrderRatesService orderRateService;
	@Autowired
	private OrgStaffsService orgStaffService;

	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;

	@Autowired
	private OrderPricesService orderPricesService;

	@Autowired
	private OrgStaffsService orgStaffsService;

	/*
	 * 1.钟点工-- 服务开始前两小时 通知 -- 接收方： 用户
	 */
	@Override
	public void noticeBeforeService() {

		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderType(Constants.ORDER_TYPE_0);
		searchVo.setOrderStatus(Constants.ORDER_STATUS_4);

		Long now = TimeStampUtil.getNowMin();
		Long twoHourBefore = now - 2 * 3600;
		searchVo.setStartServiceTime(twoHourBefore);
		searchVo.setEndServiceTime(twoHourBefore);

		List<Orders> list = orderService.selectBySearchVo(searchVo);

		for (Orders order : list) {

			// xx月 xx 日 xx:xx~ xx:xx
			String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
			String endTimeStr = TimeStampUtil.timeStampToDateStr((order.getServiceDate() + order.getServiceHour() * 3600) * 1000, "HH:mm");
			String timeStr = beginTimeStr + "-" + endTimeStr;

			// 家政师 xx 　在开始前２小时之内换人???

			Long orderId = order.getId();
			OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
			searchVo1.setOrderId(orderId);
			searchVo1.setDispatchStatus((short) 1);
			List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo1);

			OrderDispatchs orderDispatch = null;
			if (!orderDispatchs.isEmpty()) {
				orderDispatch = orderDispatchs.get(0);
			}

			if (orderDispatch == null)
				continue;

			String staffName = orderDispatch.getStaffName();

			// 用户手机号
			Long userId = order.getUserId();

			Users user = userService.selectByPrimaryKey(userId);

			String[] tempStr = new String[] { timeStr, staffName };

			SmsUtil.SendSms(user.getMobile(), "29156", tempStr);

			// SmsUtil.SendSms("13521193653", "29156", tempStr);
		}

	}

	/*
	 * 2.服务开始时间内，将订单状态改为服务中
	 */
	@Override
	public void updateDuringService() {

		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderStatus(Constants.ORDER_STATUS_4);

		Long now = TimeStampUtil.getNowSecond();
		searchVo.setEndAddTime(now);
		searchVo.setStartServiceHourTime(now);

		List<Orders> list = orderService.selectBySearchVo(searchVo);

		for (Orders orders : list) {
			orders.setOrderStatus(Constants.ORDER_STATUS_5);
			orderService.updateByPrimaryKeySelective(orders);
		}
	}

	/*
	 * 3.服务结束时间后，如果订单状态还是ORDER_STATUS_5 ，则改为待评价状态
	 */
	@Override
	public void updateAfterService() {

		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderStatus(Constants.ORDER_STATUS_5);

		Long now = TimeStampUtil.getNowSecond();

		searchVo.setEndServiceHourTime(now);

		List<Orders> list = orderService.selectBySearchVo(searchVo);

		for (Orders orders : list) {
			orders.setOrderStatus(Constants.ORDER_STATUS_7);
			orders.setUpdateTime(TimeStampUtil.getNowSecond());
			orderService.updateByPrimaryKeySelective(orders);

			// 更新服务人员的财务信息，包括财务总表，财务明细，欠款明细，是否加入黑名单
			Long orderId = orders.getId();
			OrderPrices orderPrices = orderPricesService.selectByOrderId(orderId);

			String orderNo = orders.getOrderNo();
			// 更新orderdispatchs的更新时间
			OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
			searchVo1.setOrderNo(orderNo);
			searchVo1.setDispatchStatus((short) 1);
			List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo1);

			for (OrderDispatchs item : orderDispatchs) {
				Long staffId = item.getStaffId();

				OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(staffId);
				orgStaffFinanceService.orderDone(orders, orderPrices, orgStaffs);
			}

		}
	}

	/*
	 * 4.在钟点工状态为待支付状态 ORDER_STATUS_3，超过1个小时未支付的订单，将订单状态改为已关闭.
	 */
	@Override
	public void updateOverTimeNotPay() {

		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderType(Constants.ORDER_TYPE_0);
		searchVo.setOrderStatus(Constants.ORDER_STATUS_1);

		Long now = TimeStampUtil.getNowMin();

		searchVo.setStartAddTime(now + 1800);

		List<Orders> list = orderService.selectBySearchVo(searchVo);

		for (Orders orders : list) {
			orders.setOrderStatus(Constants.ORDER_STATUS_9);
			orders.setUpdateTime(TimeStampUtil.getNowSecond());
			orderService.updateByPrimaryKeySelective(orders);
		}
	}

	/*
	 * 5.钟点工订单已结束超过七天，默认评价，并且为好评
	 */
	@Override
	public void setOrderRateOverServenDay() {
		
		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderType(Constants.ORDER_TYPE_0);
		searchVo.setOrderStatus(Constants.ORDER_STATUS_7);

		Long now = TimeStampUtil.getNowMin();
		
		searchVo.setEndServiceHourTime(now - 7*24*3600);
		
		List<Orders> list = orderService.selectBySearchVo(searchVo);

		String rateDatas = "[{'rateType':0, 'rateValue': 0}," + "{'rateType':1, 'rateValue': 0}," + "{'rateType':2, 'rateValue': 0},"
				+ "{'rateType':3, 'rateValue': 0}," + "{'rateType':4, 'rateValue': 0}]"; // 默认全
																							// 好评

		for (Orders orders : list) {
			boolean aaa = generalDefaultOrderRates(orders, rateDatas);

			// 在 order_rates表生成记录 之后，修改 订单状态为 已评价
			if (aaa) {
				orders.setOrderStatus(Constants.ORDER_STATUS_8);

				orderService.updateByPrimaryKeySelective(orders);
			}
		}
	}

	// 为 某条订单 在评价 表中 生成 全好评 的 记录
	public boolean generalDefaultOrderRates(Orders orders, String rateDatas) {

		OrderRates orderRates = orderRateService.initOrderRates();

		orderRates.setOrderId(orders.getId());
		orderRates.setOrderNo(orders.getOrderNo());
		orderRates.setAmId(orders.getAmId());
		orderRates.setUserId(orders.getUserId());
		orderRates.setMobile(orders.getMobile());

		Gson gson = new Gson();
		// 创建一个JsonParser
		JsonParser parser = new JsonParser();

		// 通过JsonParser对象可以把json格式的字符串解析成一个JsonElement对象
		JsonElement el = parser.parse(rateDatas);

		// 把JsonElement对象转换成JsonArray
		if (el.isJsonArray()) {// 数组
			JsonArray jsonArray = el.getAsJsonArray();
			// 遍历JsonArray对象
			JsonOrderRateItemVo JsonOrderRateItemVo = null;
			Iterator<JsonElement> it = jsonArray.iterator();
			while (it.hasNext()) {
				JsonElement e = it.next();
				// JsonElement转换为JavaBean对象
				JsonOrderRateItemVo = gson.fromJson(e, JsonOrderRateItemVo.class);
				Short rateType = JsonOrderRateItemVo.getRateType();
				Short rateValue = JsonOrderRateItemVo.getRateValue();
				orderRates.setRateType(rateType);
				orderRates.setRateValue(rateValue);
				orderRateService.insertByOrderRates(orderRates);
			}
		} else {
			return false;
		}

		return true;
	}

	/*
	 * 6.深度养护订单开始服务后 24小时，订单状态变为 已完成
	 * 
	 * 没有 已完成 状态，暂时 改为 已评价，，但不用生成评价记录
	 */
	@Override
	public void amOrderStatusOverOneDay() {

		
		OrderSearchVo searchVo = new OrderSearchVo();
		
		searchVo.setOrderType(Constants.ORDER_TYPE_2);
		searchVo.setOrderStatus(Constants.ORDER_STATUS_5);

		Long now = TimeStampUtil.getNowMin();
		
		searchVo.setEndUpdateTime(now - 24*3600);
		
		List<Orders> list = orderService.selectBySearchVo(searchVo);
		
		for (Orders orders : list) {
			orders.setOrderStatus(Constants.ORDER_STATUS_7);
			orders.setUpdateTime(TimeStampUtil.getNowSecond());
			orderService.updateByPrimaryKeySelective(orders);
		}

	}
}
