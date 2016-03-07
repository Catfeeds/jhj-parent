package com.jhj.action.survey;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.po.model.survey.SurveyService;
import com.jhj.service.survey.SurveyServiceService;
import com.meijia.utils.BeanUtilsExp;

/**
 *
 * @author :hulj
 * @Date : 2015年12月12日下午4:09:35
 * @Description: 
 *		
 *     问卷调查-- 服务大类管理
 *		
 */
@Controller
@RequestMapping(value = "/survey")
public class SurveyServiceController extends BaseController {
	
	@Autowired
	private SurveyServiceService surveyService;
	
	
	@RequestMapping(value = "service_list",method = RequestMethod.GET)
	public String bankList(Model model,HttpServletRequest request){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		
		List<SurveyService> list = surveyService.selectByListPage();
		
		PageInfo result = new PageInfo(list);	
		
		model.addAttribute("serviceListModel", result);
		
		return "survey/serviceList";
	}
	
	/*
	 * 跳转到 form页
	 */
	@RequestMapping(value = "service_form",method = RequestMethod.GET)
	public String bankForm(HttpServletRequest request, Model model,
					@RequestParam("id") Long id){
								
		if(id == null){
			id = 0L;
		}
		
//		SurveyBank bank = bankService.initBank();
		
		SurveyService service = surveyService.initService();
		
		if( id !=null && id >0L){
			service = surveyService.selectByPrimaryKey(id);
		}
		
		model.addAttribute("serviceFormModel", service);
		
		return "survey/serviceForm";
	}
	
	/*
	 * 提交form
	 */
	@RequestMapping(value = "/service_form", method = RequestMethod.POST)
	public String submitBankForm(@ModelAttribute("serviceFormModel") SurveyService paramService,
			BindingResult result){

		SurveyService service = surveyService.initService();
		
		Long serviceId = paramService.getServiceId();
		
		if(serviceId == 0L){
			
			BeanUtilsExp.copyPropertiesIgnoreNull(paramService, service);
			
			surveyService.insertSelective(service);
			
		}else{
			
			service = surveyService.selectByPrimaryKey(serviceId);
			
			BeanUtilsExp.copyPropertiesIgnoreNull(paramService, service);
			
			surveyService.updateByPrimaryKeySelective(service);
			
		}
		
		return "redirect:service_list";
	}
	
	
}
