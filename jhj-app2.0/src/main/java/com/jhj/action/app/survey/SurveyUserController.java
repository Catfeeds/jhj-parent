package com.jhj.action.app.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.survey.SurveyUser;
import com.jhj.service.survey.SurveyUserService;
import com.meijia.utils.StringUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年12月22日上午10:57:09
 * @Description: 
 *		
 *		问卷调查
 *			第一步--用户信息录入	
 */
@Controller
@RequestMapping(value = "/app/survey")
public class SurveyUserController extends BaseController {
	
	@Autowired
	private SurveyUserService userService;
	
	/**
	 *  !!!此处发现一个bug   注解 value值,不带.json  会出现 请求 方法执行完毕。不能执行 success回调的情况！！！！！！
	 */
	@RequestMapping(value = "survey_user_submit.json",method = RequestMethod.POST)
	public  AppResultData<Object> submitUserInfo(
			@RequestParam(value = "userName",required = false,defaultValue = "")String userName,
			@RequestParam(value = "age",required = false,defaultValue= "")Long age,
			@RequestParam("sex")Short sex,
			@RequestParam(value = "mobile",required = false, defaultValue = "")String mobile){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		if(StringUtil.isEmpty(mobile) || StringUtil.isEmpty(userName)){
			result.setStatus(Constants.ERROR_999);
			result.setMsg("您输入的信息不合法");
			return result;
		}
		
		if(age.toString() == "" || age == null){
			age = 0L;
		}
		
		
		SurveyUser user = userService.initUser();
		
		user.setUserName(userName);
		user.setAge(age);
		user.setSex(sex);
		user.setMobile(mobile);
		
		userService.insertSelective(user);
		
//		result.setData(user);
		
		result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, user.getId());
		
		return result;
	}
	
	
	
}
