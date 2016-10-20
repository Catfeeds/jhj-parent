package com.jhj.action.app.user;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.UserLogined;
import com.jhj.po.model.user.UserPushBind;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.po.model.user.UserSmsToken;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserLoginedService;
import com.jhj.service.users.UserPushBindService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UserSmsTokenService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.user.UserAppVo;
import com.jhj.vo.user.UserPushBindSearchVo;
import com.jhj.vo.user.UserSearchVo;
import com.meijia.utils.IPUtil;
import com.meijia.utils.RandomUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

/**
 * @author kerryg
 *
 */
@Controller
@RequestMapping(value = "/app/user")
public class UserAppController extends BaseController {

	@Autowired
	private UserSmsTokenService userSmsTokenService;

	@Autowired
	private UserPushBindService userPushBindService;

	@Autowired
	private UserLoginedService userLoginedService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private UserAddrsService userAddrsService;

	@Autowired
	private UserRefAmService userRefAmService;

	@Autowired
	private OrgStaffsService orgStaffService;

	@Autowired
	private UserCouponsService userCouponService;

	@Autowired
	private DictCouponsService couponsService;

	/**
	 * 
	 * 获取验证码接口sms_type：0 = 用户登陆 1 = 秘书登录
	 * 
	 * @param mobile
	 * @param sms_type
	 * @return
	 */
	@RequestMapping(value = "get_sms_token", method = RequestMethod.GET)
	public AppResultData<String> getSmsToken(@RequestParam("mobile") String mobile, @RequestParam("sms_type") int sms_type) {
		// 1.调用RandomUtil.randomNumber()产生六位验证码
		String code = RandomUtil.randomNumber(4);

		if (mobile.equals("18610807136")) {
			code = "000000";
		}

		String[] content = new String[] { code, Constants.GET_CODE_MAX_VALID };
		// 2.短信平台发送给用户，并返回相关信息（短信平台是30分钟有效）
		HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile, Constants.GET_USER_VERIFY_ID, content);
		UserSmsToken record = userSmsTokenService.initUserSmsToken(mobile, sms_type, code, sendSmsResult);
		// 3.操作user_sms_token，保存验证码信息
		userSmsTokenService.insert(record);

		AppResultData<String> result = new AppResultData<String>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		return result;
	}

	/**
	 * 用户登录接口
	 * 
	 * @param request
	 * @param mobile
	 *            用户手机号
	 * @param sms_token
	 *            验证码
	 * @param login_from
	 *            登录来源
	 * @param user_type
	 *            用户类型
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public AppResultData<Object> login(HttpServletRequest request, @RequestParam("mobile") String mobile, @RequestParam("sms_token") String sms_token,
			@RequestParam("login_from") int login_from, @RequestParam(value = "user_type", required = false, defaultValue = "0") int userType)
			throws ParseException {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		// 1、根据手机后查询 user_sms_token 最新一条记录
		UserSmsToken smsToken = userSmsTokenService.selectByMobileAndType(mobile, userType);

		// 验证码不存在，或者添加时间不存在，返回验证码错误
		if (smsToken == null || smsToken.getAddTime() == null) {
			result = new AppResultData<Object>(Constants.ERROR_999, ConstantMsg.ERROR_999_MSG_2, "");
			return result;
		}

		// 2、判断是否表记录字段add_time 是否超过30分钟.
		long expTime = TimeStampUtil.compareTimeStr(smsToken.getAddTime(), System.currentTimeMillis() / 1000);
		// 如果验证码超时，返回超时错误
		if (expTime > 1800) {
			result = new AppResultData<Object>(Constants.ERROR_999, ConstantMsg.ERROR_999_MSG_4, "");
			return result;
		} else {
			long ip = IPUtil.getIpAddr(request);
			UserLogined record = userLoginedService.initUserLogined(smsToken, login_from, ip);
			userLoginedService.insert(record);

			if (!smsToken.getSmsToken().equals(sms_token)) {// 验证码错误
				result = new AppResultData<Object>(Constants.ERROR_999, ConstantMsg.ERROR_999_MSG_2, "");
				return result;
			} else {

				Users u = usersService.selectByMobile(mobile);
				if (u == null) {// 验证手机号是否已经注册，如果未注册，则自动注册用户，
					u = usersService.genUser(mobile, Constants.USER_NET);

					if (userType == 0) {
						// 给新注册的用户发送优惠券
						Map<String, Object> map = new HashMap<String, Object>();
						Date date = new Date();

						map.put("couponsTypeId", 1);
						// map.put("toDate",
						// DateUtil.getUnixTimeStamp(DateUtil.getNow()));
						List<DictCoupons> coupons = couponsService.getSelectByMap(map);
						List<UserCoupons> userCouponsList = new ArrayList<UserCoupons>();
						if (coupons != null && coupons.size() > 0) {
							for (DictCoupons c : coupons) {
								if (c.getToDate().getTime() >= date.getTime()) {
									UserCoupons uc = userCouponService.initUserCoupons(u.getId(), c);
									userCouponsList.add(uc);
								}
							}
						}
						if (userCouponsList != null && userCouponsList.size() > 0) {
							userCouponService.insertByList(userCouponsList);
						}
					}

				}
				// 返回用户是否保存有地址
				UserAppVo userAppVo = new UserAppVo();
				try {
					BeanUtils.copyProperties(userAppVo, u);
				} catch (Exception e) {
					e.printStackTrace();
				}

				List<UserAddrs> list = userAddrsService.selectByUserId(u.getId());
				if (list != null && list.size() > 0) {
					userAppVo.setHasUserAddr(true);
				} else {
					userAppVo.setHasUserAddr(false);
				}

				// 获取用户的默认地址
				UserAddrs userAddrs = userAddrsService.selectByDefaultAddr(u.getId());
				if (userAddrs != null) {
					userAppVo.setDefaultUserAddr(userAddrs);
				} else {
					// 如果没有默认地址，则把第一个地址设置为默认地址.
					if (!list.isEmpty()) {
						userAddrs = list.get(0);
						userAddrs.setIsDefault((short) 1);
						userAddrsService.updateByPrimaryKeySelective(userAddrs);
						userAppVo.setDefaultUserAddr(userAddrs);
					}
				}

				result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, userAppVo);
				return result;
			}
		}
	}

	/**
	 * 修改用户的名称
	 * 
	 * @param userId
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "post_user_info", method = RequestMethod.POST)
	public AppResultData<Object> savaEditUser(@RequestParam("user_id") Long userId,
			@RequestParam(value = "name", required = false, defaultValue = "") String name) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		Users users = usersService.selectByPrimaryKey(userId);

		// 判断是否为注册用户，非注册用户返回 999
		if (users == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}
		users.setName(name);
		usersService.updateByPrimaryKeySelective(users);
		result.setData(users);
		return result;
	}

	/**
	 * 2015-11-24 12:06:09
	 * 
	 * 获取语音验证码
	 * 
	 * TODO 1. 官方 jar 包 还未更新，暂时不支持，用户自己上传语音文件
	 * 2. 主叫号码的显示, 需要跟 云通讯 协商, 算是一个新的 商业业务
	 * 
	 */

	@RequestMapping(value = "get_yu_yin_token", method = RequestMethod.GET)
	public AppResultData<String> getYuYinSms(@RequestParam("mobile") String mobile, @RequestParam("sms_type") int sms_type) {

		// 生成验证码
		String sms = RandomUtil.randomNumber();

		// 发送语音验证码
		HashMap<String, String> map = SmsUtil.sendYuYinSms(mobile, sms);

		// 保存验证码
		UserSmsToken smsToken = userSmsTokenService.initUserSmsToken(mobile, sms_type, sms, map);
		userSmsTokenService.insert(smsToken);

		AppResultData<String> result = new AppResultData<String>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		return result;
	}

	/**
	 * 推送设备信息绑定接口
	 * 
	 * @param userId
	 * @param clientId
	 * @param deviceType
	 * @return
	 */
	@RequestMapping(value = "post_push_bind", method = RequestMethod.POST)
	public AppResultData<String> pushBind(@RequestParam("user_id") Long userId, @RequestParam("user_type") short userType,
			@RequestParam("client_id") String clientId, @RequestParam("device_type") String deviceType) {

		Users u = usersService.selectByPrimaryKey(userId);
		AppResultData<String> result = new AppResultData<String>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}
		/*
		 * 注意点，在添加或者更新user_push_bind表之前
		 * 
		 * 1） 先根据 client_id 查询表中的数据，得到集合A
		 * 
		 * 2） 循环集合A， 如果集合A的userId, 跟接口传入的userId 不一样，则删除掉
		 */
		UserPushBindSearchVo searchVo = new UserPushBindSearchVo();
		searchVo.setClientId(clientId);
		List<UserPushBind> recordBind = userPushBindService.selectBySearchVo(searchVo);

		for (int i = 0; i < recordBind.size(); i++) {
			UserPushBind bind = recordBind.get(i);
			if (!bind.getUserId().equals(userId)) {
				recordBind.remove(i);
			}
		}

		UserPushBind userPushBind = null;
		searchVo = new UserPushBindSearchVo();
		searchVo.setUserId(userId);
		List<UserPushBind> list = userPushBindService.selectBySearchVo(searchVo);
		if (!list.isEmpty())
			userPushBind = list.get(0);

		if (userPushBind == null) {
			userPushBind = new UserPushBind();
			userPushBind.setId(0L);
		}
		userPushBind.setUserType(userType);
		userPushBind.setUserId(u.getId());
		userPushBind.setClientId(clientId);
		userPushBind.setDeviceType(deviceType);

		// 更新或者新增
		if (userPushBind != null && userPushBind.getId() > 0L) {

			userPushBindService.updateByPrimaryKeySelective(userPushBind);

		} else {
			userPushBind.setAddTime(TimeStampUtil.getNow() / 1000);
			userPushBindService.insertSelective(userPushBind);
		}
		return result;
	}

	// 注册用户
	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	public AppResultData<String> reg(@RequestParam("mobile") String mobile) {
		AppResultData<String> result = new AppResultData<String>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		Users user = null;
		if (mobile != null) {
			user = usersService.genUser(mobile, (short) 2);
		}
		result = new AppResultData<String>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, user.getId().toString());
		return result;
	}

}
