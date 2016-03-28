package com.jhj.action.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.jhj.oa.auth.AccountAuth;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.user.FinanceRecharge;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.UserSmsToken;
import com.jhj.po.model.user.Users;
import com.jhj.service.dict.CardTypeService;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.users.FinanceRechargeService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UserSmsTokenService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.UserDetailSearchVo;
import com.jhj.vo.UserSearchVo;
import com.jhj.vo.UsersSmsTokenVo;
import com.jhj.vo.bs.NewStaffListVo;
import com.jhj.vo.finance.FinanceSearchVo;
import com.jhj.vo.user.FinanceRechargeVo;
import com.jhj.vo.user.UserChargeVo;
import com.jhj.vo.user.UserCouponsVo;
import com.meijia.utils.MathBigDeciamlUtil;
import com.meijia.utils.RandomUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.StringUtil;
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
	
	@Autowired
	private CardTypeService cardTypeService;

	@Autowired
	private FinanceRechargeService financeService;
	
	
	
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
		
		Users users = usersService.selectByUsersId(userId);
		
		userChargeVo.setUserMobile(users.getMobile());
		
		model.addAttribute("userChargeVo", userChargeVo);
		model.addAttribute("selectDataSource",
				usersService.selectDictCardDataSource());
		model.addAttribute("chargeWayDataSource",
				usersService.getChargeWayDataSource());

		return "user/chargeForm";
	}

	
	/**********
	 *  2016年3月25日18:28:50 
	 *  
	 *    会员充值  相关			
	 * 
	 */
	
	/* 
	 *  运营平台-- 会员充值列表--充值--获取验证码
	 */
	@AuthPassport
	@RequestMapping(value = "get_user_sms_token.json", method = RequestMethod.GET)
	public AppResultData<String> getSmsToken(
			@RequestParam("userId") Long userId,
			@RequestParam("userMobile") String userMobile,
			@RequestParam("smsType") int sms_type) {
		
		AppResultData<String> result = new AppResultData<String>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		/*
		 * 校验  手机号 和 用户 是否匹配
		 */
		Users users = usersService.selectByUsersId(userId);
		
		String mobile = users.getMobile();
		
		if(!mobile.equals(userMobile)){
			
			result.setStatus(Constants.ERROR_999);
			result.setMsg("手机号和用户不匹配");
			
			return result;
		}
		
		
		//1.调用RandomUtil.randomNumber()产生六位验证码
		String code = RandomUtil.randomNumber(4);

		String[] content = new String[] { code, Constants.GET_CODE_MAX_VALID };
		//2.短信平台发送给用户，并返回相关信息（短信平台是30分钟有效）
		HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile,
				Constants.GET_USER_VERIFY_ID, content);
		UserSmsToken record = userSmsTokenService.initUserSmsToken(mobile,
				sms_type, code, sendSmsResult);
		//3.操作user_sms_token，保存验证码信息
		userSmsTokenService.insert(record);

		result.setStatus(Constants.SUCCESS_0);
		result.setMsg("发送成功");
		
		return result;
	}
	
	
	/**
	 * 
	 *  @Title: doChargeForm
	  * @Description: 
	  *        提交  会员充值 结果
	  * @param userId	用户id
	  * @param chargeWay 充值方式
	  * @param chargeMoney 充值金额	
	  * 			
	  * 
	  * @param userCode  验证码
	 */
	@AuthPassport
	@RequestMapping(value = "/charge-form.json", method = { RequestMethod.POST })
	public AppResultData<Object> doChargeForm(HttpServletRequest request,
			@RequestParam("userId") Long userId,
			@RequestParam("userMobile")String userMobile,
			@RequestParam("chargeWay")Short chargeWay,
			@RequestParam("chargeMoney")Long chargeMoney,
			@RequestParam("userCode")String userCode) {
		
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		//1. 判断 手机号 和 验证码是否 匹配
		Users user = usersService.selectByUsersId(userId);
		
		// 最新的 一条 验证码。类型为 3,表示  运营平台--会员充值验证码
		UserSmsToken smsToken = userSmsTokenService.selectByMobileAndType(userMobile, Constants.SMS_TYPE_3);
		
		String token = smsToken.getSmsToken();
		
		if(!token.equals(userCode)){
			
			result.setStatus(Constants.ERROR_999);
			result.setMsg("验证码与用户不匹配");
			return result;
		}
		
		
		FinanceRecharge finace = financeService.initFinace();
		
		finace.setUserId(userId);
		//用户余额
		BigDecimal restMoney = user.getRestMoney();
		
		finace.setRestMoneyBefore(restMoney);
		
		//充值金额
		BigDecimal cardValue = new BigDecimal(0);
		
		
		//2. 金额相关
		if(chargeWay == (short) 0){
			/*
			 * 如果是 固定金额充值
			 *   
			 *   chargeMoney 表示在 dict_card_type 表中，该记录的 主键id
			 */
			DictCardType cardType = cardTypeService.selectByPrimaryKey(chargeMoney);
			
			cardValue = cardType.getCardValue();
			
		}else{
			
			//如果是任意金额充值。该值就是 充值的具体数字, 保留两位小数
			cardValue = MathBigDeciamlUtil.round(new BigDecimal(chargeMoney), 2);
		}
		
		//设置充值金额
		finace.setRechargeValue(cardValue);
		
		//充值后 余额
		BigDecimal afterMoney = MathBigDeciamlUtil.add(restMoney, cardValue);
		
		finace.setRestMoneyAfter(afterMoney);
		
		/*
		 *  3. 充值操作的 人员
		 */
		AccountAuth auth = AuthHelper.getSessionAccountAuth(request);
		
		//当前登录 用户的 id
		Long id = auth.getId();
		
		finace.setAdminId(id);
		
		finace.setAdminName(auth.getUsername());
		
		//TODO 登录角色 的 手机号， 无法获得
		finace.setAdminMobile("");
		
		/*
		 * 4. 审批手机号、验证码
		 */
		finace.setApproveMobile(userMobile);
		finace.setApproveToken(userCode);
		
		financeService.insertSelective(finace);
		
		//此时 更新 用户 余额
		
		user.setRestMoney(afterMoney);
		usersService.updateByPrimaryKeySelective(user);
		
		// TODO 5. 充值成功后,将该验证码。设为无效
		
		result.setMsg("充值成功");
		
		return result;
	}
	
	
	/*
	 * 充值记录
	 */
	@AuthPassport
	@RequestMapping(value = "finace_recharge_list",method = RequestMethod.GET)
	public String getAllChargeList(Model model,HttpServletRequest request,FinanceSearchVo searchVo){
		
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());

		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);

		PageHelper.startPage(pageNo, pageSize);
		
		
		// 过滤 显示  当前  登录 用户 
		AccountAuth auth = AuthHelper.getSessionAccountAuth(request);
		
		Long id = auth.getId();
		
		searchVo.setAdminId(id);
		
		
		List<FinanceRecharge> list = financeService.selectByListPage(searchVo);
		
		FinanceRecharge finance = null;
		for (int i = 0; i < list.size(); i++) {
			
			finance = list.get(i);
			
			FinanceRechargeVo financeVo = financeService.transToFinanceVo(finance);
			
			list.set(i, financeVo);
		}
		
		PageInfo result = new PageInfo(list);
		
		model.addAttribute("financeListModel", result);

		return "user/financeList";
	}
	
	/*
	 *   发起充值操作的 列表页
	 */
	
//	@AuthPassport
	@RequestMapping(value = "/finance_user-list", method = { RequestMethod.GET })
	public String getUserList(HttpServletRequest request, Model model,
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

		return "user/financeUserList";
	}
	
}
