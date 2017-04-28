package com.jhj.action.app.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.service.order.OrderDispatchsService;
import com.meijia.utils.vo.AppResultData;


@Controller
@RequestMapping(value = "/app/order")
public class OrderDisptachController extends BaseController {
	@Autowired
	private OrderDispatchsService orderDispatchsService;
	
	@RequestMapping(value = "check_dispatch", method = RequestMethod.POST)
	public AppResultData<Object> list(
			@RequestParam("service_type_id") Long serviceTypeId,
			@RequestParam("service_date_str") String serviceDateStr,
			@RequestParam("addr_id") Long addrId,
			@RequestParam("staff_id") Long staffId
			) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		if (staffId.equals(0L)) {
			datas = orderDispatchsService.checkDispatchedDay(serviceTypeId, serviceDateStr, addrId);
		}
		
		if (staffId > 0L) {
			datas = orderDispatchsService.checkDispatchedDayByStaffId(serviceTypeId, serviceDateStr, addrId, staffId);
		}
		
		result.setData(datas);
		
		return result;
	}
	

}
