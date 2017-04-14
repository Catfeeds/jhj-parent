package com.jhj.action.app.period;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.period.PeriodOrder;
import com.jhj.service.period.PeriodOrderService;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping("/period")
public class PeriodOrderController extends BaseController{
	
	@Autowired
	private PeriodOrderService periodOrderService;
	
	@RequestMapping(value="/save-period-order.json",method=RequestMethod.POST)
	public AppResultData<Object> save(
			@RequestParam(value="user_id") Integer userId,
			@RequestParam(value="mobile") String mobile,
			@RequestParam(value="addr_id") Integer addrId,
			@RequestParam(value="order_type") Integer orderType,
			@RequestParam(value="order_status") Integer orderStatus,
			@RequestParam(value="order_money") Double orderMoney,
			@RequestParam(value="order_price") Double orderPay,
			@RequestParam(value="user_coupons_id",defaultValue="0") Integer userCouponsId,
			@RequestParam(value="service_type_id") Integer serviceTypeId,
			@RequestParam(value="order_from",defaultValue="1") Integer orderFrom,
			@RequestParam(value="remarks",required=false) String remarks){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		if(addrId==null || addrId<=0){
			result.setStatus(Constants.ERROR_999);
			result.setMsg("请选择服务地址");
			return result;
		}
		
		PeriodOrder init = periodOrderService.init();
		init.setUserId(userId);
		init.setMobile(mobile);
		init.setAddrId(addrId);
		init.setOrderType(orderType);
		init.setOrderStatus(orderStatus);
		init.setOrderMoney(new BigDecimal(orderMoney));
		init.setOrderPrice(new BigDecimal(orderPay));
		init.setUserCouponsId(userCouponsId);
		init.setServiceTypeId(serviceTypeId);
		init.setOrderFrom(orderFrom);
		init.setRemarks(remarks);
		periodOrderService.insert(init);
		
		return result;
	}


}