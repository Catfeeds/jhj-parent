package com.jhj.action.app.order;


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
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.ServiceAddonsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderHourAddService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.vo.AppResultData;
import com.jhj.vo.order.OrderHourAddVo;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.StringUtil;

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
	
	@RequestMapping(value = "post_hour", method = RequestMethod.POST)
	public AppResultData<Object> submitOrder(
			@RequestParam("userId") Long userId,
			@RequestParam("serviceType") Long serviceType,
			@RequestParam("serviceContent") String serviceContent,
			@RequestParam("serviceDate") Long serviceDate,
			@RequestParam("addrId") Long addrId,
			@RequestParam("serviceHour") Short serviceHour,
			@RequestParam(value = "serviceAddons", required = false, defaultValue = "") String serviceAddons,
			@RequestParam(value = "remarks", required = false, defaultValue = "") String remarks,
			@RequestParam(value = "orderFrom", required = false, defaultValue = "1") Short orderFrom) throws Exception{
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		//当前时间 的时间戳
		Long nowTimeStamp =  new Date().getTime()/1000;
		
		if(nowTimeStamp >= serviceDate){
			
			result.setStatus(Constants.ERROR_999);
			
			result.setMsg(ConstantMsg.OLD_TIME);
			return result;
		}
		
		Users u = userService.getUserById(userId);

		// 判断是否为注册用户，非注册用户返回 999
		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}
		
		UserRefAm userRefAm = userRefAmService.selectByAmId(userId);
		// 调用公共订单号类，生成唯一订单号
		String orderNo = String.valueOf(OrderNoUtil.genOrderNo());
				
		//保存订单信息 
		Orders order = ordersService.initOrders();
		order.setMobile(u.getMobile());
		order.setUserId(userId);
		order.setAmId(userRefAm.getStaffId());
		order.setServiceType(serviceType);
//		order.setServiceContent(serviceContent);
		order.setServiceDate(serviceDate);
		order.setAddrId(addrId);
		order.setServiceHour(serviceHour);
		order.setOrderStatus(Constants.ORDER_HOUR_STATUS_1);//钟点工未支付
		order.setOrderNo(orderNo);
		
		order.setRemarks(remarks);
		
		OrgStaffs staffs = orgStaService.selectOrgIdByStaffId(userRefAm.getStaffId());
		
		order.setOrgId(staffs.getOrgId());
		
		
		//mybatis xml 需要增加插入后获取last_insert_id;
		ordersService.insert(order);
		
		//设置订单总金额。插入 order_prices表
		
		OrderPrices orderPrices =  orderHourAddservice.getOrderPriceSum(serviceType, serviceAddons, serviceHour);
		orderPrices.setUserId(userId);
		orderPrices.setOrderId(order.getId());
		orderPrices.setMobile(u.getMobile());
		orderPrices.setOrderNo(orderNo);
		
		orderPricesService.insert(orderPrices);
		
	
		
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
			List<DictServiceAddons> dictServiceAddons = serviceAddonsService.selectByServiceAddonIds(serviceAddonIds);
			
			
			
			for (int i = 0; i < serviceAddonArray.length; i++) {	
				Long serviceAddonId = Long.valueOf(serviceAddonArray[i]);
				
				OrderServiceAddons record = orderServiceAddonsService.initOrderServiceAddons();
				
				record.setOrderId(order.getId());
				record.setOrderNo(order.getOrderNo());
				record.setServiceAddonId(serviceAddonId);
				record.setUserId(userId);
				
				//订单表 。服务内容字段
				String serviceContents = "保洁  ";
				
				
				for (DictServiceAddons item : dictServiceAddons) {
					if (item.getServiceAddonId().equals(serviceAddonId)) {
						record.setItemNum(item.getDefaultNum());
						record.setItemUnit(item.getItemUnit());
						record.setPrice(item.getPrice());
					}
					
					serviceContents += item.getName() +" ";
				}
				
				
				orderServiceAddonsService.insert(record);
				order.setServiceContent(serviceContents);
			}
		}else{
			order.setServiceContent("保洁");
		}
		ordersService.updateByPrimaryKeySelective(order);
		

		/*
		 * 2.插入订单日志表  order_log
		 */
		OrderLog orderLog = orderLogService.initOrderLog(order);
		
		orderLogService.insert(orderLog);
		
		result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, order);
		
		return result;
	}
	/*
	 * 
	 * 用户版-钟点工 --提交订单 页面加载，调用这个接口
	 */
	@RequestMapping(value = "get_some_list.json" ,method = RequestMethod.GET)
	public AppResultData<Object> getReleativeList(
			@RequestParam(value = "userId") Long userId){

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		OrderHourAddVo orderHourAddVo = new OrderHourAddVo();
		
		//用户地址列表
		List<UserAddrs> userAddrList = userAddrService.selectByUserId(userId);
		if(userAddrList!=null){
			orderHourAddVo.setUserAddrList(userAddrList);
		}else{
			orderHourAddVo.setUserAddrList(new ArrayList<UserAddrs>());
		}
		
		//附加服务列表，页面已经确定。。，即 钟点工
		
		List<DictServiceAddons> dictList = dictServiceAddonService.selectAllHourAddons();
		
		orderHourAddVo.setDictServiceAddonList(dictList);
		
		result.setData(orderHourAddVo);
		
		return result;
		
	}
	
}
