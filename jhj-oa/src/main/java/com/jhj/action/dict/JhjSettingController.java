package com.jhj.action.dict;

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
import com.jhj.po.model.dict.DictAd;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.po.model.survey.SurveyBank;
import com.jhj.service.orderReview.SettingService;
import com.meijia.utils.BeanUtilsExp;

/**
 *
 * @author :hulj
 * @Date : 2016年2月22日上午10:28:39
 * @Description: 
 *		
 *  运营平台--基础配置--app业务参数配置	 	
 *	
 */
@Controller
@RequestMapping(value ="/base")
public class JhjSettingController extends BaseController {
	
	
	@Autowired
	private SettingService jhjSettingService;
	
	@RequestMapping(value = "jhj_setting_list",method = RequestMethod.GET)
	public String getSettingList(Model model, HttpServletRequest request){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		List<JhjSetting> list = jhjSettingService.selectByListPage();
		
		PageInfo result = new PageInfo(list);	
		
		model.addAttribute("jhjSettingModel",result);
		
		
		return "dict/jhjSettingList";
	}
	
	@RequestMapping(value = "jhj_setting_form",method = RequestMethod.GET)
	public String jhjSettingForm(HttpServletRequest request, Model model,
			@RequestParam("id") Long id){
		
		if(id == null){
			id = 0L;
		}
		
		JhjSetting jhjSetting = jhjSettingService.initJhjSetting();
		
		if( id !=null && id >0L){
			
			jhjSetting = jhjSettingService.selectByPrimaryKey(id);
			
		}
		
		model.addAttribute("jhjSettingFormModel", jhjSetting);
		return "dict/jhjSettingForm";
	}

	
	@RequestMapping(value = "jhj_setting_form",method = RequestMethod.POST)
	public String jhjSettingForm(HttpServletRequest request, Model model,
			@ModelAttribute("jhjSettingFormModel") JhjSetting paramJhjSetting,BindingResult result){
		

		JhjSetting jhjSetting = jhjSettingService.initJhjSetting();

		Long id = paramJhjSetting.getId();
		
		
		if(id == 0L){
			
			BeanUtilsExp.copyPropertiesIgnoreNull(paramJhjSetting, jhjSetting);
			
			jhjSettingService.insertSelective(jhjSetting);
			
		}else{
			
			jhjSetting = jhjSettingService.selectByPrimaryKey(id);
			
			BeanUtilsExp.copyPropertiesIgnoreNull(paramJhjSetting, jhjSetting);
			
			jhjSettingService.updateByPrimaryKeySelective(jhjSetting);
			
		}
		
		return "redirect:/base/jhj_setting_list";
	}
	
	
}
