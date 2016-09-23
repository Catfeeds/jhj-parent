package com.jhj.action.app.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.user.UserTrailHistory;
import com.jhj.po.model.user.UserTrailReal;
import com.jhj.service.users.UserTrailHistoryService;
import com.jhj.service.users.UserTrailRealService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.user.UserTrailSearchVo;
import com.meijia.utils.vo.AppResultData;
import com.meijia.utils.TimeStampUtil;

@Controller
@RequestMapping(value = "/app/user")
public class UserTrailController extends BaseController {

	@Autowired
	private UsersService userService;

	@Autowired
	private UserTrailRealService userTrailRealService;

	@Autowired
	private UserTrailHistoryService userTrailHistoryService;

	/**
	 * 用户地理位置记录接口
	 * 
	 * @param userId
	 * @param userType
	 * @param lat
	 * @param lng
	 * @return
	 */
	@RequestMapping(value = "post_user_trail", method = RequestMethod.POST)
	public AppResultData<String> trail(@RequestParam("user_id") Long userId, @RequestParam("user_type") short userType, @RequestParam("lat") String lat,
			@RequestParam("lng") String lng, @RequestParam(value = "poi_name", required = false, defaultValue = "") String poiName) {

		UserTrailReal record = null;

		UserTrailSearchVo searchVo = new UserTrailSearchVo();
		searchVo.setUserId(userId);
		searchVo.setUserType(userType);

		List<UserTrailReal> userTrails = userTrailRealService.selectBySearchVo(searchVo);
		if (!userTrails.isEmpty())
			record = userTrails.get(0);

		if (record != null) {
			record.setUserId(userId);
			record.setLat(lat);
			record.setLng(lng);
			record.setUserType(userType);
			record.setPoiName(poiName);
			record.setAddTime(TimeStampUtil.getNow() / 1000);
			userTrailRealService.updateByPrimaryKeySelective(record);
		} else {
			record = new UserTrailReal();
			record.setId(0L);
			record.setUserId(userId);
			record.setLat(lat);
			record.setLng(lng);
			record.setUserType(userType);
			record.setPoiName(poiName);
			record.setAddTime(TimeStampUtil.getNow() / 1000);
			userTrailRealService.insert(record);
		}
		UserTrailHistory userTrailHistory = new UserTrailHistory();

		userTrailHistory.setUserId(userId);
		userTrailHistory.setUserType(userType);
		userTrailHistory.setLat(lat);
		userTrailHistory.setLng(lng);
		userTrailHistory.setPoiName(poiName);
		userTrailHistory.setAddTime(TimeStampUtil.getNow() / 1000);

		userTrailHistoryService.insertSelective(userTrailHistory);

		AppResultData<String> result = new AppResultData<String>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		return result;
	}
}
