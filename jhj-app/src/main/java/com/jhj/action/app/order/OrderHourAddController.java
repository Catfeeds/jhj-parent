package com.jhj.action.app.order;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.jhj.po.model.order.OrderAppoint;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.Users;
import com.jhj.service.ValidateService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.ServiceAddonsService;
import com.jhj.service.order.OrderAppointService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderHourAddService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.vo.AppResultData;
import com.jhj.vo.ServiceAddonSearchVo;
import com.jhj.vo.order.OrderHourAddVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.Week;

/**
 *
 * @author :hulj
 * @Date : 2015年7月20日下午5:31:54
 * @Description: （钟点工）保洁类-订单提交接口 
 *
 */
@Controller
@RequestMapping(value = "/app/order")
public class OrderHourAddController extends BaseController {
	@Autowired
	private UsersService userService;
	
	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private OrderPricesService orderPricesService;
	
	@Autowired
	private OrderDispatchsService orderDispatchService;
	
	@Autowired
	private OrgStaffsService orgStaService;
	
	@Autowired
	private OrderHourAddService orderHourAddservice;
	
	@Autowired
	private OrderLogService orderLogService;
	
	@Autowired
	private UserAddrsService userAddrService;
	
	@Autowired
	private UserCouponsService userCoupService;
	
	@Autowired
	private UserRefAmService userRefAmService;	
	
	@Autowired
	private ServiceAddonsService dictServiceAddonService;
	
    @Autowired
    private  OrderServiceAddonsService  orderServiceAddonsService;	
    
    @Autowired
    private ServiceAddonsService serviceAddonsService;       
	
    @Autowired
    private PartnerServiceTypeService partService;
    
    @Autowired
    private OrderAppointService orderAppointService;
    
    @Autowired
	private ValidateService validateService;	
    
	@RequestMapping(value = "post_hour", method = RequestMethod.POST)
	public AppResultData<Object> submitOrder(
			@RequestParam("userId") Long userId,
			@RequestParam("serviceType") Long serviceType,
			@RequestParam(value = "serviceContent",required =false,defaultValue = "") String serviceContent,
			@RequestParam("serviceDate") Long serviceDate,
			@RequestParam(value = "addrId", required = false, defaultValue = "") String addrIdStr,
			@RequestParam("serviceHour") Short serviceHour,
			@RequestParam(value = "staffNums", required = false, defaultValue = "1") int staffNums,
			@RequestParam(value = "serviceAddons", required = false, defaultValue = "") String serviceAddons,
			@RequestParam(value = "remarks", required = false, defaultValue = "") String remarks,
			@RequestParam(value = "orderFrom", required = false, defaultValue = "1") Short orderFrom,
			@RequestParam(value = "orderPay", required = false, defaultValue = "0") BigDecimal orderPay,
			@RequestParam(value = "orderOpFrom", required = false, defaultValue = "0") Long orderOpFrom,
			@RequestParam(value = "staff_id", required = false, defaultValue = "0") Long staffId) throws Exception{
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		AppResultData<Object> v = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		v = validateService.validateUser(userId);
		if (v.getStatus() == Constants.ERROR_999) {
			return v;
		}
		
		Long addrId = 0L;
		if (addrIdStr.equals("undefined")) {
			//找出该用户是否有默认的地址，有则为他
			List<UserAddrs> userAddrs = userAddrService.selectByUserId(userId);
			if (!userAddrs.isEmpty() && userAddrs.size() == 1) {
				UserAddrs userAddr = userAddrs.get(0);
				addrId = userAddr.getId();
			}
		} else if (!StringUtil.isEmpty(addrIdStr)) {
			addrId = Long.valueOf(addrIdStr);
		}
				
		//判断用户地址是否存在
		v = validateService.validateUserAddr(userId, addrId);
		if (v.getStatus() == Constants.ERROR_999) {
			return v;
		}
		
		//判断服务时间不能为过去时间
		v = validateService.validateServiceDate(serviceDate);
		if (v.getStatus() == Constants.ERROR_999) {
			return v;
		}
		
		//如果为特惠日，需要判断是否为周一到周三.
		if (serviceType.equals(69L) || serviceType.equals(70L)) {
			String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDate * 1000);
			Date sDate = DateUtil.parse(serviceDateStr);
			Week sWeek = DateUtil.getWeek(sDate);
			if (sWeek.getNumber() < 1 || sWeek.getNumber() > 3) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg("只能限定周一到周三使用.");
				return result;
			}
		}
		
		Users u = userService.selectByPrimaryKey(userId);
		
		// 调用公共订单号类，生成唯一订单号
		String orderNo = String.valueOf(OrderNoUtil.genOrderNo());
				
		//保存订单信息 
		Orders order = ordersService.initOrders();
		order.setMobile(u.getMobile());
		order.setUserId(userId);
		order.setServiceType(serviceType);
		order.setServiceDate(serviceDate);
		order.setAddrId(addrId);
		order.setServiceHour(serviceHour);
		order.setStaffNums(staffNums);
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_1);//钟点工未支付
		order.setOrderNo(orderNo);
		order.setOrderFrom(orderFrom);
		order.setOrderOpFrom(orderOpFrom);
		
		PartnerServiceType partnerServiceType = partService.selectByPrimaryKey(serviceType);
		
		//订单表 。服务内容字段
		String serviceName = partnerServiceType.getName();
		
		//如果有附加服务类型，需要存储到order_service_addons表.
		if (!StringUtil.isEmpty(serviceAddons)) {
			String[] serviceAddonArray = StringUtil.convertStrToArray(serviceAddons);
			
			List<Long> serviceAddonIds = new ArrayList<Long>();
			for (int i = 0; i < serviceAddonArray.length; i++) {
				if (!StringUtil.isEmpty(serviceAddonArray[i])) {
					Long serviceAddonId = Long.valueOf(serviceAddonArray[i]);
					serviceAddonIds.add(serviceAddonId);
				}
			}
			
			ServiceAddonSearchVo searchVo1 = new ServiceAddonSearchVo();
			searchVo1.setServiceAddonIds(serviceAddonIds);
			List<DictServiceAddons> dictServiceAddons = serviceAddonsService.selectBySearchVo(searchVo1);
			
			for (int i = 0; i < serviceAddonArray.length; i++) {	
				Long serviceAddonId = Long.valueOf(serviceAddonArray[i]);
				
				OrderServiceAddons record = orderServiceAddonsService.initOrderServiceAddons();
				
				record.setOrderId(order.getId());
				record.setOrderNo(order.getOrderNo());
				record.setServiceAddonId(serviceAddonId);
				record.setUserId(userId);
				
				
				for (DictServiceAddons item : dictServiceAddons) {
					if (item.getServiceAddonId().equals(serviceAddonId)) {
						record.setItemNum(item.getDefaultNum());
						record.setItemUnit(item.getItemUnit());
						record.setPrice(item.getDisPrice());
					}
					serviceName += item.getName() +" ";
				}
				orderServiceAddonsService.insert(record);
				order.setServiceContent(serviceName);
			}
		}else{
			order.setServiceContent(serviceName);
		}
		
		order.setRemarks(remarks);
		
		ordersService.insert(order);
		
		//设置订单总金额。插入 order_prices表
		
		OrderPrices orderPrices = orderHourAddservice.getNewOrderPrice(order, serviceType);
		
		if (orderFrom.equals((short)2) && orderPay.compareTo(new BigDecimal(0)) == 1) {

			orderPrices.setOrderMoney(orderPay);
			orderPrices.setOrderPay(orderPay);
		}
		
		orderPrices.setUserId(userId);
		orderPrices.setOrderId(order.getId());
		orderPrices.setMobile(u.getMobile());
		orderPrices.setOrderNo(orderNo);
		
		orderPricesService.insert(orderPrices);
		
		//如果有指定的员工，则要插入指定员工表.
		if (!staffId.equals(0L)) {
			OrderAppoint orderAppoint =  orderAppointService.initOrderAppoint();
			orderAppoint.setOrderId(order.getId());
			orderAppoint.setUserId(userId);
			orderAppoint.setStaffId(staffId);
			orderAppointService.insert(orderAppoint);
		}
		
		/*
		 * 2.插入订单日志表  order_log
		 */
		OrderLog orderLog = orderLogService.initOrderLog(order);
		
		orderLogService.insert(orderLog);
		
		result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, order);
		
		return result;
	}
}
