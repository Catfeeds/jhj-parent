package com.jhj.action.app.user;

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
import com.jhj.po.model.user.UserRefAm;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.users.UserGetAmService;
import com.jhj.service.users.UserRefAmService;
import com.meijia.utils.vo.AppResultData;
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
public class UserGetAmController extends BaseController {
	
	@Autowired
	private UserGetAmService userGetAmService;
	
	@Autowired
	private OrgStaffsService orgStaffService;
	
	@Autowired
	private UserRefAmService userRefAmService;
	
	/*
	 * 用户版查看助理页签
	 */
	@RequestMapping(value = "user_get_am",method = RequestMethod.GET)
	public AppResultData<Object> getAmByUser(@RequestParam("user_id") Long userId){
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		UserGetAmVo userGetAmVo = userGetAmService.getAmVoByUserId(userId);
		
		result.setData(userGetAmVo);
		
		return result;
	}
	
	/*
	 * 用户版查看助理页签
	 */
	@RequestMapping(value = "user_get_am_id",method = RequestMethod.GET)
	public AppResultData<Object> getAmIdByUser(@RequestParam("user_id") Long userId){
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		UserRefAm userRefAm = userRefAmService.selectByAmId(userId);
		
		if (userRefAm == null) return result;
		
		Long staffId = userRefAm.getStaffId();
		
		OrgStaffs orgStaffs = orgStaffService.selectByPrimaryKey(staffId);
		
		Map<String, String> resultMap = new HashMap<String, String>();
		
		resultMap.put("am_id", staffId.toString());
		resultMap.put("am_mobile", orgStaffs.getMobile());
		
		result.setData(resultMap);
		
		return result;
	}	
	
	/*
	 * 助理版-我的  
	 */
	@RequestMapping(value = "get_am_by_amId",method = RequestMethod.GET)
	public AppResultData<Object> getAmByAmId(@RequestParam("amId") Long amId){
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		UserGetAmVo userGetAmVo = userGetAmService.getAmVoByAmId(amId);
		
		result.setData(userGetAmVo);
		return result;
	}
	
	/*
	 * 助理版--我的--查看管辖阿姨
	 */
	@RequestMapping(value = "get_staff_by_amId" , method =RequestMethod.GET)
	public AppResultData<Object> getOrgStaffByAmId(
			@RequestParam("amId") Long amId,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page){
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		
		
		List<OrgStaffs> staffList = orgStaffService.selectStaffByAmIdListPage(amId, page, Constants.PAGE_MAX_NUMBER);
		result.setData(staffList);
		
		return result;
		
	}
}
