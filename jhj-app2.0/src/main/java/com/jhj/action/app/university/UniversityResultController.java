package com.jhj.action.app.university;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.Constants;
import com.jhj.service.university.UniversityResultService;
import com.jhj.vo.university.AppUniversityResultVo;
import com.meijia.utils.StringUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2016年1月18日下午5:03:30
 * @Description: 
 *			
 *	  叮当大学--考试结果相关   		
 */
@Controller
@RequestMapping(value ="/app/university")
public class UniversityResultController extends BaseController {
	
	
	@Autowired
	private UniversityResultService resultService;
	
	/**
	 * 
	 *  @Title: submitUniversityResult
	  * @Description: 
	  * 	 提交测验结果
	  * @param staffId  服务人员id
	  * @param serviceTypeId  服务类别id,“考试科目”
	  * @param universityResult
	  * 	
	  * 	答题情况
	  * 	[{"questionId":xx,"optionNo":xx}...]
	  * 
	  * @return AppResultData<Object>    返回类型
	 * @throws JSONException 
	 */
	@RequestMapping(value = "submit_university_result.json",method = RequestMethod.POST)
	public AppResultData<Object> submitUniversityResult(
			@RequestParam("service_type_id")Long serviceTypeId,
			@RequestParam("staff_id")Long staffId,
			@RequestParam("university_result")String universityResult) throws JSONException{
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		if(StringUtil.isEmpty(universityResult)){
			result.setStatus(Constants.ERROR_999);
			result.setMsg("参数错误");
			return result;
		}
		
		AppUniversityResultVo resultVo = resultService.insertRecord(staffId, serviceTypeId, universityResult);
		
		result.setData(resultVo);
		
		return result;
	}
}
