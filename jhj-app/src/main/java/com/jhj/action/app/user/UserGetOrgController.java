package com.jhj.action.app.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.po.model.user.UserRefOrg;
import com.jhj.po.model.user.Users;
import com.jhj.service.async.UsersAsyncService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserGetAmService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UserRefOrgService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.vo.AppResultData;
import com.jhj.vo.user.CompareOrderNumForStaff;
import com.jhj.vo.user.UserGetAmVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月10日下午4:01:21
 * @Description: 
 *			
 *		1、 用户版--用户查看助理页签
 *      2、 助理版 -- 我的
 */
@Controller
@RequestMapping(value = "/app/user")
public class UserGetOrgController extends BaseController {
	
	@Autowired
	private UsersService userService;
	
	@Autowired
	private UserGetAmService userGetAmService;
	
	@Autowired
	private OrgStaffsService orgStaffService;
	
	@Autowired
	private UserRefAmService userRefAmService;
	
	@Autowired
	private UserRefOrgService userRefOrgService;
	
	@Autowired
	private UserAddrsService userAddrsService;
	
	@Autowired
	private UsersAsyncService usersAsyncService;
	
	/*
	 * 用户版查看助理页签
	 */
	@RequestMapping(value = "user_get_org",method = RequestMethod.GET)
	public AppResultData<Object> getOrgByUser(@RequestParam("user_id") Long userId){
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		UserRefOrg userRefOrg = userRefOrgService.selectByUserId(userId);
		
		if (userRefOrg == null) return result;
		
		Long orgId = userRefOrg.getOrgId();
		List<OrgStaffs> orgStaffs = orgStaffService.selectByOrgId(orgId);
		
		List<UserGetAmVo> list = new ArrayList<UserGetAmVo>();
		
		for (OrgStaffs item : orgStaffs) {
			
			UserGetAmVo userGetAmVo = userGetAmService.getStaffByUserId(userId, item.getStaffId());
			
			list.add(userGetAmVo);
		}
		
		//自定义排序，按服务次数倒序 
		Collections.sort(list, new CompareOrderNumForStaff());
		
		//列表展示 ，只展示前5条
		if(list.size() >= 5){
			list = list.subList(0, 5);
		}
		
		result.setData(list);
		
		return result;
	}
	
	/*
	 * 用户版查看助理页签
	 */
	@RequestMapping(value = "user_get_staff_detail",method = RequestMethod.GET)
	public AppResultData<Object> getStaffByUser(
			@RequestParam("user_id") Long userId, 
			@RequestParam("staff_id") Long staffId){
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		UserGetAmVo userGetAmVo = userGetAmService.getStaffByUserId(userId, staffId);
		
		result.setData(userGetAmVo);
		
		return result;
	}	
	
	@RequestMapping(value = "gen_user_org",method = RequestMethod.GET)
	public AppResultData<Object> genUserOrg(){
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		List<Users> userlist = userService.selectByAll();
		
		for (Users u : userlist) {
			Long userId = u.getId();
			
			UserRefOrg userRefOrg = userRefOrgService.selectByUserId(userId);
			if (userRefOrg != null) continue;
			
			UserAddrs userAddrs = userAddrsService.selectByDefaultAddr(Long.valueOf(userId));
			
			if (userAddrs == null) continue;
			
			usersAsyncService.allotOrg(userAddrs.getId());	
		}
				
		return result;
	}		
	
}
