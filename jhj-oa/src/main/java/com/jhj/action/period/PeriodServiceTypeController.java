package com.jhj.action.period;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.po.model.period.PeriodServiceType;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.period.PeriodServiceTypeService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.PartnerServiceTypeVo;

@Controller
@RequestMapping("/period")
public class PeriodServiceTypeController extends BaseController{
	
	@Autowired
	private PeriodServiceTypeService periodServiceTypeService;
	
	@Autowired
	private PartnerServiceTypeService partnerServiceTypeService;

	@RequestMapping(value = "/deleteServiceType/{id}", method = RequestMethod.GET)
	public String deleteByPrimaryKey(@PathVariable("id") Integer id) {
		periodServiceTypeService.deleteByPrimaryKey(id);
		return "redirect:getList";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,@RequestParam(value="id",required=false) Integer id) {
		PeriodServiceType periodServiceType = null;
		if(id!=null){
			periodServiceType = periodServiceTypeService.selectByPrimaryKey(id);
		}else{
			periodServiceType = new PeriodServiceType();
		}
		model.addAttribute("periodServiceType", periodServiceType);
		
		PartnerServiceTypeVo vo = new PartnerServiceTypeVo();
		Long[] parentServiceTypeId = {23L,24L,25L,26L,27L,74L};
		vo.setParentServiceTypeId(Arrays.asList(parentServiceTypeId));
		vo.setEnable((short)1);
		List<PartnerServiceType> partnerServiceTypeList = partnerServiceTypeService.selectByPartnerServiceTypeVo(vo);
		model.addAttribute("partnerServiceTypeList", partnerServiceTypeList);
		
		return "period/periodServiceType";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute PeriodServiceType periodServiceType) {
		
		if(periodServiceType.getId()!=null && periodServiceType.getId()>0){
			PeriodServiceType pt = periodServiceTypeService.selectByPrimaryKey(periodServiceType.getId());
			pt.setName(periodServiceType.getName());
			pt.setNum(periodServiceType.getNum());
			pt.setPrice(periodServiceType.getPrice());
			pt.setPunit(periodServiceType.getPunit());
			pt.setRemarks(periodServiceType.getRemarks());
			pt.setServiceAddonId(periodServiceType.getServiceAddonId());
			pt.setServiceTypeId(periodServiceType.getServiceTypeId());
			pt.setTotal(periodServiceType.getTotal());
			pt.setVipPrice(periodServiceType.getVipPrice());
			pt.setEnbale(periodServiceType.getEnbale());
			periodServiceTypeService.updateByPrimaryKeySelective(pt);
		}else{
			PeriodServiceType init = periodServiceTypeService.init();
			init.setName(periodServiceType.getName());
			init.setNum(periodServiceType.getNum());
			init.setPrice(periodServiceType.getPrice());
			init.setPunit(periodServiceType.getPunit());
			init.setRemarks(periodServiceType.getRemarks());
			init.setServiceAddonId(periodServiceType.getServiceAddonId());
			init.setServiceTypeId(periodServiceType.getServiceTypeId());
			init.setTotal(periodServiceType.getTotal());
			init.setVipPrice(periodServiceType.getVipPrice());
			init.setEnbale(periodServiceType.getEnbale());
			periodServiceTypeService.insertSelective(init);
		}
		return "redirect:getList";
	}

	@RequestMapping(value = "/getServiceType/{id}",method = RequestMethod.GET)
	public String selectByPrimaryKey(Model model,@PathVariable("id") Integer id) {
		PeriodServiceType periodServiceType = periodServiceTypeService.selectByPrimaryKey(id);
		model.addAttribute("periodServiceType",periodServiceType);
		return "period/periodServiceType";
	}
	
	@RequestMapping(value = "/getList",method = {RequestMethod.GET,RequestMethod.POST})
	public String getList(Model model, HttpServletRequest request, PeriodServiceType periodServiceType) {
		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		PageInfo<PeriodServiceType> listPage = periodServiceTypeService.getListPage(periodServiceType, pageNo, Constants.PAGE_MAX_NUMBER);
		model.addAttribute("page", listPage);
		
		return "period/periodServiceTypeList";
	}
    
}