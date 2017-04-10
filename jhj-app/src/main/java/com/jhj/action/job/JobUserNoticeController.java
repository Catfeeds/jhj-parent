package com.jhj.action.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.UserSmsNotice;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.job.OrderCrondService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserSmsNoticeService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.user.UserSmsNoticeSearchVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.RegexUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 */
@Controller
@RequestMapping(value = "/app/job/user")
public class JobUserNoticeController extends BaseController {
	
	@Autowired
	UsersService userService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@Autowired
	private UserSmsNoticeService userSmsNoticeService;
	
	@Autowired
	private UserCouponsService userCouponService;	
	
	@Autowired
	private DictCouponsService dictCouponService;	

	/*
	 * 发送规则：所有下单用户，在一个月内没有下单使用的自动发送一条短信和一张优惠券
	 * 发送规则：所有下单用户，在两个月内没有下单使用的自动发送一条短信和一张优惠券到用户账户（优惠券有效期1个月）
	 * 发送规则：所有下单用户，在三个月内没有下单使用的自动发送一条短信和一张优惠券到用户账户（优惠券有效期1个月）
	 */

	@RequestMapping(value = "order_notice", method = RequestMethod.GET)
	public AppResultData<Object> beforeServiceNotice(HttpServletRequest request) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		System.out.println("Remote Host: " + request.getRemoteHost());
		String reqHost = request.getRemoteHost();// 如果用的 IPV6 得到的 将是 0:0.。。。。1

		// String reqHost = request.getRemoteAddr();
		// 限定只有localhost能访问
		if (reqHost.equals("localhost") || reqHost.equals("127.0.0.1")) {
						
			Date nowDate = DateUtil.parse(DateUtil.getBeginOfDay());
			String nowDateStr = DateUtil.getToday();
			String oneMonthDayStr = DateUtil.addDay(nowDate, -1, Calendar.MONTH, DateUtil.DEFAULT_PATTERN);
			String twoMonthDayStr = DateUtil.addDay(nowDate, -2, Calendar.MONTH, DateUtil.DEFAULT_PATTERN);
			String threeMonthDayStr = DateUtil.addDay(nowDate, -3, Calendar.MONTH, DateUtil.DEFAULT_PATTERN);
			System.out.println("startTimeForOneMonth = " +oneMonthDayStr);
			System.out.println("startTimeForTwoMonth = " +twoMonthDayStr);
			System.out.println("startTimeForThreeMonth = " +threeMonthDayStr);
			
			Long startAddTime = 0L;
			Long endAddTime = 0L;
			List<Long> userIds = new ArrayList<Long>();
			OrderSearchVo searchVo = new OrderSearchVo();
			
			//1.找出3个月内未下单的用户 > 60天 and < 90 天
			Date threeMonthDay = DateUtil.parse(threeMonthDayStr);
			String endThreeMonthDayStr = DateUtil.addDay(threeMonthDay, -1, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
			System.out.println("3个月内 = " + endThreeMonthDayStr + "---" );
			endAddTime = TimeStampUtil.getMillisOfDayFull(endThreeMonthDayStr + " 23:59:59")/1000;
			searchVo = new OrderSearchVo();
			searchVo.setEndAddTime(endAddTime);
			userIds = orderQueryService.getLastOrder(searchVo);
			if (!userIds.isEmpty()) {
				doSmsNotice(userIds, (short) 3);
			}
			
			//2.找出2个月内未下单的用户 > 60天 and < 90 天
			Date twoMonthDay = DateUtil.parse(twoMonthDayStr);
			String endTwoMonthDayStr = DateUtil.addDay(twoMonthDay, -1, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
			
			System.out.println("2个月内 = " + threeMonthDayStr + "---" + endTwoMonthDayStr);
			startAddTime = TimeStampUtil.getMillisOfDayFull(threeMonthDayStr + " 00:00:00")/1000;
			endAddTime = TimeStampUtil.getMillisOfDayFull(endTwoMonthDayStr + " 23:59:59")/1000;
			searchVo = new OrderSearchVo();
			searchVo.setStartAddTime(startAddTime);
			searchVo.setEndAddTime(endAddTime);
			userIds = orderQueryService.getLastOrder(searchVo);
			if (!userIds.isEmpty()) {
				doSmsNotice(userIds, (short) 2);
			}
			
			//3.找出一个月内未下单的用户  > 30天 and < 60天
			Date oneMonthDay = DateUtil.parse(oneMonthDayStr);
			String endOneMonthDayStr = DateUtil.addDay(oneMonthDay, -1, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
			System.out.println("一个月内 = " + twoMonthDayStr + "---" + endOneMonthDayStr);
			startAddTime = TimeStampUtil.getMillisOfDayFull(twoMonthDayStr + " 00:00:00")/1000;
			endAddTime = TimeStampUtil.getMillisOfDayFull(endOneMonthDayStr + " 23:59:59")/1000;
			
			searchVo.setStartAddTime(startAddTime);
			searchVo.setEndAddTime(endAddTime);
			userIds = orderQueryService.getLastOrder(searchVo);
			
			if (!userIds.isEmpty()) {
				doSmsNotice(userIds, (short) 1);
			}
			
			
			
			
			
		}

		return result;
	}
	
	
	private boolean doSmsNotice(List<Long> userIds, Short lastMonth) {
		if (userIds.isEmpty()) return true;
		
		UserSmsNoticeSearchVo searchVo = new UserSmsNoticeSearchVo();
		Date nowDate = DateUtil.parse(DateUtil.getBeginOfDay());
		String oneMonthDayStr = DateUtil.addDay(nowDate, -1, Calendar.MONTH, DateUtil.DEFAULT_PATTERN);
		
		Long startTime = TimeStampUtil.getMillisOfDayFull(oneMonthDayStr + " 00:00:00") / 1000; 
		Long endTime = TimeStampUtil.getNowSecond();
		String[] content = new String[] { "" };
		for (Long userId : userIds) {
			//先检测最近一个月是否已经推送过，如果有则跳过
			searchVo = new UserSmsNoticeSearchVo();
			searchVo.setUserId(userId);
			searchVo.setStartTime(startTime);
			searchVo.setEndTime(endTime);
			List<UserSmsNotice> list = userSmsNoticeService.selectBySearchVo(searchVo);
			
			if (!list.isEmpty()) continue;
			//再检测是否已经推送过3个月，如果有则跳过
			searchVo = new UserSmsNoticeSearchVo();
			searchVo.setUserId(userId);
			Integer total = userSmsNoticeService.totalBySearchVo(searchVo);
			
			if (total > 3) continue;
			
			Users u = userService.selectByPrimaryKey(userId);
			if (u == null) continue;
			String mobile = u.getMobile();
			if (StringUtil.isEmpty(mobile)) continue;
			if (!RegexUtil.isMobile(mobile)) continue;
			
			
			String smsTemplateId = getNoticeSmsTemplate(lastMonth);
			UserSmsNotice record = userSmsNoticeService.initPo();
			
			record.setUserId(userId);
			record.setMobile(mobile);
			record.setLastMonth(lastMonth);
			record.setSmsTemplateId(smsTemplateId);
			record.setRemarks(lastMonth + "个月内没有下单自动发送短信");
			userSmsNoticeService.insert(record);
			
			
			HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile, smsTemplateId, content);
			
			if (sendSmsResult.get(Constants.SMS_SUCCESS_CODE) != null && 
				sendSmsResult.get("statusCode").equals(Constants.SMS_SUCCESS_CODE)) {
				record.setIsSuceess( Constants.SMS_SUCCESS);
			} else {
				record.setIsSuceess( Constants.SMS_FAIL);
			}

			record.setSmsReturn(sendSmsResult.get(Constants.SMS_STATUS_CODE));
			userSmsNoticeService.updateByPrimaryKeySelective(record);
			
			//赠送 10元金牌保洁  4192
			doSendCoupon(4192L, userId);
			
			if (lastMonth.equals((short)2) || lastMonth.equals((short)3)) {
				doSendCoupon(4194L, userId);
			}
			
			if (lastMonth.equals((short)3)) {
				doSendCoupon(4200L, userId);
			}
		}
		
		return true;
	}
		
	private boolean doSendCoupon(Long couponId, Long userId) {
		DictCoupons dictCoupon = dictCouponService.selectByPrimaryKey(couponId);
		if (dictCoupon != null) {
		
			UserCoupons record = userCouponService.initUserCoupons();
			record.setUserId(userId);
			record.setCouponId(dictCoupon.getId());
			record.setValue(dictCoupon.getValue());
			record.setServiceType(dictCoupon.getServiceType());
			if(dictCoupon.getRangMonth() == 0){
				record.setFromDate(dictCoupon.getFromDate());
				record.setToDate(dictCoupon.getToDate());
			}
			if(dictCoupon.getRangMonth() > 0){
				Date fromDate = DateUtil.getNowOfDate();
				record.setFromDate(fromDate);
				String toDateStr = DateUtil.addDay(fromDate, dictCoupon.getRangMonth().intValue(), Calendar.MONTH, DateUtil.DEFAULT_PATTERN);
				Date toDate = DateUtil.parse(toDateStr);
				record.setToDate(toDate);
			}
			userCouponService.insert(record);
		}
		
		return true;
	}
	
	public String getNoticeSmsTemplate(Short lastMonth) {
		String tId = "";
		switch (lastMonth) {
			case 1: 
				tId = "165610";
				break;
			case 2:
				tId = "165611";
				break;
			case 3:
				tId = "165612";
				break;
			default:
				tId = "165610";
				break;
		}
		
		return tId;
	}
}
