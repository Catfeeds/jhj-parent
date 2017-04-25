package com.jhj.action.app.period;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.period.PeriodOrder;
import com.jhj.po.model.period.PeriodOrderAddons;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.period.PeriodOrderAddonsService;
import com.jhj.service.period.PeriodOrderService;
import com.jhj.service.users.UserAddrsService;
import com.meijia.utils.DateUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping("/app/period")
public class PeriodOrderController extends BaseController{
	
	@Autowired
	private PeriodOrderService periodOrderService;
	
	@Autowired
	private PeriodOrderAddonsService periodOrderAddonsService;
	
	@Autowired
	private UserAddrsService userAddrService;
	
	@RequestMapping(value="/save-period-order.json",method=RequestMethod.POST)
	public AppResultData<Object> save(
			@RequestParam(value="user_id") Integer userId,
			@RequestParam(value="mobile") String mobile,
			@RequestParam(value="addr_id") Long addrId,
			@RequestParam(value="order_type") Integer orderType,
			@RequestParam(value="order_status") Integer orderStatus,
			@RequestParam(value="order_money") Double orderMoney,
			@RequestParam(value="order_price") Double orderPay,
			@RequestParam(value="user_coupons_id",defaultValue="0",required=false) Integer userCouponsId,
			@RequestParam(value="period_service_type_id") Integer periodServiceTypeId,
			@RequestParam(value="order_from",defaultValue="1") Integer orderFrom,
			@RequestParam(value="remarks",required=false,defaultValue="") String remarks,
			@RequestParam(value="period_service_addons_json") String periodServiceAddonsJson){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		if(addrId==null || addrId<=0){
			result.setStatus(Constants.ERROR_999);
			result.setMsg("请选择服务地址");
			return result;
		}
		
//		UserAddrs userAddrs = userAddrService.selectByPrimaryKey(addrId);
//		if(userAddrs==null){
//			result.setStatus(Constants.ERROR_999);
//			result.setMsg("您选择的服务地址不存在！");
//			return result;
//		}
		
		PeriodOrder init = periodOrderService.init();
		init.setUserId(userId);
		init.setMobile(mobile);
		init.setAddrId(addrId.intValue());
		init.setOrderType(orderType);
		init.setOrderStatus(orderStatus);
		init.setOrderMoney(new BigDecimal(orderMoney));
		init.setOrderPrice(new BigDecimal(orderPay));
		init.setUserCouponsId(userCouponsId);
		init.setPeriodServiceTypeId(periodServiceTypeId);
		init.setOrderFrom(orderFrom);
		init.setRemarks(remarks);
		periodOrderService.insert(init);
		
		PeriodOrder periodOrder = periodOrderService.selectByOrderNo(init.getOrderNo());
		
		
		List<PeriodOrderAddons> periodOrderAddonsList = JSON.parseArray(periodServiceAddonsJson, PeriodOrderAddons.class);
		if(periodOrderAddonsList!=null && periodOrderAddonsList.size()>0){
			for(int i=0;i<periodOrderAddonsList.size();i++){
				PeriodOrderAddons periodOrderAddons = periodOrderAddonsList.get(i);
				
				periodOrderAddons.setPeriodOrderNo(init.getOrderNo());
				periodOrderAddons.setPeriodOrderId(periodOrder.getId());
				periodOrderAddons.setUserId(userId);
				periodOrderAddons.setAddTime(DateUtil.getNowOfDate());
				
				periodOrderAddonsList.set(i, periodOrderAddons);
			}
			
			periodOrderAddonsService.insertBatch(periodOrderAddonsList);
		}
		
		
		return result;
	}


}