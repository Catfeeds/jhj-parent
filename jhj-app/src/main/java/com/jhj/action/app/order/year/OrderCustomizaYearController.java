package com.jhj.action.app.order.year;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.Constants;
import com.jhj.po.model.order.OrderCustomizationYear;
import com.jhj.service.orderYear.OrderCustomizYearService;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2016年4月2日上午11:07:09
 * @Description: 
 *		 
 *     全年订制 服务  提交 
 *			
 */
@Controller
@RequestMapping(value = "/app/order")
public class OrderCustomizaYearController extends BaseController {
	
	@Autowired
	private OrderCustomizYearService cusService;
	
	/*
	 * 提交 全年订制服务 
	 */
	@RequestMapping(value = "order_customiza_year.json",method = RequestMethod.GET)
	public AppResultData<Object> submitYearOrder(
			@RequestParam(value = "user_id")Long userId,
			@RequestParam(value = "service_type_id")Long serviceTypeId){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		if(userId == null || serviceTypeId == null){
			result.setMsg("参数不合法,请稍后重试");
			result.setStatus(Constants.ERROR_999);
			
			return result;
		}
		
		OrderCustomizationYear year = cusService.initCusYear();
		
		year.setUserId(userId);
		year.setServiceTypeId(serviceTypeId);
		
		cusService.insert(year);
			
		result.setMsg("全年订制服务,预约成功");
		
		return result;		
	}
	
}
