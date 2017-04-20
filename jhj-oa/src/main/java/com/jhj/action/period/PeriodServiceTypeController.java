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
import com.jhj.oa.auth.AuthPassport;
import com.jhj.oa.vo.PeriodServiceTypeVo;
import com.jhj.po.model.period.PeriodServiceType;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.period.PeriodServiceTypeService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.PartnerServiceTypeVo;
import com.meijia.utils.BeanUtilsExp;

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

	@AuthPassport
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,@RequestParam(value="id",required=false) Integer id) {
		PeriodServiceTypeVo periodServiceTypeVo = new PeriodServiceTypeVo();
		if(id!=null){
			PeriodServiceType periodServiceType = periodServiceTypeService.selectByPrimaryKey(id);
			BeanUtilsExp.copyPropertiesIgnoreNull(periodServiceType, periodServiceTypeVo);
			String packageType = periodServiceType.getPackageType();
			if(packageType!=null && !"".equals(packageType)){
				String[] split = packageType.split(",");
				periodServiceTypeVo.setPackageTypeList(Arrays.asList(split));
			}
		}
		model.addAttribute("periodServiceTypeVo", periodServiceTypeVo);
		
		PartnerServiceTypeVo vo = new PartnerServiceTypeVo();
		Long[] parentServiceTypeId = {23L,24L,25L,26L,27L,74L};
		vo.setParentServiceTypeId(Arrays.asList(parentServiceTypeId));
		vo.setEnable((short)1);
		List<PartnerServiceType> partnerServiceTypeList = partnerServiceTypeService.selectByPartnerServiceTypeVo(vo);
		model.addAttribute("partnerServiceTypeList", partnerServiceTypeList);
		
		return "period/periodServiceType";
	}

	@AuthPassport
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute PeriodServiceTypeVo periodServiceTypeVo) {
		
		if(periodServiceTypeVo.getId()!=null && periodServiceTypeVo.getId()>0){
			PeriodServiceType pt = periodServiceTypeService.selectByPrimaryKey(periodServiceTypeVo.getId());
			pt.setName(periodServiceTypeVo.getName());
			pt.setNum(periodServiceTypeVo.getNum());
			pt.setPrice(periodServiceTypeVo.getPrice());
			pt.setPunit(periodServiceTypeVo.getPunit());
			pt.setRemarks(periodServiceTypeVo.getRemarks());
			pt.setServiceAddonId(periodServiceTypeVo.getServiceAddonId());
			pt.setServiceTypeId(periodServiceTypeVo.getServiceTypeId());
			pt.setTotal(periodServiceTypeVo.getTotal());
			pt.setVipPrice(periodServiceTypeVo.getVipPrice());
			pt.setEnbale(periodServiceTypeVo.getEnbale());
			List<String> packageTypeList = periodServiceTypeVo.getPackageTypeList();
			String replace = packageTypeList.toString().replace(" ", "").replace("[", "").replace("]", "");
			pt.setPackageType(replace);
			periodServiceTypeService.updateByPrimaryKeySelective(pt);
		}else{
			PeriodServiceType init = periodServiceTypeService.init();
			init.setName(periodServiceTypeVo.getName());
			init.setNum(periodServiceTypeVo.getNum());
			init.setPrice(periodServiceTypeVo.getPrice());
			init.setPunit(periodServiceTypeVo.getPunit());
			init.setRemarks(periodServiceTypeVo.getRemarks());
			if(periodServiceTypeVo.getServiceAddonId()!=null){
				init.setServiceAddonId(periodServiceTypeVo.getServiceAddonId());
			}else{
				init.setServiceAddonId(0);
			}
			
			init.setServiceTypeId(periodServiceTypeVo.getServiceTypeId());
			init.setTotal(periodServiceTypeVo.getTotal());
			init.setVipPrice(periodServiceTypeVo.getVipPrice());
			init.setEnbale(periodServiceTypeVo.getEnbale());
			List<String> packageTypeList = periodServiceTypeVo.getPackageTypeList();
			String replace = packageTypeList.toString().replace(" ", "").replace("[", "").replace("]", "");
			init.setPackageType(replace);
			periodServiceTypeService.insertSelective(init);
		}
		return "redirect:getList";
	}

	@AuthPassport
	@RequestMapping(value = "/getServiceType/{id}",method = RequestMethod.GET)
	public String selectByPrimaryKey(Model model,@PathVariable("id") Integer id) {
		PeriodServiceType periodServiceType = periodServiceTypeService.selectByPrimaryKey(id);
		model.addAttribute("periodServiceType",periodServiceType);
		return "period/periodServiceType";
	}
	
	@AuthPassport
	@RequestMapping(value = "/getList",method = {RequestMethod.GET,RequestMethod.POST})
	public String getList(Model model, HttpServletRequest request, PeriodServiceType periodServiceType) {
		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		PageInfo<PeriodServiceType> listPage = periodServiceTypeService.getListPage(periodServiceType, pageNo, Constants.PAGE_MAX_NUMBER);
		model.addAttribute("page", listPage);
		
		return "period/periodServiceTypeList";
	}
    
}