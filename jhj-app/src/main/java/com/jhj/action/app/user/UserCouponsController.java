package com.jhj.action.app.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.dict.CouponSearchVo;
import com.jhj.vo.user.UserCouponVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

/**
 * @description：
 * @author： kerryg
 * @date:2015年7月24日 
 */
@Controller
@RequestMapping(value="/app/user")
public class UserCouponsController extends BaseController {
	
		@Autowired
		private UserCouponsService userCouponsService;
		
		@Autowired
		private UsersService userService;		
		
		@Autowired
		private DictCouponsService dictCouponsService;
		
		/**
		 * 优惠券列表接口
		 * @param userId
		 * @return
		 */
		@RequestMapping(value = "get_coupon", method = RequestMethod.GET)
		public AppResultData<List<UserCouponVo>> getCoupon(
				@RequestParam("user_id") Long userId) {
			
			AppResultData<List<UserCouponVo>> result = new AppResultData<List<UserCouponVo>>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG,new ArrayList<UserCouponVo>());
			
			Users u = userService.selectByPrimaryKey(userId);
			
			// 判断是否为注册用户，非注册用户返回 999
			if (u == null) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
				return result;
			}			
			
			List<UserCoupons> list = userCouponsService.selectByUserId(userId);
			List<UserCouponVo> listNew = new ArrayList<UserCouponVo>();
			//如果用户未使用的 优惠券 ，数量不为0
			if(list.isEmpty()) return result;
			
			List<UserCouponVo> listUserCouponVo = userCouponsService.changeToUserCouponVos(list);
			//过滤优惠券是否失效
			for (Iterator<UserCouponVo> iterator = listUserCouponVo.iterator(); iterator.hasNext();) {
				UserCouponVo userCouponVo = (UserCouponVo) iterator.next();
				if (userCouponVo.getIsUsed().equals((short)1) ||
					userCouponVo.getToDate().before(DateUtil.getNowOfDate())) {
					continue;
				}
				listNew.add(userCouponVo);
			}
			
			result.setData(listNew);
			return result;
		}
		
		/**
		 * 兑换优惠码接口
		 * @param request
		 * @param userId
		 * @param cardPasswd
		 * @return
		 */
		@RequestMapping(value = "post_coupon", method = RequestMethod.POST)
		public AppResultData<Object> login(
				HttpServletRequest request,
				@RequestParam("user_id") Long userId,
				@RequestParam("card_passwd") String cardPasswd) {
			
			AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());
			
			Users u = userService.selectByPrimaryKey(userId);
			
			// 判断是否为注册用户，非注册用户返回 999
			if (u == null) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
				return result;
			}			
			
			DictCoupons dictCoupons = dictCouponsService.selectByCardPasswd(cardPasswd);
			
			if(dictCoupons == null) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.COUPON_INVALID_MSG);
				return result;
			}
			
			AppResultData<Object> validateResult = null;
			
			//验证优惠劵有效方法1. 优惠劵是否已经无效，结束时间超过当前时间
			validateResult = userCouponsService.validateCouponForPost(u, dictCoupons);
			if (validateResult.getStatus() == Constants.ERROR_999) return validateResult;
			
			//增加成功兑换优惠券记录
			UserCoupons userCouponsItem  = userCouponsService.initUserCoupons();
			userCouponsItem.setCouponId(dictCoupons.getId());
			userCouponsItem.setOrderNo("");
			userCouponsItem.setServiceType(dictCoupons.getServiceType());
			userCouponsItem.setUserId(userId);
			userCouponsItem.setFromDate(dictCoupons.getFromDate());
			userCouponsItem.setToDate(dictCoupons.getToDate());
			userCouponsItem.setValue(dictCoupons.getValue());
			
			userCouponsService.insertSelective(userCouponsItem);
			
			
			UserCouponVo userCouponVo = userCouponsService.changeToUserCouponVo(userCouponsItem);
			
			result.setData(userCouponVo);
			
			return result;
		}
		
		@RequestMapping(value = "get_validate_coupons",method = RequestMethod.GET)
		public AppResultData<Object> getValidateCoupons(
				@RequestParam("user_id") Long userId,
				@RequestParam(value="service_date") Long serviceDate,
				@RequestParam(value="order_money",required=false) Float orderMoney,
				@RequestParam("service_type") String serviceTypeId){

			AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
			Users u = userService.selectByPrimaryKey(userId);

			// 判断是否为注册用户，非注册用户返回 999
			if (u == null) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
				return result;
			}

			UserCoupons userCoupons = new UserCoupons();
			userCoupons.setIsUsed((short)0);
			userCoupons.setUserId(userId);
//			userCoupons.setServiceType(serviceTypeId);
			List<UserCoupons> list = userCouponsService.selectByUserCoupons(userCoupons);
			
			//有效的优惠劵列表
			List<UserCouponVo> listNew = new ArrayList<UserCouponVo>();
			
			Map<String, Object> resultData = new HashMap<String, Object>();
			resultData.put("total", 0);
			resultData.put("coupon", "");
			if(list.isEmpty()) {
				result.setData(resultData);
				return result;
			}

			List<UserCouponVo> listUserCouponVo = userCouponsService.changeToUserCouponVos(list);
			
			BigDecimal moeney=new BigDecimal(0);
			if(orderMoney!=null){
				moeney=new BigDecimal(orderMoney);
			}
			
			for (UserCouponVo userCouponVo : listUserCouponVo) {	
				Date fromDate = userCouponVo.getFromDate();
				String fromDateStr = DateUtil.formatDate(fromDate);
				Long startTime = TimeStampUtil.getMillisOfDayFull(fromDateStr + " 00:00:00");
				startTime = startTime / 1000;
				
				Date toDate = userCouponVo.getToDate();
				String toDateStr = DateUtil.formatDate(toDate);
				Long endTime = TimeStampUtil.getMillisOfDayFull(toDateStr + " 23:59:59");
				endTime = endTime / 1000;
				
				//1. 判断优惠券的有效期
				if (serviceDate < startTime || serviceDate > endTime) {
					listNew.add(userCouponVo);
					continue;
				}

				//2. 判断服务类型是否正确
				if (!userCouponVo.getServiceType().equals("0") &&
					!userCouponVo.getServiceType().toString().equals(serviceTypeId)) {
					listNew.add(userCouponVo);
					continue;
				}
				
				BigDecimal maxValue = new BigDecimal(0);
				if (userCouponVo.getMaxValue() != null) maxValue = userCouponVo.getMaxValue();
				if (!maxValue.equals(BigDecimal.ZERO)) {
					if (moeney.compareTo(maxValue) < 0) {
						listNew.add(userCouponVo);
						continue;
					}
				}
			}
			
			listUserCouponVo.removeAll(listNew);
			if (listUserCouponVo.isEmpty() || listUserCouponVo.size() == 0)  {
				result.setData(resultData);
				return result;
			}
			
			
			
			if (listUserCouponVo.size() == 1) {
				resultData.put("total", listUserCouponVo.size());
				resultData.put("coupon", listUserCouponVo.get(0));
				
				result.setData(resultData);
				return result;
			}
			//找出最符合要求的优惠劵
			//1. 过期时间最早的优惠劵
			//2. 过期时间相同的优惠劵，金额最大的为优先
			
			//1. 找出时间最早的优惠劵
			Collections.sort(listUserCouponVo, new Comparator<UserCouponVo>() {
			    @Override
				public int compare(UserCouponVo s1, UserCouponVo s2) {
			    	Long d1 = TimeStampUtil.getMillisOfDate(s1.getToDate()) /1000;
			    	Long d2 = TimeStampUtil.getMillisOfDate(s2.getToDate()) / 1000;
			    	if (d1 > d2) return 1;
			    	if (d1 < d2) return -1;
			    	if (d1 == d2) return 0;
			        return 0;
			    }
			});
			
			UserCouponVo firstVo = listUserCouponVo.get(0);
			Date fistToDate = firstVo.getToDate();
			List<UserCouponVo> matchUserCoupons = new ArrayList<UserCouponVo>();
			for (UserCouponVo userCouponVo : listUserCouponVo) {	
				if (userCouponVo.getToDate().equals(fistToDate)) {
					matchUserCoupons.add(userCouponVo);
				}
			}
			
			//1. 找出时间最早的优惠劵
			Collections.sort(matchUserCoupons, new Comparator<UserCouponVo>() {
			    @Override
				public int compare(UserCouponVo s1, UserCouponVo s2) {
			    	return s2.getValue().compareTo(s1.getValue());
			    }
			});
			
			resultData.put("total", matchUserCoupons.size());
			resultData.put("coupon", matchUserCoupons.get(0));
			result.setData(resultData);
			return result;
		}

		/*
		 * 
		 * 领取优惠券活动
		 */
		@RequestMapping(value = "receive_coupon.json", method = RequestMethod.POST)
		public AppResultData<String> receiveCoupon(
				@RequestParam("mobile") String mobile,@RequestParam("coupons_id") String couponsId) {
			
			AppResultData<String> result = new AppResultData<String>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG,"");
			
			Users u = userService.selectByMobile(mobile);
			
			// 如果用户不存在，则存入改用户
			if (u == null) {
				u = userService.initUsers(mobile, (short)1);
				userService.insertSelective(u);
			}			
			
			//判断只有这三种优惠劵可用
			List<Long> validateCouponIds = new ArrayList<Long>();
			validateCouponIds.add(4191L);
			validateCouponIds.add(4170L);
			validateCouponIds.add(4171L);
			validateCouponIds.add(4172L);
			validateCouponIds.add(4173L);
			validateCouponIds.add(4174L);
			
			String[] couponsIdList = couponsId.split(",");
			List<Long> list=new ArrayList<Long>();
			if(couponsIdList.length>0){
				List<String> asList = Arrays.asList(couponsIdList);
				for(String str:asList){
					list.add(Long.valueOf(str));
				}
			}
			
			if (!validateCouponIds.containsAll(list)) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg("无效的优惠劵");
				return result;
			}
			
			CouponSearchVo couponSearchVo=new CouponSearchVo();
			couponSearchVo.setCouponsIdList(list);
			List<DictCoupons> couponsList = dictCouponsService.selectBySearchVo(couponSearchVo);
			
			if (couponsList == null) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg("无效的优惠劵");
				return result;
			}
			
			if(couponsList!=null && couponsList.size()>0){
				UserCoupons uc = new UserCoupons();
				uc.setUserId(u.getId());
				for(int i=0,len=couponsList.size();i<len;i++){
					uc.setCouponId(couponsList.get(i).getId());
					List<UserCoupons> couponList = userCouponsService.selectByUserCoupons(uc);
					if(couponList.isEmpty() ){
						UserCoupons userCoupons = userCouponsService.initUserCoupons(u.getId(), couponsList.get(i));
						if(couponsList.get(i).getId()==4191L){
							String toDateStr = DateUtil.addDay(DateUtil.getNowOfDate(), 7, Calendar.DAY_OF_MONTH, DateUtil.DEFAULT_PATTERN);
							userCoupons.setToDate(DateUtil.parse(toDateStr));
						}
						userCouponsService.insertSelective(userCoupons);
					} else {
						result.setStatus(Constants.ERROR_999);
						result.setMsg("您已经领取过此优惠劵");
						return result;
					}
				}
			}
			
			return result;
		}
		
		@RequestMapping(value = "get_validate_one_coupons.json",method = RequestMethod.POST)
		public AppResultData<Object> getValidateOneCoupons(
				@RequestParam("user_coupon_id") Long user_coupon_id,
				@RequestParam("user_id") Long userId,
				@RequestParam(value="service_date") Long serviceDate,
				@RequestParam(value="order_money",required=false) Float orderMoney,
				@RequestParam("service_type") String serviceTypeId){

			AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

			UserCoupons userCoupon = userCouponsService.selectByPrimaryKey(user_coupon_id);
			DictCoupons dictCoupons = dictCouponsService.selectByPrimaryKey(userCoupon.getCouponId());
			
			BigDecimal moeney=new BigDecimal(0);
			if(orderMoney!=null){
				moeney=new BigDecimal(orderMoney);
			}
			
			Date fromDate = userCoupon.getFromDate();
			String fromDateStr = DateUtil.formatDate(fromDate);
			Long startTime = TimeStampUtil.getMillisOfDayFull(fromDateStr + " 00:00:00");
			startTime = startTime / 1000;
			
			Date toDate = userCoupon.getToDate();
			String toDateStr = DateUtil.formatDate(toDate);
			Long endTime = TimeStampUtil.getMillisOfDayFull(toDateStr + " 23:59:59");
			endTime = endTime / 1000;
			
			//1. 判断优惠券的有效期
			if (serviceDate < startTime || serviceDate > endTime) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.COUPON_IS_INVALID);
			}

			//2. 判断服务类型是否正确
			if (!userCoupon.getServiceType().equals("0") &&
				!userCoupon.getServiceType().toString().equals(serviceTypeId)) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.COUPON_IS_INVALID);
			}
			
			BigDecimal maxValue = new BigDecimal(0);
			if (dictCoupons.getMaxValue() != null) maxValue = dictCoupons.getMaxValue();
			if (!maxValue.equals(BigDecimal.ZERO)) {
				if (moeney.compareTo(maxValue) < 0) {
					result.setStatus(Constants.ERROR_999);
					result.setMsg(ConstantMsg.COUPON_IS_INVALID);
				}
			}
			
			return result;
		}
}
