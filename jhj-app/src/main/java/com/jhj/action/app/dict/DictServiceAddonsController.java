package com.jhj.action.app.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.service.dict.ServiceAddonsService;
import com.jhj.vo.ServiceAddonSearchVo;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping("/app/dictServiceAddons")
public class DictServiceAddonsController extends BaseController{
	
	@Autowired
	private ServiceAddonsService addonsService;
	
	@RequestMapping(value="get_service_type",method=RequestMethod.GET)
	public AppResultData<Object> selectByServiceTypeId(@RequestParam("service_type_id") Long serviceTypeId){
		
		ServiceAddonSearchVo serviceAddonVo=new ServiceAddonSearchVo();
		serviceAddonVo.setServiceType(serviceTypeId);
		List<DictServiceAddons> serviceAddonsList = addonsService.selectBySearchVo(serviceAddonVo);
		
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, serviceAddonsList);
		return result;
	}
}
