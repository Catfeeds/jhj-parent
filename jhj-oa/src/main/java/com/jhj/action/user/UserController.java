package com.jhj.action.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.UserSmsToken;
import com.jhj.po.model.user.Users;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UserSmsTokenService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.UserDetailSearchVo;
import com.jhj.vo.UserSearchVo;
import com.jhj.vo.UsersSmsTokenVo;
import com.jhj.vo.user.UserChargeVo;
import com.jhj.vo.user.UserCouponsVo;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	@Autowired
	private UsersService usersService;

	@Autowired
	private UserCouponsService usersCounpsService;

	@Autowired
	private UserSmsTokenService userSmsTokenService;

	@Autowired
	private UserDetailPayService userDetailPayService;

	@Autowired
	private OrderCardsService orderCardsService;

	@AuthPassport
	@RequestMapping(value = "/update_name", method = { RequestMethod.POST })
	public AppResultData<Object> detail(@RequestParam("pk") Long userId,
			@RequestParam("value") String userName) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		if (userId == null || userName == null) {
			return result;
		}
		Users user = usersService.selectByUsersId(userId);
		if (user == null) {
			return result;
		}

		user.setName(userName);
		user.setUpdateTime(TimeStampUtil.getNow() / 1000);

		usersService.updateByPrimaryKeySelective(user);
		result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, user);
		return result;
	}

	@AuthPassport
	@RequestMapping(value = "/user-list", method = { RequestMethod.GET })
	public String userList(HttpServletRequest request, Model model,
			UserSearchVo searchVo) {
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());

		model.addAttribute("searchModel", searchVo);
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		PageInfo result = usersService.searchVoListPage(searchVo, pageNo,
				pageSize);
		model.addAttribute("userList", result);

		return "user/userList";
	}

	/**
	 * 用户查看其优惠券列表
	 * 
	 * @param model
	 * @param userId
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/coupons-list", method = { RequestMethod.GET })
	public String couponsList(Model model, @RequestParam("user_id") Long userId) {

		List<UserCoupons> userCouponsList = usersCounpsService
				.selectAllByUserId(userId);

		List<UserCouponsVo> userCouponsVos = new ArrayList<UserCouponsVo>();
		if (userCouponsList != null) {
			for (Iterator iterator = userCouponsList.iterator(); iterator
					.hasNext();) {
				UserCoupons userCoupons = (UserCoupons) iterator.next();
				UserCouponsVo vo = usersCounpsService
						.getUsersCounps(userCoupons);
				userCouponsVos.add(vo);
			}
		}
		model.addAttribute("userCouponsVos", userCouponsVos);

		return "user/userCouponsList";
	}

	@AuthPassport
	@RequestMapping(value = "/user-pay-detail", method = { RequestMethod.GET })
	public String payDetailList(HttpServletRequest request, Model model,
			UserDetailSearchVo searchVo) {
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());

		model.addAttribute("searchModel", searchVo);
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);

		PageInfo result = userDetailPayService.searchVoListPage(searchVo,
				pageNo, pageSize);
		model.addAttribute("userPayDetailList", result);

		return "user/userDetailPayList";
	}

	@RequestMapping(value = "/token-list", method = { RequestMethod.GET })
	public String userTokenList(HttpServletRequest request, Model model,
			UsersSmsTokenVo usersSmsTokenVo) {

		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());

		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);

		// 分页
		PageHelper.startPage(pageNo, pageSize);

		List<UserSmsToken> lists = userSmsTokenService.selectByListPage(
				usersSmsTokenVo, pageNo, pageSize);

		PageInfo result = new PageInfo(lists);
		model.addAttribute("usersSmsTokenVo", usersSmsTokenVo);
		model.addAttribute("userSmsTokenModel", result);

		return "user/userTokenList";
	}

	/**
	 * 跳转到>1w充值页面
	 */
	@AuthPassport
	@RequestMapping(value = "/charge-form", method = { RequestMethod.GET })
	public String toChargeForm(Model model, @RequestParam("user_id") Long userId) {

		UserChargeVo userChargeVo = new UserChargeVo();
		// 设置用户Id;充值方式
		userChargeVo.setUserId(userId);
		userChargeVo.setChargeWay((short) 0);// 0=固定充值
		model.addAttribute("userChargeVo", userChargeVo);
		model.addAttribute("selectDataSource",
				usersService.selectDictCardDataSource());
		model.addAttribute("chargeWayDataSource",
				usersService.getChargeWayDataSource());

		return "user/chargeForm";
	}

	/**
	 * >1w充值操作 会员充值，流程如下 1)更新对应用户的restMoney 2)记录用户充值卡消费明细 3)记录用户交易消费明细
	 * 4)赠送相应的优惠券到对应的用户
	 * 
	 * @param dictCoupons
	 * @param model
	 * @param id
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/charge-form", method = { RequestMethod.POST })
	public String doChargeForm(
			@ModelAttribute("userChargeVo") UserChargeVo userChargeVo,
			Model model, @RequestParam("userId") Long userId) {

		Users user = usersService.selectByUsersId(userId);

		Short chargeWay = userChargeVo.getChargeWay();
		// 0=固定充值，1=任意充值
		if (chargeWay == 1) {
			BigDecimal restMoney = user.getRestMoney();
			BigDecimal chargeMoney = userChargeVo.getChargeMoney();
			user.setRestMoney(restMoney.add(chargeMoney));
			int result = usersService.updateByPrimaryKeySelective(user);
			if (result > 0) {
				// 2、记录用户充值卡消费明细
				OrderCards orderCards = orderCardsService.initOrderCards(user,
						userChargeVo, Constants.PAY_TYPE_4);
				orderCards.setOrderStatus(Constants.PAY_STATUS_1);
				orderCardsService.insertSelective(orderCards);
				// 3、记录用户交易明细表
				userDetailPayService.addUserDetailPayForOrderCard(user,
						orderCards, "SUCCESS", "", "");
				// 对于任意充值，无法赠送优惠券
			}
		} else {
			DictCardType dictCardType = usersService
					.selectDictCardTypeById(userChargeVo.getDictCardId());
			// 1、充值更新会员余额
			BigDecimal restMoney = user.getRestMoney();
			BigDecimal cardValue = dictCardType.getCardValue();
			user.setRestMoney(restMoney.add(cardValue));
			int result = usersService.updateByPrimaryKeySelective(user);
			if (result > 0) {
				// 2、记录用户充值卡消费明细
				OrderCards orderCards = orderCardsService.initOrderCards(user,
						dictCardType.getId(), dictCardType,
						Constants.PAY_TYPE_4);
				orderCards.setOrderStatus(Constants.PAY_STATUS_1);
				orderCardsService.insertSelective(orderCards);
				// 3、记录用户交易明细表
				userDetailPayService.addUserDetailPayForOrderCard(user,
						orderCards, "SUCCESS", "", "");
				// 4、赠送相应的优惠劵到对应的用户账户
				orderCardsService.sendCoupons(user.getId(), orderCards.getId());
			}
		}
		return "redirect:user-list";
	}

}
