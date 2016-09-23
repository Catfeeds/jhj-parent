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
import com.jhj.po.model.user.UserDetailPay;
import com.jhj.po.model.user.Users;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.user.AppUserDetailPayVo;
import com.jhj.vo.user.UserDetailSearchVo;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2016年4月5日下午4:27:05
 * @Description: 
 *
 */
@Controller
@RequestMapping(value = "/app/user")
public class UserPayDetailController extends BaseController {
	
	@Autowired
	private UserDetailPayService detailPayService;
	
	@Autowired
	private UsersService userService;
	
	/*
	 * 微网站-- 我的--余额点击--消费明细列表
	 */
	@RequestMapping(value = "pay_detail_list.json", method = RequestMethod.GET)
	public AppResultData<Object> orderHourNowList(
			@RequestParam("user_id") Long userId, 
			@RequestParam(value = "page", required = false, defaultValue = "1") int page){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, new String());
		
		Users users = userService.selectByPrimaryKey(userId);
		
		if(users == null){
			result.setMsg("用户不存在");
			result.setStatus(Constants.ERROR_999);
			
			return result;
		}
		
		UserDetailSearchVo searchVo = new UserDetailSearchVo();
		searchVo.setMobile(users.getMobile());
		
		List<UserDetailPay> list = detailPayService.appSelectByListPage(searchVo, page, Constants.PAGE_MAX_NUMBER);
		
		List<AppUserDetailPayVo> voList = detailPayService.transToListVo(list);
		
		result.setData(voList);
		
		return result;
	}
	
	
	
}
