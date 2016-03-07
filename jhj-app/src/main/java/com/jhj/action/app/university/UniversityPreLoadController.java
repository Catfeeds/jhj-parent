package com.jhj.action.app.university;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.app.BaseController;
import com.jhj.common.Constants;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.university.StudyLearning;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.university.StudyLearningService;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2016年1月15日下午5:26:58
 * @Description: 
 *	
 *		叮当大学--需要预加载的 相对无大变化的 数据	
 *		
 */
@Controller
@RequestMapping(value = "/app/university")
public class UniversityPreLoadController extends BaseController {
	
	
	@Autowired
	private PartnerServiceTypeService partnerService;
	
	@Autowired
	private StudyLearningService learningService;
	
	/**
	 * 
	 *  @Title: loadAllServiceType
	  * @Description: 
	  * 	
	  * 	预加载内容, 预加载所有考核项目, 即partner_service_type表中，parent_id = 0 的记录 	
	  * 
	  * @return AppResultData<Object>    返回类型
	  * @throws
	 */
	@RequestMapping(value ="load_all_service.json",method = RequestMethod.GET)
	public AppResultData<Object> loadAllServiceType(){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		List<PartnerServiceType> list = partnerService.selectNoParentServiceObj();
		
		result.setData(list);
		
		return result;
	}
	
	
	/**
	 * 
	 *  @Title: loadStudyLearning
	  * @Description: 
	  * 	加载  培训 学习资料
	  * 	由于 培训项目的 相对固定，培训资料也可以在进入 app时预加载
	 */
	@RequestMapping(value = "load_study_learning.json",method = RequestMethod.GET)
	public AppResultData<Object> loadStudyLearning(){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		List<StudyLearning> list = learningService.selectAllLearning();
		
		result.setData(list);
		
		return result;
	}
}
