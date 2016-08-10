package com.jhj.action.app.user;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.tags.UserRefTags;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.Users;
import com.jhj.service.dict.CardTypeService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.tags.UserRefTagsService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.OrderSearchVo;
import com.jhj.vo.user.UserAppVo;
import com.jhj.vo.user.UserEditViewVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/user")
public class UserController extends BaseController {

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private UserAddrsService userAddrsService;

	@Autowired
	private UserRefTagsService userRefTagsService;

	@Autowired
	private UserRefAmService userRefAmService;

	@Autowired
	private OrderQueryService orderQueryService;
	
	@Autowired
	private OrderDispatchsService orderDispatchsService;
	
	@Autowired
	private CardTypeService cardTypeService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "get_user_list", method = RequestMethod.GET)
	public AppResultData<Object> Sec(
			HttpServletRequest request,
			@RequestParam("am_id") Long amId,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		//根据派工找出相应的用户
		
		
		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setAmId(amId);
		
		List<HashMap> list = orderDispatchsService.getUserIdsByListPage(searchVo, page, Constants.PAGE_MAX_NUMBER);
				
		List<HashMap> resultList = new ArrayList<HashMap>();
		
		if (list.isEmpty()) {
			result.setData(resultList);
			return result;
		}
		
		for (HashMap item : list) {

			Long userId = Long.valueOf(item.get("userId").toString());
			Users user = usersService.getUserById(userId);
			
			String serviceAddr = "";
			UserAddrs userAddr = userAddrsService.selectByDefaultAddr(userId);
			if (userAddr != null) {
				serviceAddr = userAddr.getName() + userAddr.getAddr();
			}
			
			HashMap vo = new HashMap();
			vo.put("staff_id", amId.toString());
			vo.put("user_id", userId.toString());
			vo.put("mobile", user.getMobile());
			vo.put("service_times", item.get("total").toString());
			vo.put("service_addr", serviceAddr);
			
			resultList.add(vo);
		}

		result.setData(resultList);
		return result;
	}

	/**
	 * 客户列表详情
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "get_user_list_detail", method = RequestMethod.GET)
	public AppResultData<Object> getAmOrderDetail(
			@RequestParam("user_id") Long userId) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		List<Orders> orders = ordersService.selectByUserIdList(userId);
		if (orders.isEmpty()) {
			List<UserAddrs> userAddrsList = userAddrsService
					.selectByUserId(userId);
			if (userAddrsList.isEmpty()) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.USER_ADDR_NOT_EXIST_MG);
				return result;
			}
			UserEditViewVo vo = usersService.getUserAddrDetail(userAddrsList,
					userId);
			result.setData(vo);
			return result;
		}

		List<Long> userIds = new ArrayList<Long>();
		for (Orders item : orders) {
			userIds.add(item.getId());
		}
		Collections.sort(userIds);

		Orders order = ordersService.selectByPrimaryKey(userIds.get(0));

		UserEditViewVo vo = usersService.getUserDetail(order.getOrderNo(),
				userId);

		result.setData(vo);
		return result;
	}

	/**
	 * 客户修改页面详情显示
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "get_user_edit_detail", method = RequestMethod.GET)
	public AppResultData<Object> getUserOrderDetail(
			@RequestParam("user_id") Long userId) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		List<Orders> orders = ordersService.selectByUserIdList(userId);
		if (orders.isEmpty()) {
			List<UserAddrs> userAddrsList = userAddrsService
					.selectByUserId(userId);
			if (userAddrsList.isEmpty()) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.USER_ADDR_NOT_EXIST_MG);
				return result;
			}
			/*UserEditViewVo vo = usersService.getUserAddrDetail(userAddrsList,
					userId);*/
			UserEditViewVo vo = usersService.getUserAddrEditDetail(userAddrsList,
					userId);
			result.setData(vo);
			return result;
		}

		List<Long> userIds = new ArrayList<Long>();
		for (Orders item : orders) {
			userIds.add(item.getId());
		}
		Collections.sort(userIds);

		Orders order = ordersService.selectByPrimaryKey(userIds.get(0));

		UserEditViewVo vo = usersService.getUserEditViewDetail(
				order.getOrderNo(), userId);

		result.setData(vo);
		return result;
	}

	/**
	 * 客户信息修改
	 * 
	 * @param userId
	 * @param name
	 * @param mobile
	 * @param sex
	 * @param remarks
	 * @return
	 */
	@RequestMapping(value = "post_user_edit", method = RequestMethod.POST)
	public AppResultData<Object> savaEditUser(
			@RequestParam("user_id") Long userId,
			/*@RequestParam("name") String name,
			@RequestParam("mobile") String mobile,*/
			@RequestParam("sex") String sex,
			@RequestParam("remarks") String remarks,
			@RequestParam(value = "tag_ids", required = false, defaultValue = "") String tagIds) {
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		Users users = usersService.selectByUsersId(userId);

		if (users != null) {

		//	users.setName(name);
			users.setSex(sex);

			// 备注进行urldecode;
			try {
				remarks = URLDecoder.decode(remarks, Constants.URL_ENCODE);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.ERROR_100_MSG);
				return result;
			}
			users.setRemarks(remarks);
			usersService.updateByPrimaryKeySelective(users);
		}
		userRefTagsService.deleByUserId(userId);
		if (!StringUtil.isEmpty(tagIds)) {

			String[] tagIdsAry = StringUtil.convertStrToArray(tagIds);
			
			for (int i = 0; i < tagIdsAry.length; i++) {
				UserRefTags userRefTags = new UserRefTags();

				userRefTags.setUserId(users.getId());
				userRefTags.setAddTime(TimeStampUtil.getNow() / 1000);
				
				if (StringUtil.isEmpty(tagIdsAry[i])) {
					continue;
				} else {
					userRefTags.setTagId(Long.valueOf(tagIdsAry[i]));
					
					userRefTagsService.insertByUserRefTags(userRefTags);

				}
			}
		} 
		UserEditViewVo vo = new UserEditViewVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(users, vo);
		vo.setAddrName(tagIds);
		result.setData(vo);
		return result;
	}

	// 6. 账号信息
	/**
	 * mobile:手机号 rest_money 余额 score会员积分
	 */
	@RequestMapping(value = "get_userinfo", method = RequestMethod.GET)
	public AppResultData<Object> getUserInfo(
			@RequestParam("user_id") Long userId) {

		AppResultData<Object> resultFail = new AppResultData<Object>(
				Constants.ERROR_999, ConstantMsg.USER_NOT_EXIST_MG, "");

		UserAppVo vo = usersService.changeToUserAppVo(userId);

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, vo);
		return result;
	}
	
	
	/**
	 * 该方法为用户充值后，更新用户的rest_money
	 * 
	 * */
	@RequestMapping(value = "getUserRestMoneyInfo", method = RequestMethod.GET)
	public AppResultData<Object> getUserRestMoneyInfo(
			@RequestParam("user_id") Long userId,
			@RequestParam("pay_card_id") Long cardId) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.ERROR_999, ConstantMsg.USER_NOT_EXIST_MG, "");

		Users user=usersService.selectByUsersId(userId);
		DictCardType card = cardTypeService.selectByPrimaryKey(cardId);
		BigDecimal restMoney=user.getRestMoney();
		System.out.println("restMoney="+restMoney+"card_value="+card.getCardValue()+"card_pay"+card.getCardPay());
		restMoney=restMoney.add(card.getCardPay()).add(card.getSendMoney());
		System.out.println("cardId="+cardId);
		System.out.println("restMoney="+restMoney+"getCardPay="+card.getCardPay()+"getSendMoney="+card.getSendMoney());
		user.setRestMoney(restMoney);
		usersService.updateByPrimaryKeySelective(user);

		result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, user);
		return result;
	}
}
