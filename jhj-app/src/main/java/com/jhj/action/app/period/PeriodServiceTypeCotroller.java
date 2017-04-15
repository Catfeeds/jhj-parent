package com.jhj.action.app.period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.period.PeriodServiceType;
import com.jhj.service.period.PeriodServiceTypeService;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping("/period")
public class PeriodServiceTypeCotroller {
	
	@Autowired
	private PeriodServiceTypeService periodServiceTypeService;
	
	@RequestMapping(value = "/get-periodServiceType-list.json",method=RequestMethod.GET)
	public AppResultData<Object> getPeriodServiceTypeList(
			@RequestParam("packageTypeId") String packageTypeId,
			@RequestParam("pageNo") Integer pageNo){
		
		PeriodServiceType periodServiceType = new PeriodServiceType();
		periodServiceType.setPackageType(packageTypeId);
		PageInfo listPage = periodServiceTypeService.getListPage(periodServiceType,pageNo,Constants.PAGE_MAX_NUMBER);
		
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, listPage);
		
		return result;
		
	}
	
	
}
