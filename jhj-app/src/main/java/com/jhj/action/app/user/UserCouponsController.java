package com.jhj.action.app.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.jhj.vo.user.UserCouponVo;
import com.meijia.utils.DateUtil;
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
			for (Iterator iterator = listUserCouponVo.iterator(); iterator.hasNext();) {
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

}
