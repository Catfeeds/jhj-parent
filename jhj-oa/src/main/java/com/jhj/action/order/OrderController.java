package com.jhj.action.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jhj.action.BaseController;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.cooperate.CooperateBusinessService;
import com.jhj.service.order.DispatchStaffFromOrderService;
import com.jhj.service.order.OaOrderService;
import com.jhj.service.order.OrderDispatchsService;
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
	private DispatchStaffFromOrderService dispatchStaffFromOrderService;
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
	
	/*
	 * 深度保洁派工----固定派工人员
	 * 
	 * @param userId
	 * 
	 * @param id
	 * 
	 * @param staffId
	 * 
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/disStaffByExpOrderNo", method = RequestMethod.POST)
	public String disStaffForExpOrder(Long userId, Long id) {

		OrgStaffs orgStaffs = orgStaService.selectByPrimaryKey(Long
				.valueOf("17"));
		Orders order = orderService.selectByPrimaryKey(id);
		Users user = usersService.selectByPrimaryKey(userId);

		// 如果订单为已支付=4，更新订单状态为服务中=5
		if (order.getOrderStatus() == Constants.ORDER_DEP_STATUS_2) {// 已支付的订单才进行派工
			order.setOrderStatus(Constants.ORDER_DEP_STATUS_3);
			orderService.updateByPrimaryKeySelective(order);

			// 更新派工表 + 日志表
			dispatchStaffFromOrderService.disStaff(orgStaffs, order, user);

		}
		return "redirect:/order/order-exp-list";
	}

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
	@RequestMapping(value = "remarks_bussiness_form", method = RequestMethod.GET)
	public String toBussinessRemarkForm(Model model,
			@RequestParam("orderId") Long orderId) {

		Orders orders = orderService.selectByPrimaryKey(orderId);

		model.addAttribute("orderModel", orders);

		return "order/OrderRemarksBussinessForm";
	}

	/**
	 * 运营人员 为订单 添加 备注
	 */
	@AuthPassport
	@RequestMapping(value = "remarks_bussiness_form", method = RequestMethod.POST)
	public String submitBussinessRemarkForm(
			@ModelAttribute("orderModel") Orders orderForm,
			BindingResult bindingResult) {

		Long id = orderForm.getId();

		Orders orders = orderService.selectByPrimaryKey(id);

		orders.setRemarksBussinessConfirm(orderForm
				.getRemarksBussinessConfirm());

		// 这里不设置修改时间。。与1期的定时任务，可能会冲突
		orderService.updateByPrimaryKeySelective(orders);

		String returnUrl = "";

		if (orders.getOrderType() == Constants.ORDER_TYPE_0) {
			// 钟点工订单
			returnUrl = "redirect:order-hour-list";
		}
		if (orders.getOrderType() == Constants.ORDER_TYPE_1) {
			// 深度保洁
			returnUrl = "redirect:order-deep-list";
		}

		return returnUrl;
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
	@RequestMapping(value = "/cancelOrder/{id}", method = RequestMethod.GET)
	public Map<String,Object> cancelOrder(Model model, @PathVariable("id") Long orderId) {

		Map<String, Object> map = new HashMap<String, Object>();
		Orders order = orderService.selectByPrimaryKey(orderId);
//		int status = orderService.cancelByOrder(order);
		boolean flg = orgStaffFinanceService.cancleOrderDone(order);
		if (flg) {
			map.put("success", true);
		} else {
			map.put("fail", false);
		}
		
		return map;
	}
	
	@AuthPassport
	@RequestMapping(value = "/order-hour-add", method = RequestMethod.GET)
	public String oaOrderHourAdd(Model model) {
		
		// 订单的来源
		CooperativeBusinessSearchVo vo = new CooperativeBusinessSearchVo();
		vo.setEnable((short) 1);
		List<CooperativeBusiness> CooperativeBusinessList = cooperateBusinessService
				.selectCooperativeBusinessVo(vo);

		PartnerServiceType serviceType = partService.selectByPrimaryKey(28L);
		PartnerServiceType serviceType1 = partService.selectByPrimaryKey(29L);
		Map<String,Object> serviceTypeList=new HashMap<String,Object>();
		serviceTypeList.put("hour",serviceType);
		serviceTypeList.put("cook",serviceType1);
		
		if (CooperativeBusinessList != null) {
			model.addAttribute("cooperativeBusiness", CooperativeBusinessList);
		}
		model.addAllAttributes(serviceTypeList);
		return "order/orderHourAdd";
	}

	
	@AuthPassport
	@RequestMapping(value = "/order-exp-add", method = RequestMethod.GET)
	public String orderAmAdd(Model model) {

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
		return "order/orderExpAdd";
	}

	@AuthPassport
	@RequestMapping(value = "/order-baby-add", method = RequestMethod.GET)
	public String orderBabyAdd(Model model) {

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

		return "order/orderExpAdd";
	}

}
