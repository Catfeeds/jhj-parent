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
import com.jhj.po.model.survey.SurveyBank;
import com.jhj.service.survey.SurveyBankService;
import com.meijia.utils.BeanUtilsExp;

/**
 *
 * @author :hulj
 * @Date : 2015年12月10日下午4:43:18
 * @Description: 
 *
 */
@Controller
@RequestMapping(value = "/survey")
public class SurveyBankController extends BaseController {
	
	@Autowired
	private SurveyBankService bankService;
	
	@RequestMapping(value = "bank_list",method = RequestMethod.GET)
	public String bankList(Model model,HttpServletRequest request){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		List<SurveyBank> list = bankService.selectByListPage();
		
		PageInfo result = new PageInfo(list);	
		
		model.addAttribute("bankListModel", result);
		
		return "survey/bankList";
	}
	
	/*
	 * 跳转到 form页
	 */
	@RequestMapping(value = "bank_form",method = RequestMethod.GET)
	public String bankForm(HttpServletRequest request, Model model,
					@RequestParam("id") Long id){
								
		if(id == null){
			id = 0L;
		}
		
		SurveyBank bank = bankService.initBank();
		
		if( id !=null && id >0L){
			bank = bankService.selectByPrimaryKey(id);
		}
		
		model.addAttribute("bankFormModel", bank);
		
		return "survey/bankForm";
	}
	
	/*
	 * 提交form
	 */
	@RequestMapping(value = "/bank_form", method = RequestMethod.POST)
	public String submitBankForm(@ModelAttribute("bankFormModel") SurveyBank paramBank,
			BindingResult result){

		SurveyBank studyBank = bankService.initBank();
		
		Long bankId = paramBank.getBankId();
		
		if(bankId == 0L){
			
			BeanUtilsExp.copyPropertiesIgnoreNull(paramBank, studyBank);
			
			bankService.insertSelective(studyBank);
			
		}else{
			
			studyBank = bankService.selectByPrimaryKey(bankId);
			
			BeanUtilsExp.copyPropertiesIgnoreNull(paramBank, studyBank);
			
			bankService.updateByPrimaryKeySelective(studyBank);
			
		}
		
		return "redirect:bank_list";
	}
	
	
}
