package com.jhj.action.app.newlogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.diffrentUserLogIn.DiffrentUserReturnVo;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2016年1月14日下午6:11:58
 * @Description: 
 *	
 *		
 *		
 *
 */
@Controller
@RequestMapping(value = "/app/newLogIn")
public class DiffrentUserLogInController extends BaseController {
	
	@Autowired
	private UsersService userService;
	
	@Autowired
	private UserAddrsService userAddrService;
	
	@Autowired
	private UserRefAmService userRefAmService;
	
	@Autowired
	private OrgStaffsService orgStaffService;
	
	/**
	 * 
	 *  @Title: signOnDingdang
	  * @Description: 
	  * 
	  * 	第三方app 注册 叮当到家 用户	
	  * 
	  * @param mobile			用户手机号
	  * @param appFlag			应用 标识，由叮当到家提供
	  * 
	  * @return AppResultData<Object>    返回类型
	 */
	@RequestMapping(value = "sign_in.json",method =RequestMethod.GET)
	public AppResultData<Object> signOnDingdang(
			@RequestParam("mobile") String mobile,
			@RequestParam("app_flag")Short appFlag){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		Users u = userService.getUserByMobile(mobile);
		
		if (u == null) {
			// 验证手机号是否已经注册，如果未注册，则自动注册用户，
			u = userService.genUser(mobile,appFlag);
			
		}
		
		//返回 vo
		DiffrentUserReturnVo returnVo = new DiffrentUserReturnVo();
		
		// mybatis已设置主键自增,可直接获取 userId
		returnVo.setUserId(u.getId());
		returnVo.setMobile(mobile);
		
		result.setData(returnVo);
		
		return result;
	}
}
