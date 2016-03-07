package com.jhj.action.app.university;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.Constants;
import com.jhj.service.university.StudyStaffPassQueryService;
import com.jhj.vo.university.AppStaffTestFirstVo;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2016年1月8日下午6:19:13
 * @Description: 
 *		
 *	  app- 叮当大学- 服务人员相关 	
 */
@Controller
@RequestMapping(value = "/app/university")
public class UniversityStaffController extends BaseController {
	
	
	@Autowired
	private StudyStaffPassQueryService studyPassQueryService;
	
	/**
	 * 
	 *  @Title: getStaffTestStatus
	  * @Description: 
	  * 	
	  * 	叮当大学--首页
	  * 	
	  * @param staffId		服务人员Id
	  * @throws
	 */
	@RequestMapping(value = "get_staff_test_status.json",method = RequestMethod.GET)
	public AppResultData<Object> getStaffTestStatus(
			@RequestParam("staff_id")Long staffId){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
//		Map<Long, Short> map = studyPassQueryService.getStatusByStaffId(staffId);
		
		List<AppStaffTestFirstVo> list = studyPassQueryService.appGetStatusByStaffId(staffId);
		
		result.setData(list);
		
		return result;
	}
	
}
