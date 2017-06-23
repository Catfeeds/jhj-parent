package com.jhj.action.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jhj.action.BaseController;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AccountAuth;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderExtend;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.cooperate.CooperateBusinessService;
import com.jhj.service.order.OaOrderService;
import com.jhj.service.order.OrderCancelService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderExtendService;
import com.jhj.service.order.OrderHourAddService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.PartnerServiceTypeVo;
import com.jhj.vo.dict.CooperativeBusinessSearchVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.meijia.utils.StringUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年8月10日下午6:12:56
 * @Description:
 *
 *               运营平台--订单管理模块
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController extends BaseController {
	@Autowired
	private OaOrderService oaOrderService;

	@Autowired
	private OrgStaffsService orgStaService;
	@Autowired
	private OrderPayService orderPayService;
	@Autowired
	private OrdersService orderService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private OrderPricesService priceService;

	@Autowired
	private PartnerServiceTypeService partService;
	@Autowired
	private OrgsService orgService;

	@Autowired
	private OrderDispatchsService orderDispatchsService;

	@Autowired
	private OrderQueryService orderQueryService;

	@Autowired
	private CooperateBusinessService cooperateBusinessService;

	@Autowired
	private PartnerServiceTypeService serviceType;

	@Autowired
	private OrderLogService orderLogService;
	
	@Autowired
	private OrderPricesService orderPriceService;
	
	@Autowired
	private OrderHourAddService orderHourAddservice;
	
	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;
	
	@Autowired
	private OrderCancelService orderCancelService;
	
	@Autowired
	private OrderExtendService orderExtendService;
	
	/*
	 * 订单换人操作
	 * 
	 * @param 订单用户Id, 订单Id, 订单（有过派工） 对应 的阿姨 Id
	 */
	@AuthPassport
	@RequestMapping(value = "/updateStaffByOrderNo", method = RequestMethod.POST)
	public String updateStaff(Long userId, Long id, Long staffId) {

		/*
		 * 订单在 order_dispatch中 派工状态 最多有 两种
		 * 
		 * 如果 前台 传参 staffId，未发生变化。。则 无后续 更新及发短信操作
		 */
		boolean isChange = true;

		// 如果 能 找到 派工 为 1 的 订单记录 , mybatis已修改。 selectByOrderId() 得到 的 是 有效派工 的记录
		// 。
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderId(id);
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDispatchs = orderDispatchsService
				.selectBySearchVo(searchVo);

		OrderDispatchs orderDispatch = null;
		if (!orderDispatchs.isEmpty()) {
			orderDispatch = orderDispatchs.get(0);
		}

		if (orderDispatch != null) {
			Long staId = orderDispatch.getStaffId();
			// 如果 没有 选择 新的 staffId ，或 这 staId 为空。。
			if (staId == staffId || staId == 0L) {
				isChange = false;
			}
			
			orderPayService.orderPaySuccessToDoForHour(userId, id, isChange);
		}

		return "redirect:/order/order-list";
	}

	/**
	 * 运营人员 为订单 添加 备注
	 */
	@AuthPassport
	@ResponseBody
	@RequestMapping(value = "update-remarks", method = RequestMethod.POST)
	public Map<String,String> updateRemarks(@RequestParam("order_no") String orderNo,
			@RequestParam(value="remarks",required=false) String remarks,
			HttpServletRequest request) {

		Orders orders = orderService.selectByOrderNo(orderNo);
		String oldRemarks = orders.getRemarks();

		orders.setRemarks(remarks);
		orderService.updateByPrimaryKeySelective(orders);
		if(oldRemarks.equals("") || !oldRemarks.equals(remarks)){
			AccountAuth accountAuth = AuthHelper.getSessionAccountAuth(request);
			OrderLog initOrderLog = orderLogService.initOrderLog(orders);
			initOrderLog.setAction(Constants.ORDER_ACTION_UPDATE);
			initOrderLog.setUserId(accountAuth.getId());
			initOrderLog.setUserName(accountAuth.getUsername());
			initOrderLog.setUserType((short)2);
			
			StringBuffer bf=new StringBuffer();
			StringBuffer append = bf.append(oldRemarks).append("修改成").append(remarks);
			initOrderLog.setRemarks(append.toString());
			orderLogService.insert(initOrderLog);
		}
		
		Map<String,String> map =new HashMap<String,String>();
		map.put("status", "0");
		
		return map;
		
	}

	/**
	 * 取消订单,订单状态为已支付和完成服务之间（不包括）
	 * 取消订单后，余额支付的，则退回，并记录消费明细
	 * 在线支付、现金支付和平台已支付的则不需要操作
	 * @param orderId
	 * 
	 * */
	@AuthPassport
	@ResponseBody
	@RequestMapping(value = "/cancelOrder.json", method = RequestMethod.POST)
	public AppResultData<Object> cancelOrder(
			@RequestParam("order_id") Long orderId,
			@RequestParam("remarks") String remarks,HttpServletRequest request) {

		Orders order = orderService.selectByPrimaryKey(orderId);

		AppResultData<Object> result = orderCancelService.cancleOrderDone(order);
		
		if(result.status==0){
			OrderLog initOrderLog = orderLogService.initOrderLog(order);
			initOrderLog.setAction(Constants.ORDER_ACTION_CANCLE);
			initOrderLog.setUserType((short)2);
			AccountAuth auth = AuthHelper.getSessionAccountAuth(request);
			initOrderLog.setUserId(auth.getId());
			initOrderLog.setUserName(auth.getUsername());
			initOrderLog.setRemarks("人工取消-"+remarks);
			orderLogService.insert(initOrderLog);
		}
		
		return result;
	}
	
	@AuthPassport
	@RequestMapping(value = "/order-hour-add", method = RequestMethod.GET)
	public String oaOrderHourAdd(Model model,HttpServletRequest request) {
		
		// 订单的来源
		CooperativeBusinessSearchVo vo = new CooperativeBusinessSearchVo();
		vo.setEnable((short) 1);
		List<CooperativeBusiness> CooperativeBusinessList = cooperateBusinessService
				.selectCooperativeBusinessVo(vo);
		if (CooperativeBusinessList != null) {
			model.addAttribute("cooperativeBusiness", CooperativeBusinessList);
		}
		
		List<PartnerServiceType> serviceTypeList = new ArrayList<PartnerServiceType>();
		List<Long> serviceTypeIds = new ArrayList<Long>();
		serviceTypeIds.add(28L);
		serviceTypeIds.add(29L);
		serviceTypeIds.add(68L);
		serviceTypeIds.add(69L);
		serviceTypeIds.add(70L);
		serviceTypeIds.add(73L);
		serviceTypeList = partService.selectByIds(serviceTypeIds);
		
		model.addAttribute("serviceTypeList", serviceTypeList);
		AccountAuth accountAuth = AuthHelper.getSessionAccountAuth(request);
		model.addAttribute("accountAuth", accountAuth);
		
		return "order/orderHourAdd";
	}

	
	@AuthPassport
	@RequestMapping(value = "/order-exp-add", method = RequestMethod.GET)
	public String orderAmAdd(Model model,HttpServletRequest request) {

		CooperativeBusinessSearchVo vo = new CooperativeBusinessSearchVo();
		vo.setEnable((short) 1);
		List<CooperativeBusiness> CooperativeBusinessList = cooperateBusinessService
				.selectCooperativeBusinessVo(vo);
		if (CooperativeBusinessList != null) {
			model.addAttribute("cooperativeBusiness", CooperativeBusinessList);
		}

		PartnerServiceTypeVo serviceTypeVo=new PartnerServiceTypeVo();
		serviceTypeVo.setParentId(26L);
		serviceTypeVo.setEnable((short)1);
		List<PartnerServiceType> serviceTypeList = serviceType.selectByPartnerServiceTypeVo(serviceTypeVo);
		model.addAttribute("serviceType", serviceTypeList);
		
		AccountAuth accountAuth = AuthHelper.getSessionAccountAuth(request);
		model.addAttribute("accountAuth", accountAuth);
		
		return "order/orderExpAdd";
	}

	@AuthPassport
	@RequestMapping(value = "/order-baby-add", method = RequestMethod.GET)
	public String orderBabyAdd(Model model,HttpServletRequest request) {

		CooperativeBusinessSearchVo vo = new CooperativeBusinessSearchVo();
		vo.setEnable((short) 1);
		List<CooperativeBusiness> CooperativeBusinessList = cooperateBusinessService
				.selectCooperativeBusinessVo(vo);
		if (CooperativeBusinessList != null) {
			model.addAttribute("cooperativeBusiness", CooperativeBusinessList);
		}

		PartnerServiceTypeVo serviceTypeVo=new PartnerServiceTypeVo();
		serviceTypeVo.setParentId(57L);
		serviceTypeVo.setEnable((short)1);
		List<PartnerServiceType> serviceTypeList = serviceType.selectByPartnerServiceTypeVo(serviceTypeVo);
		model.addAttribute("serviceType", serviceTypeList);
		
		AccountAuth accountAuth = AuthHelper.getSessionAccountAuth(request);
		model.addAttribute("accountAuth", accountAuth);

		return "order/orderExpAdd";
	}
	
	@AuthPassport
	@RequestMapping(value = "/update-order", method = RequestMethod.POST)
	@ResponseBody
	public String updateOrder(@ModelAttribute("oaOrderListVoModel") Orders orderForm,HttpServletRequest request) {

		Orders orders = orderService.selectByOrderNo(orderForm.getOrderNo());
		Long orderOpFrom = orders.getOrderOpFrom();
		orders.setOrderOpFrom(orderForm.getOrderOpFrom());
		orderService.updateByPrimaryKeySelective(orders);

		String remarks = "";
		if(orderOpFrom!=orderForm.getOrderOpFrom()){
			CooperativeBusiness cb1 = cooperateBusinessService.selectByPrimaryKey(orderOpFrom);
			if (cb1 != null) remarks+= cb1.getBusinessName();
			CooperativeBusiness cb2 = cooperateBusinessService.selectByPrimaryKey(orderForm.getOrderOpFrom());
			
			if (cb2 != null) remarks+= "修改成"+cb2.getBusinessName();
		}
		
		String groupCode = request.getParameter("groupCode");
		if (!StringUtil.isEmpty(groupCode)) {
			Long orderId = orders.getId();
			OrderExtend orderExtend = orderExtendService.selectByOrderId(orderId);
			String oldGroupCode = "";
			if (orderExtend == null) {
				orderExtend = orderExtendService.initPo();
			} else {
				oldGroupCode = orderExtend.getGroupCode();
			}
			
			orderExtend.setUserId(orders.getUserId());
			orderExtend.setOrderId(orders.getId());
			orderExtend.setOrderNo(orders.getOrderNo());
			orderExtend.setGroupCode(groupCode);
			if (orderExtend.getId().equals(0L)) {
				remarks+= ";团购卷修改成" + groupCode;
				orderExtendService.insert(orderExtend);
			} else {
				if (!oldGroupCode.equals(groupCode)) {
					remarks+= ";团购卷修改成" + groupCode;
					orderExtendService.updateByPrimaryKeySelective(orderExtend);
				}
			}
		}
		
		if (!StringUtil.isEmpty(remarks)) {
			AccountAuth accountAuth = AuthHelper.getSessionAccountAuth(request);
			OrderLog initOrderLog = orderLogService.initOrderLog(orders);
			initOrderLog.setAction(Constants.ORDER_ACTION_UPDATE);
			initOrderLog.setUserId(accountAuth.getId());
			initOrderLog.setUserName(accountAuth.getUsername());
			initOrderLog.setUserType((short)2);
			initOrderLog.setRemarks(remarks);
			orderLogService.insert(initOrderLog);
		}
			
		return "1" ;
	}
	
	/**
	 * 
	 * 验码功能，
	 * @param validateCode = 1,是
	 * 		  validateCode = 0,否
	 * 
	 * 
	 * */
	@AuthPassport
	@RequestMapping(value = "/validate-code", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> updateOrder(
			@RequestParam("orderId") Long orderId,
			@RequestParam("validateCode") Short validateCode,
			HttpServletRequest request) {

		Orders orders = orderService.selectByPrimaryKey(orderId);
		orders.setValidateCode(validateCode);
		orderService.updateByPrimaryKeySelective(orders);
		Map<String,String> map = new HashMap<>();
		String msg = "";
		if(validateCode==1){
			map.put("validateCode", "1");
			map.put("btnValue", "解除");
			msg = "已验码";
		}else{
			map.put("validateCode", "0");
			map.put("btnValue", "验码");
			msg = "解除验码";
		}
		
		//修改记录
		AccountAuth accountAuth = AuthHelper.getSessionAccountAuth(request);
		OrderLog initOrderLog = orderLogService.initOrderLog(orders);
		initOrderLog.setAction(Constants.ORDER_ACTION_UPDATE);
		initOrderLog.setUserId(accountAuth.getId());
		initOrderLog.setUserName(accountAuth.getUsername());
		initOrderLog.setUserType((short)2);
		initOrderLog.setRemarks("修改成" + msg);
		orderLogService.insert(initOrderLog);
		return map;
	}

}
