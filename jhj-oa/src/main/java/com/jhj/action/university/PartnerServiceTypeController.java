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
import com.jhj.common.ConstantOa;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.university.OaPartnerServiceTypeVo;
import com.meijia.utils.BeanUtilsExp;

/**
 *
 * @author :hulj
 * @Date : 2015年12月2日下午3:43:44
 * @Description: 
 *		
 *		叮当大学-- 服务类别
 *
 */
@Controller
@RequestMapping(value = "/university")
public class PartnerServiceTypeController extends BaseController {

	@Autowired
	private PartnerServiceTypeService partService;
	
	@RequestMapping(value = "/partner_list",method = RequestMethod.GET)
	public String partnerList(Model model,HttpServletRequest request){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		
		List<PartnerServiceType> list = partService.selectByListPage(pageNo, pageSize);
		
		PartnerServiceType partner = null;
		
		for (int i = 0; i < list.size(); i++) {
			partner = list.get(i);
			
			OaPartnerServiceTypeVo completeVo = partService.completeVo(partner);
			
			list.set(i, completeVo);
		}
		
		PageInfo result = new PageInfo(list);	
		
		model.addAttribute("partnerServiceTypeListModel", result);
		
		return "university/partnerList";
	}
	
	/*
	 * 跳转到 form页
	 */
	@RequestMapping(value = "/partner_type_form",method = RequestMethod.GET)
	public String degreeTypeForm(HttpServletRequest request, Model model,
					@RequestParam("serviceTypeId") Long id){
								
		if(id == null){
			id = 0L;
		}
		
		PartnerServiceType partner = partService.initPartner();
		
		if( id !=null && id >0L){
			partner = partService.selectByPrimaryKey(id);
		}
		
		
		model.addAttribute("partnerFormModel", partner);
		
		return "university/partnerForm";
	}
	
	/*
	 * 提交form
	 */
	@RequestMapping(value = "/partner_type_form", method = RequestMethod.POST)
	public String submitDegreeForm(
			@ModelAttribute("partnerFormModel") PartnerServiceType partnerServiceType,
			@RequestParam("flagServiceTypeId") Long flagServiceTypeId,
			BindingResult result){
		
		PartnerServiceType partner = partService.initPartner();
		
		if(flagServiceTypeId == 0L){
			
			BeanUtilsExp.copyPropertiesIgnoreNull(partnerServiceType, partner);
			
			//真正的serviceTypeId
			partner.setServiceTypeId(flagServiceTypeId);
			//下拉选择标签的 name 为 serviceTypeId,实际代表 parentId
			partner.setParentId(partnerServiceType.getServiceTypeId());
			
			partService.insertSelective(partner);
			
		}else{
			
			partner = partService.selectByPrimaryKey(partnerServiceType.getServiceTypeId());
			
			BeanUtilsExp.copyPropertiesIgnoreNull(partnerServiceType, partner);
			
			//真正的serviceTypeId
			partner.setServiceTypeId(flagServiceTypeId);
			//下拉选择标签的 name 为 serviceTypeId,实际代表 parentId
			partner.setParentId(partnerServiceType.getServiceTypeId());
			
			partService.updateByPrimaryKeySelective(partner);
			
		}
		
		return "redirect:partner_list";
	}
	
}
