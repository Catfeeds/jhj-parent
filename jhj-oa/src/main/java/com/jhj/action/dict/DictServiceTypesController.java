package com.jhj.action.dict;

import java.math.BigDecimal;
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

import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.Gifts;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.dict.DictServiceTypes;
import com.jhj.service.bs.GiftsService;
import com.jhj.service.dict.ServiceTypeService;
import com.meijia.utils.TimeStampUtil;

@Controller
@RequestMapping(value = "/base")
public class DictServiceTypesController extends BaseController {

	@Autowired
    private ServiceTypeService serviceTypeService;
	
	@Autowired
	private GiftsService giftService;	


	/**列表显示发布的优惠券
	 * @param request
	 * @param model
	 * @param searchVo
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/service-list", method = { RequestMethod.GET })
	public String list(HttpServletRequest request, Model model
		) {

		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());

		model.addAttribute("searchModel");

		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);

		PageInfo result=serviceTypeService.searchVoListPage( pageNo, pageSize);

		model.addAttribute("contentModel",result);

		return "dict/list";
	}
	
	
	
	

	/**
	 * 服务类型表单方法，显示
	 * @param model
	 * @param id
	 * @param request
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/serviceTypeForm", method = { RequestMethod.GET })
	public String serviceTypeForm(Model model,
			@RequestParam(value = "id") Long id,
			HttpServletRequest request) {

		if (id == null) {
			id = 0L;
		}

		DictServiceTypes serviceType = serviceTypeService.initServiceType();
		if (id != null && id > 0) {
			serviceType = serviceTypeService.selectByPrimaryKey(id);
		}

		model.addAttribute("serviceTypeModel", serviceType);

		return "dict/serviceTypeForm";
	}

	
	/**
	 * 服务类型保存数据方法.
	 *
	 * @param request
	 * @param model
	 * @param serviceType
	 * @param result
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/serviceTypeForm", method = { RequestMethod.POST })
	public String doServiceTypeForm(HttpServletRequest request, Model model,
			@ModelAttribute("serviceType") DictServiceTypes serviceType,
			BindingResult result) {

		Long id = Long.valueOf(request.getParameter("id"));

		//更新或者新增
		if (id != null && id > 0) {
			serviceType.setUpdateTime(TimeStampUtil.getNow() / 1000);
			serviceTypeService.updateByPrimaryKeySelective(serviceType);
		} else {
			serviceType.setId(Long.valueOf(request.getParameter("id")));
			serviceType.setAddTime(TimeStampUtil.getNow()/1000);
			serviceType.setUpdateTime(0L);
	        serviceTypeService.insertSelective(serviceType);
		}



		return "redirect:service-list";
	}
	/**
	 * 充值卡列表展示
	 * @param request
	 * @param model
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "card-list",method = { RequestMethod.GET})
	public String cardList(HttpServletRequest request, Model model){
		
		model.addAttribute("requestUrl",request.getServletPath());
		model.addAttribute("requestQuery",request.getQueryString());
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME,ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		PageInfo result=serviceTypeService.selectAll(pageNo,pageSize);
		
		model.addAttribute("contentModel",result);
		
		return "dict/cardList";
	}
	/**
	 * 充值卡表单显示
	 * @param model
	 * @param id
	 * @param request
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/cardForm",method = { RequestMethod.GET })
	public String cardTypeForm(Model model,
			@RequestParam(value = "id") Long id,
			HttpServletRequest request){
		
		if (id == null) {
			id = 0L;
		}
		DictCardType dictCardType = serviceTypeService.initCardType();
		if (id !=null && id > 0) {
			dictCardType = serviceTypeService.selectById(id);
		}
		
		//礼包列表
		List<Gifts> gifts = giftService.selectAll();
		
		model.addAttribute("gifts",gifts);
		model.addAttribute("contentModel",dictCardType);
		
		return "dict/cardForm";
	}
	/**
	 * 充值卡保存数据
	 * @param request
	 * @param model
	 * @param cardType
	 * @param result
	 * @return
	 */
	
	@AuthPassport
	@RequestMapping(value = "/cardForm", method = { RequestMethod.POST })
	public String doCardForm(HttpServletRequest request, Model model,
			@ModelAttribute("cardType") DictCardType cardType,
			BindingResult result) {

		Long id = Long.valueOf(request.getParameter("id"));
		
		Long selectGiftId = Long.valueOf(request.getParameter("selectGiftId"));
		cardType.setGiftId(selectGiftId);
		//更新或者新增
		if (id != null && id > 0) {
			
			serviceTypeService.updateByidSelective(cardType);
			
		} else {
			cardType.setId(Long.valueOf(request.getParameter("id")));
			cardType.setAddTime(TimeStampUtil.getNow()/1000);
	        serviceTypeService.insertSelect(cardType);
		}

		
		return "redirect:card-list";
	}
	/**
	 * 服务类型附加列表展示
	 * @param request
	 * @param model
	 * @param serviceAddons
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/serviceAdd-list", method = { RequestMethod.GET })
	public String doServiceTypeAdd(HttpServletRequest request, Model model,
			@ModelAttribute("serviceAddons") DictServiceAddons serviceAddons
			) {
			
			model.addAttribute("requestUrl",request.getServletPath());
			model.addAttribute("requestQuery",request.getQueryString());
			
			int pageNo = ServletRequestUtils.getIntParameter(request,
					ConstantOa.PAGE_NO_NAME,ConstantOa.DEFAULT_PAGE_NO);
			int pageSize = ServletRequestUtils.getIntParameter(request,
					ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
			
			Long id = Long.valueOf(request.getParameter("id"));
			
			PageInfo result=serviceTypeService.selectByServiceType(id,pageNo,pageSize);
			
        model.addAttribute("contentModel",result);
        model.addAttribute("serviceTypeId",id);
		
		return "dict/serviceAddList";
	}
	/**
	 * 服务类型附加展示表单
	 * @param model
	 * @param id
	 * @param request
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/serviceAddForm",method = { RequestMethod.GET })
	public String serviceAddForm(Model model,
			@RequestParam(value = "serviceAddonId") Long id,
			@RequestParam(value = "serviceType") Long ids,
			HttpServletRequest request){
		//Long ID = Long.valueOf(request.getParameter("serviceType"));
		if (id == null) {
			id = 0L;
		}
		DictServiceAddons dictServiceAddons = serviceTypeService.initServiceAdd();
		if (id !=null && id > 0) {
			dictServiceAddons = serviceTypeService.selectByAddId(id);
			
		}
		dictServiceAddons.setServiceType(ids);
		model.addAttribute("contentModel",dictServiceAddons);
		model.addAttribute("serviceType",dictServiceAddons.getServiceType());
		//model.addAttribute("contentModel",ID);
		
		return "dict/serviceAddForm";
	}
	
	@AuthPassport
	@RequestMapping(value = "/serviceAddForm", method = { RequestMethod.POST })
	public String serviceAddSave(HttpServletRequest request, Model model,
			@ModelAttribute("serviceAddons") DictServiceAddons serviceAddons,
			BindingResult result) {

		Long id = Long.valueOf(request.getParameter("serviceAddonId"));
		Long ids = Long.valueOf(request.getParameter("id"));

		serviceAddons.setKeyword("");
		serviceAddons.setTips("");
		serviceAddons.setDisPrice(new BigDecimal(0));
		serviceAddons.setAddTime(TimeStampUtil.getNow()/1000);
		serviceAddons.setUpdateTime(0L);
		//更新或者新增
		if (id != null && id > 0) {
			
			serviceTypeService.updateById(serviceAddons);
			
		}
		else {
	        serviceTypeService.insertServiceAddSelect(serviceAddons);
		}

		
		return "redirect:serviceAdd-list?id="+ids;
	}
	/**
	 * 根据id删除serviceAdd记录
	 */ 
	@RequestMapping(value = "/delete", method = { RequestMethod.GET })
	public String deleteAdminRole(HttpServletRequest request,Model model,
			 HttpServletRequest response,
			 @RequestParam(value="serviceAddonId") Long id) {
		//Long id = Long.valueOf(request.getParameter("serviceAddonId"));
		Long ids = Long.valueOf(request.getParameter("id"));

		int result = serviceTypeService.deleteById(id);
		
		return "redirect:serviceAdd-list?id="+ids;
	}
	
}
