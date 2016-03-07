package com.jhj.action.app.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.dict.DictServiceTypes;
import com.jhj.service.dict.ServiceTypeService;
import com.meijia.utils.vo.AppResultData;



@Controller
@RequestMapping(value = "/app/order")
public class OrderServiceTypeController extends BaseController {

	@Autowired
	private ServiceTypeService serviceTypeService;


	/*
	 * 获取服务类型
	 */
	@RequestMapping(value = "get_dictServiceType",method = RequestMethod.POST)
	public AppResultData<Object>  serviceTypeList(
			@RequestParam("serviceType_id") Long serviceTypeId){
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		DictServiceTypes serviceType = serviceTypeService.selectByPrimaryKey(serviceTypeId);
		
		result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, serviceType);
    

		
    	return result;
	}
	
}
