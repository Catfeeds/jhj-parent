package com.jhj.action.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;
import com.jhj.po.model.user.UserFeedback;
import com.jhj.po.model.user.Users;
import com.jhj.service.users.UserFeedBackService;
import com.jhj.service.users.UsersService;

@Controller
@RequestMapping(value="/app/user")
public class UserFeedBackController extends BaseController {
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private UserFeedBackService userFeedBackService;
	
	
	/**
	 * 保存用户意见信息
	 * @param userId
	 * @param content
	 * @return
	 */
	@RequestMapping(value = "post_feed_back", method = RequestMethod.POST)
	public AppResultData<Object> postFeedBack(
			@RequestParam("user_id")	Long userId,
			@RequestParam("content") 	String content) {

		AppResultData<Object> result = new AppResultData<Object>( Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");		
		Users users = usersService.selectByPrimaryKey(userId);
		// 判断是否为注册用户，非注册用户返回 999
		if (users == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}		
		UserFeedback  userFeedback = userFeedBackService.selectByUserId(userId);
		 if(userFeedback !=null){
			 userFeedback.setContent(content);
			 userFeedBackService.updateByPrimaryKeySelective(userFeedback);
		 }else{
			 userFeedback = userFeedBackService.initUserFeedBack();
			 userFeedback.setMobile(users.getMobile());
			 userFeedback.setUserId(userId);
			 userFeedback.setContent(content);
			 userFeedback.setAddTime(TimeStampUtil.getNowSecond());
			 userFeedBackService.insertSelective(userFeedback);
		 }
		result.setData(userFeedback);
		return result;
	}

}
