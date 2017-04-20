package com.jhj.action.app.period;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.period.PeriodServiceType;
import com.jhj.service.period.PeriodServiceTypeService;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping("/app/period")
public class PeriodServiceTypeCotroller {
	
	@Autowired
	private PeriodServiceTypeService periodServiceTypeService;
	
	@RequestMapping(value = "/get-periodServiceType-list.json",method=RequestMethod.GET)
	public AppResultData<Object> getPeriodServiceTypeList(
			@RequestParam("packageTypeId") String packageTypeId){
		
		PeriodServiceType periodServiceType = new PeriodServiceType();
		periodServiceType.setPackageType(packageTypeId);
		List<PeriodServiceType> list = periodServiceTypeService.getList(periodServiceType);
		
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, list);
		
		return result;
		
	}
	
	
}
