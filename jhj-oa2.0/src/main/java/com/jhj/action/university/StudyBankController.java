package com.jhj.action.university;

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
import com.jhj.common.ConstantMsg;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.po.model.university.StudyBank;
import com.jhj.service.university.StudyBankService;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年12月3日下午6:56:07
 * @Description: 
 * 
 * 		平台--订单大学--题库管理
 *
 */
@Controller
@RequestMapping(value = "/university")
public class StudyBankController extends BaseController {
	
	@Autowired
	private StudyBankService bankService;
	
	@RequestMapping(value = "bank_list",method = RequestMethod.GET)
	public String bankList(Model model,HttpServletRequest request){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		List<StudyBank> list = bankService.selectByListPage();
		
		PageInfo result = new PageInfo(list);	
		
		model.addAttribute("bankListModel", result);
		
		return "university/bankList";
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
		
		StudyBank bank = bankService.initBank();
		
		if( id !=null && id >0L){
			bank = bankService.selectByPrimaryKey(id);
		}
		
		model.addAttribute("bankFormModel", bank);
		
		return "university/bankForm";
	}
	
	/*
	 * 提交form
	 */
	@RequestMapping(value = "/bank_form", method = RequestMethod.POST)
	public String submitBankForm(@ModelAttribute("bankFormModel") StudyBank paramBank,
			BindingResult result){

		StudyBank studyBank = bankService.initBank();
		
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
	
	
	/*
	 *  实现联动：
	 *   
	 *   题目管理搜索条件：
	 *    	 	服务类别--题库			
	 */
//	get-bank-by-partnerServiceType
	@RequestMapping(value = "get-bank-by-partnerServiceType",method = RequestMethod.GET)
	public AppResultData<Object>  getBankByServiceType(
			@RequestParam(value = "serviceTypeId",required = true, defaultValue= "0") Long serviceTypeId){
		
		
		List<StudyBank> list = bankService.selectBankByServiceType(serviceTypeId);
		
		if(serviceTypeId >0L){
			list = bankService.selectBankByServiceType(serviceTypeId);
		}
		
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, list);
		
		return result;
	}
	
}
