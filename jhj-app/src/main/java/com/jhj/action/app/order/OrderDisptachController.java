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
import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.users.UserAddrsService;
import com.meijia.utils.DateUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;


@Controller
@RequestMapping(value = "/app/order")
public class OrderDisptachController extends BaseController {
	@Autowired
	private OrderDispatchsService orderDispatchsService;
	
	@Autowired
	private UserAddrsService userAddrService;
	
	@RequestMapping(value = "check_dispatch", method = RequestMethod.POST)
	public AppResultData<Object> list(
			@RequestParam("service_type_id") Long serviceTypeId,
			@RequestParam("service_date_str") String serviceDateStr,
			@RequestParam("addr_id") Long addrId,
			@RequestParam(value = "staff_id", required = false, defaultValue = "0") Long staffId
			) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		if (addrId.equals(0L)) return result;
		
		UserAddrs addrs = userAddrService.selectByPrimaryKey(addrId);
		String lat = addrs.getLatitude();
		String lng = addrs.getLongitude();
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		if (staffId.equals(0L)) {
			String today = DateUtil.getToday();
			if (serviceDateStr.equals(today)) {
				datas = orderDispatchsService.checkDispatchedToday(serviceTypeId, serviceDateStr, lat, lng);
			} else {
				datas = orderDispatchsService.checkDispatchedNotToday(serviceTypeId, serviceDateStr, lat, lng);
			}
		}
		
		if (staffId > 0L) {
			datas = orderDispatchsService.checkDispatchedDayByStaffId(serviceTypeId, serviceDateStr, staffId, lat, lng);
		}
		
		result.setData(datas);
		
		return result;
	}
	
	@RequestMapping(value = "check_dispatch_by_poi", method = RequestMethod.POST)
	public AppResultData<Object> checkDispatchByPoi(
			@RequestParam("service_type_id") Long serviceTypeId,
			@RequestParam("service_date_str") String serviceDateStr,
			@RequestParam("lat") String lat,
			@RequestParam("lng") String lng,
			@RequestParam(value = "staff_id", required = false, defaultValue = "0") Long staffId
			) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		if (staffId.equals(0L)) {
			
			String today = DateUtil.getToday();
			if (serviceDateStr.equals(today)) {
				datas = orderDispatchsService.checkDispatchedToday(serviceTypeId, serviceDateStr, lat, lng);
			} else {
				datas = orderDispatchsService.checkDispatchedNotToday(serviceTypeId, serviceDateStr, lat, lng);
			}
		}
		
		if (staffId > 0L) {
			datas = orderDispatchsService.checkDispatchedDayByStaffId(serviceTypeId, serviceDateStr, staffId, lat, lng);
		}
		
		result.setData(datas);
		
		return result;
	}
	
	
	@RequestMapping(value = "get_someday_dispatched", method = RequestMethod.GET) 
	public AppResultData<Object> getSomeDayDispathed(
			@RequestParam("service_date_str") String serviceDateStr) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
	
		return result;
	}

}
