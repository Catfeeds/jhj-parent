package com.jhj.service.impl.job;

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
import com.jhj.po.model.order.OrderRates;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.job.OrderCrondService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderRatesService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.JsonOrderRateItemVo;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月24日下午4:17:01
 * @Description: 
 *		定时任务
 */
@Service
public class OrderCrondServiceImpl implements OrderCrondService {
	
	@Autowired
	private OrdersMapper orderMapper;
	@Autowired
	private OrderDispatchsService orderDisService;
	@Autowired
	private UsersService userService;
	@Autowired
	private OrderRatesService orderRateService;
	@Autowired
	private OrgStaffsService orgStaffService;
	
	/*
	 *  1.钟点工--  服务开始前两小时  通知 -- 接收方：  用户
	 */
	@Override
	public void noticeBeforeService() {
		
		List<Orders> list = orderMapper.selectBeforeService();
		
		for (Orders order : list) {
			
			// xx月 xx 日 xx:xx~ xx:xx
			String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
			String endTimeStr = TimeStampUtil.timeStampToDateStr( (order.getServiceDate() + order.getServiceHour() * 3600) * 1000, "HH:mm");
			String timeStr = beginTimeStr + "-" + endTimeStr;
			
			// 家政师 xx   　在开始前２小时之内换人???
			
			Long orderId = order.getId();
			OrderDispatchs orderDispatchs = orderDisService.selectByOrderId(orderId);
			
			String staffName = orderDispatchs.getStaffName();
			
			// 用户手机号
			Long userId = order.getUserId();
			
			Users user = userService.selectByUsersId(userId);
			
			String[] tempStr = new String[]{ timeStr,staffName};
			
			SmsUtil.SendSms(user.getMobile(), "29156", tempStr);
			
//			SmsUtil.SendSms("13521193653", "29156", tempStr);
		}
		
	}
	
	/*
	 * 2.服务开始时间内，将订单状态改为服务中 
	 */
	@Override
	public void updateDuringService(){
		List<Orders> list = orderMapper.selectDuringService();
		
		for (Orders orders : list) {
			orders.setOrderStatus(Constants.ORDER_STATUS_5);
			orderMapper.updateByPrimaryKeySelective(orders);
		}
	}
	
	/*
	 * 3.服务结束时间后，如果订单状态还是ORDER_STATUS_5 ，则改为待评价状态
	 */
	@Override
	public void updateAfterService(){
		List<Orders> list = orderMapper.selectAfterService();
		
		for (Orders orders : list) {
			orders.setOrderStatus(Constants.ORDER_STATUS_6);
			orderMapper.updateByPrimaryKeySelective(orders);
		}
	}
	
	/*
	 * 4.在钟点工状态为待支付状态 ORDER_STATUS_3，超过1个小时未支付的订单，将订单状态改为已关闭.
	 */
	@Override
	public void updateOverTimeNotPay(){
		List<Orders> list = orderMapper.selectOverTimeNotPay();
		for (Orders orders : list) {
			orders.setOrderStatus(Constants.ORDER_STATUS_9);
			orderMapper.updateByPrimaryKeySelective(orders);
		}
	}
	
	/*
	 * 5.订单已结束超过七天，默认评价，并且为好评
	 */
	@Override
	public void setOrderRateOverServenDay(){
		List<Orders> list = orderMapper.selectOverSevenDay();
		
		String rateDatas = "[{'rateType':0, 'rateValue': 0},"
						+ "{'rateType':1, 'rateValue': 0},"
						+ "{'rateType':2, 'rateValue': 0},"
						+ "{'rateType':3, 'rateValue': 0},"
						+ "{'rateType':4, 'rateValue': 0}]"; // 默认全 好评
		
		for (Orders orders : list) {
			boolean aaa = generalDefaultOrderRates(orders, rateDatas);
			
			//在  order_rates表生成记录 之后，修改 订单状态为 已评价
			if(aaa){
				orders.setOrderStatus(Constants.ORDER_STATUS_7);
				
				orderMapper.updateByPrimaryKeySelective(orders);
			}
		}
	}
	
	
	
	 //为 某条订单  在评价 表中  生成  全好评 的  记录
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
				JsonOrderRateItemVo = gson.fromJson(e,JsonOrderRateItemVo.class);
				Short rateType = JsonOrderRateItemVo.getRateType();
                Short rateValue = JsonOrderRateItemVo.getRateValue();
                orderRates.setRateType(rateType);
                orderRates.setRateValue(rateValue);
                orderRateService.insertByOrderRates(orderRates);
			}
		}else{
			return  false;
		}
		
		return true;
	}
	
	
	
	/*
	 * 6.助理订单支付后 24小时，订单状态变为 已完成
	 * 		  
	 * 		没有 已完成 状态，暂时 改为  已评价，，但不用生成评价记录
	 * 
	 * 
	 */
	@Override
	public void amOrderStatusOverOneDay() {
		
		//已支付状态的 助理单，24小时后。
		List<Orders> list = orderMapper.selectAmOrderOverOneDay();
		
		for (Orders orders : list) {
			orders.setOrderStatus(Constants.ORDER_STATUS_7);
			
			orderMapper.updateByPrimaryKeySelective(orders);
		}
		
		
	}

	
	/*
	 * 超出 服务时间 的 提醒类 订单，修改为 已完成, 同时给用户发短信
	 */
	@Override
	public void remindOverServiceDate() {
		
		List<Orders> list = orderMapper.selectRemindOverServiceDate();
		
		for (Orders orders : list) {
			orders.setOrderStatus(Constants.ORDER_STATUS_11);
			
			orderMapper.updateByPrimaryKeySelective(orders);
			
			Long userId = orders.getUserId();
			
			Users users = userService.selectByUsersId(userId);
			
			String[] tempStr = new String[]{""};
			
			SmsUtil.SendSms(users.getMobile(), "39627", tempStr);
			
		}
		
		
	}

	/*
	 *  服务前 30分钟 提醒 助理 ，到时间 给 用户 打电话
	 */
	@Override
	public void remindAmToDoService() {
		
		List<Orders> list = orderMapper.selectRemindBeforeHalfHour();
		
		for (Orders order : list) {
			
//			Long userId = order.getUserId();
//			Users user = userService.selectByUsersId(userId);
			
			Long amId = order.getAmId();
			
			OrgStaffs orgStaffs = orgStaffService.selectByPrimaryKey(amId);
			
			String[] tempStr = new String[]{""};
			
			SmsUtil.SendSms(orgStaffs.getMobile(), "37805", tempStr);
		}
		
	}
	
	/*
	 * 话费充值订单，超过1小时 未完成 微信支付，由 13(充值中) 变为 16(已取消)
	 */
	@Override
	public void phoneRechargeOverOneHour() {
		
		List<Orders> list = orderMapper.selectPhoneOrderOverOneHour();
		for (Orders orders : list) {
			
			orders.setUpdateTime(TimeStampUtil.getNowSecond());
			
			orders.setOrderStatus(Constants.ORDER_STATUS_16);
			
			orderMapper.updateByPrimaryKey(orders);
			
		}
		
	}
	
	
	/*
	 * 超过 3小时  , 未 被确认的 助理/深度保洁订单， 由 确认状态 改为  已关闭状态
	 */
	
	@Override
	public void changeToClose() {
		
		List<Orders> list = orderMapper.selectChangeToClose();
		
		for (Orders orders : list) {
			
			orders.setUpdateTime(TimeStampUtil.getNowSecond());
			
			orders.setOrderStatus(Constants.ORDER_STATUS_9);
			
			orderMapper.updateByPrimaryKeySelective(orders);
			
		}
		
		
	}
	
}
