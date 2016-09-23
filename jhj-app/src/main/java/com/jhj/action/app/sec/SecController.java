package com.jhj.action.app.sec;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.user.UserLogined;
import com.jhj.po.model.user.UserSmsToken;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.CityService;
import com.jhj.service.dict.DictService;
import com.jhj.service.users.UserLoginedService;
import com.jhj.service.users.UserSmsTokenService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.vo.AppResultData;
import com.jhj.vo.bs.SecInfoVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.jhj.vo.user.LoginVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.IPUtil;
import com.meijia.utils.TimeStampUtil;

@Controller
@RequestMapping(value = "/app/am")
public class SecController extends BaseController {

	@Autowired
	private UserLoginedService userLoginedService;

	/*
	 * @Autowired
	 * private OrderQueryService orderQueryService;
	 */

	@Autowired
	private UsersService userService;

	@Autowired
	private CityService cityService;

	@Autowired
	private DictService dictService;

	@Autowired
	private OrgStaffsService orgStaffsService;

	/*
	 * @Autowired
	 * private UserRefSecService userRefSecService;
	 */

	/*
	 * @Autowired
	 * private UserBaiduBindService userBaiduBindService;
	 */

	@Autowired
	private UserSmsTokenService smsTokenService;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public AppResultData<Object> login(HttpServletRequest request, @RequestParam("mobile") String mobile, @RequestParam("sms_token") String sms_token,
			@RequestParam("login_from") int login_from, @RequestParam(value = "user_type", required = false, defaultValue = "1") int userType) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		StaffSearchVo searchVo = new StaffSearchVo();
		searchVo.setMobile(mobile);
		List<OrgStaffs> orgStaffs = orgStaffsService.selectBySearchVo(searchVo);

		if (orgStaffs.isEmpty()) {

			result = new AppResultData<Object>(Constants.ERROR_999, ConstantMsg.ERROR_999_MSG_1, "");
			return result;
		}
		
		OrgStaffs item = orgStaffs.get(0);
		// 测试 账号 13521193653

		if ((mobile.trim().equals("15712917308") || mobile.trim().equals("13521193653") || mobile.trim().equals("13810002890"))
				&& sms_token.trim().equals("000000")) {
			result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, item);
			return result;

		} else {

			//

			UserSmsToken smsToken = smsTokenService.selectByMobileAndType(mobile, userType);// 1、根据mobile
			// 从表user_sms_token找出最新一条记录
			LoginVo loginVo = new LoginVo(0l, mobile);
			if (smsToken == null || smsToken.getAddTime() == null) {
				result = new AppResultData<Object>(Constants.ERROR_999, ConstantMsg.ERROR_999_MSG_2, "");
				return result;
			}
			loginVo = new LoginVo(smsToken.getUserId(), mobile);
			// 2、判断是否表记录字段add_time 是否超过十分钟.
			long expTime = TimeStampUtil.compareTimeStr(smsToken.getAddTime(), System.currentTimeMillis() / 1000);
			if (expTime > 600) {// 超时
				// 999
				result = new AppResultData<Object>(Constants.ERROR_999, ConstantMsg.ERROR_999_MSG_8, "");
				return result;
			} /*
			 * else {
			 * 
			 * }
			 */
			long ip = IPUtil.getIpAddr(request);
			UserLogined record = userLoginedService.initUserLogined(smsToken, login_from, ip);
			userLoginedService.insert(record);

			if (!smsToken.getSmsToken().equals(sms_token)) {// 验证码错误
				result = new AppResultData<Object>(Constants.ERROR_999, ConstantMsg.ERROR_999_MSG_2, "");
				return result;
			}
			result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, item);

			return result;
		}
	}

	/**
	 * 秘书信息修改
	 * 
	 * @param request
	 * @param staffId
	 * @param mobile
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "get_secinfo", method = RequestMethod.POST)
	public AppResultData<Object> Sec(HttpServletRequest request, @RequestParam("staff_id") Long staffId, @RequestParam("mobile") String mobile)
			throws ParseException {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(staffId);

		if (orgStaffs == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ERROR_999_MSG_1);
			return result;
		}

		SecInfoVo secInfoVo = orgStaffsService.changeSecToVo(orgStaffs);

		result.setData(secInfoVo);
		return result;

	}

	/**
	 * 秘书信息修改
	 * 
	 * @param secId
	 * @param mobile
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value = "post_secinfo", method = RequestMethod.POST)
	public AppResultData<Object> secInfo(
			HttpServletRequest request,
			@RequestParam("staff_id") Long staffId,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			// @RequestParam(value = "nick_name", required = false, defaultValue
			// = "") String nickName,
			@RequestParam(value = "sex", required = false, defaultValue = "") short sex,
			@RequestParam(value = "birth", required = false, defaultValue = "") String birth,
			@RequestParam(value = "city_id", required = false, defaultValue = "") Long cityId,
			@RequestParam(value = "head_img", required = false, defaultValue = "") String headImg) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.ERROR_999, ConstantMsg.USER_NOT_EXIST_MG, "");

		OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(staffId);

		if (orgStaffs == null) {

			result = new AppResultData<Object>(Constants.ERROR_999, ConstantMsg.ERROR_999_MSG_1, "");
			return result;
		}

		if (!StringUtils.isEmpty(request.getParameter("mobile")) && !name.equals(orgStaffs.getMobile())) {

			orgStaffs.setMobile(request.getParameter("mobile"));

		}
		if (!StringUtils.isEmpty(name)) {

			orgStaffs.setName(name);

		}
		if (!StringUtils.isEmpty(sex)) {
			orgStaffs.setSex(sex);
		}

		if (!StringUtils.isEmpty(birth)) {

			orgStaffs.setBirth(DateUtil.parse(birth));

		}
		if (!cityId.equals(cityId)) {
			orgStaffs.setCityId(cityId);
		}

		if (!StringUtils.isEmpty(headImg)) {
			orgStaffs.setHeadImg(headImg);
		}

		if (cityId > 0L) {
			orgStaffs.setCityId(cityId);
		}

		// 保存上传的图像

		orgStaffsService.updateByPrimaryKeySelective(orgStaffs);

		SecInfoVo secInfoVo = orgStaffsService.changeSecToVo(orgStaffs);

		result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, secInfoVo);
		return result;

	}
}
