package com.jhj.action.order;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AccountAuth;
import com.jhj.oa.auth.AccountRole;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.order.DispatchStaffFromOrderService;
import com.jhj.service.order.OaOrderService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OaOrderListNewVo;
import com.jhj.vo.order.OaOrderListVo;
import com.jhj.vo.order.OaOrderSearchVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.jhj.vo.org.OrgSearchVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
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

	/**
	 * 钟点工-----订单列表---orderType=0
	 * 
	 * @param model
	 * @param request
	 * @param oaOrderSearchVo
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@AuthPassport
	@RequestMapping(value = "/order-hour-list", method = RequestMethod.GET)
	public String getOrderHourList(Model model, HttpServletRequest request, OrderSearchVo searchVo) throws ParseException, UnsupportedEncodingException {

		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ConstantOa.DEFAULT_PAGE_SIZE;
		// 分页
		
		//查询条件的组合，需要做一些逻辑判断
		//1. 如果为运营人员，则可以看所有的门店和所有状态
		//2. 如果为店长，则只能看当前门店和已派工到该门店的人员.
		
		//查询条件： 设置为钟点工的订单
		if (searchVo == null) {
			searchVo = new OrderSearchVo();
		}
		
		//此方法只查普通服务
		searchVo.setOrderType(Constants.ORDER_TYPE_0);
		
		//判断是否为店长登陆，如果org > 0L ，则为某个店长，否则为运营人员.
		Long sessionParentId = AuthHelper.getSessionLoginOrg(request);
		if (sessionParentId > 0L) searchVo.setParentId(sessionParentId);
		// 处理查询条件云店--------------------------------开始
		// 1) 如果有查询条件云店org_id，则以查询条件的云店为准
		// 2) 如果没有查询条件，则判断是否为店长，并且只能看店长所在门店下的所有云店.
		
		Long parentId = 0L;
		String parentIdParam = request.getParameter("parentId");
		if (!StringUtil.isEmpty(parentIdParam)) parentId = Long.valueOf(parentIdParam);

		if (parentId > 0L) searchVo.setParentId(parentId);
		
		Long orgId = 0L;
		String orgIdParam = request.getParameter("orgId");
		
		if (!StringUtil.isEmpty(orgIdParam)) orgId = Long.valueOf(orgIdParam);
		
		if (orgId > 0L) searchVo.setOrgId(orgId);
		
		// 处理查询时间条件--------------------------------开始
		//下单开始时间
		String startTimeStr = request.getParameter("startTimeStr");
		if (!StringUtil.isEmpty(startTimeStr)) {
			searchVo.setStartAddTime(TimeStampUtil.getMillisOfDay(startTimeStr) / 1000);
			
			model.addAttribute("startTimeStr", startTimeStr);
		}

		//下单结束时间
		String endTimeStr = request.getParameter("endTimeStr");
		if (!StringUtil.isEmpty(endTimeStr)) {
			searchVo.setEndAddTime(TimeStampUtil.getMillisOfDay(startTimeStr) / 1000);
			
			model.addAttribute("endTimeStr", endTimeStr);
		}
		
		//服务开始时间
		String serviceStartTime = request.getParameter("serviceStartTime");
		if (!StringUtil.isEmpty(serviceStartTime)) {
			searchVo.setStartServiceTime(TimeStampUtil.getMillisOfDay(serviceStartTime) / 1000);
			
			model.addAttribute("serviceStartTime", serviceStartTime);
		}
		//服务结束时间
		String serviceEndTimeStr = request.getParameter("serviceEndTimeStr");
		if (!StringUtil.isEmpty(serviceEndTimeStr)) {
			searchVo.setEndServiceTime(TimeStampUtil.getMillisOfDay(serviceEndTimeStr) / 1000);
			
			model.addAttribute("serviceEndTimeStr", serviceEndTimeStr);
		}
		// 处理查询时间条件--------------------------------结束
		
		// 处理查询状态条件--------------------------------开始
		if (searchVo.getOrderStatus() == null) {
			//如果为店长只能看已派工状态之后的订单.
			if (sessionParentId > 0L) {
				List<Short> orderStatusList = new ArrayList<Short>();

	            // 店长 可查看的 钟点工 订单状态列表： 已派工 之后的都可以查看
	            // public static short ORDER_HOUR_STATUS_3=3;//已派工
	            // public static short ORDER_HOUR_STATUS_5=5;//开始服务
	            // public static short ORDER_HOUR_STATUS_7=7;//完成服务
	            // public static short ORDER_HOUR_STATUS_8=8;//已评价
	            // public static short ORDER_HOUR_STATUS_9=9;//已关闭

				orderStatusList.add(Constants.ORDER_HOUR_STATUS_3);
				orderStatusList.add(Constants.ORDER_HOUR_STATUS_5);
				orderStatusList.add(Constants.ORDER_HOUR_STATUS_7);
				orderStatusList.add(Constants.ORDER_HOUR_STATUS_8);
				orderStatusList.add(Constants.ORDER_HOUR_STATUS_9);

				searchVo.setOrderStatusList(orderStatusList);
			}
		}

		PageInfo result = orderService.selectByListPage(searchVo, pageNo, pageSize);
		
		List<Orders> orderList = result.getList();
		Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaOrderListNewVo completeVo = oaOrderService.completeNewVo(orders);
			orderList.set(i, completeVo);
		}

		result = new PageInfo(orderList);

		model.addAttribute("loginOrgId", sessionParentId); // 当前登录的 id,动态显示搜索 条件
		model.addAttribute("oaOrderListVoModel", result);
		model.addAttribute("searchModel", searchVo);
		
		return "order/orderHourList";
	}

	/**
	 * 钟点工---订单详情
	 * 
	 * @param orderNo
	 * @param disStatus
	 * 
	 *            2016年4月20日15:39:00 该参数已无 实际用处。但为了兼顾 老数据，暂时保留
	 * 
	 * @param model
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/order-hour-view", method = RequestMethod.GET)
	public String orderHourDetail(String orderNo, Short disStatus, Model model, HttpServletRequest request) {
		OaOrderListVo oaOrderListVo = oaOrderService.getOrderVoDetailHour(orderNo, disStatus);
		
		Orders orders = orderService.selectByOrderNo(orderNo);
		
		List<OrgStaffsNewVo> list = new ArrayList<OrgStaffsNewVo>();
		
		oaOrderListVo.setVoList(list);

		model.addAttribute("oaOrderListVoModel", oaOrderListVo);

		// 得到 当前登录 的 店长所在 门店id，
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {

			/*
			 * 派工调整时，选择 门店
			 */
			model.addAttribute("loginOrgId", sessionOrgId);
		}

		short orderType = oaOrderListVo.getOrderType();
		Map<String, String> map = isRole(request);
		model.addAttribute("role", map);
		if (orderType == Constants.ORDER_TYPE_0) {// 钟点工
			return "order/orderHourViewForm";
		} else {
			return "order/orderViewForm";
		}
	}

	/**
	 * 深度保洁---订单详情
	 * 
	 * 2016年4月20日15:39:44 该方法 已经没有用处。。jhj2.1 没有深度保洁订单了
	 * 
	 * @param orderNo
	 * @param disStatus
	 * 
	 *            2016年4月20日15:39:00 该参数已无 实际用处。但为了兼顾 老数据，暂时保留
	 * @param model
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/order-exp-view", method = RequestMethod.GET)
	public String orderExpDetail(String orderNo, Short disStatus, Model model) {
		OaOrderListVo oaOrderListVo = oaOrderService.getOrderExpVoDetail(orderNo, disStatus);
		model.addAttribute("oaOrderListVoModel", oaOrderListVo);
		short orderType = oaOrderListVo.getOrderType();
		if (orderType == Constants.ORDER_TYPE_1) {// 深度保洁
			return "order/orderExpViewForm";
		} else {
			return "order/orderViewForm";
		}
	}

	/*
	 * 跳转显示可用服务人员列表
	 * 
	 * @param orderNo
	 * 
	 * @param disStatus
	 * 
	 * @param model
	 * 
	 * @param poiLongitude
	 * 
	 * @param poiLatitude
	 * 
	 * @param pickAddrName
	 * 
	 * @param pickAddr
	 * 
	 * @return
	 */
	// @AuthPassport
	@RequestMapping(value = "/order-am-staff", method = RequestMethod.POST)
	public String orderAmDetail(String orderNo, Short disStatus, Model model, String poiLongitude, String poiLatitude, String pickAddrName, String pickAddr,
			String userAddrKey) {
		OaOrderListVo oaOrderListVo = oaOrderService.getOrderVoDetailAm(orderNo, disStatus, poiLongitude, poiLatitude);
		oaOrderListVo.setPoiLongitude(poiLongitude);// 服务地址经度
		oaOrderListVo.setPoiLatitude(poiLatitude);// 服务地址维度
		oaOrderListVo.setPickAddr(pickAddr);// 服务地址门牌号
		oaOrderListVo.setPickAddrName(pickAddrName);// 服务地址名称
		oaOrderListVo.setUserAddrKey(userAddrKey);
		oaOrderListVo.setFlag("1");// 是否显示服务人员列表1=显示，0=不显示
		model.addAttribute("oaOrderListVoModel", oaOrderListVo);
		short orderType = oaOrderListVo.getOrderType();
		if (orderType == Constants.ORDER_TYPE_2) {// 助理
			return "order/orderAmViewForm";
		} else {
			return "order/orderViewForm";
		}
	}

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
	@RequestMapping(value = "/disStaffByExpOrderNo", method = RequestMethod.POST)
	public String disStaffForExpOrder(Long userId, Long id) {

		OrgStaffs orgStaffs = orgStaService.selectByPrimaryKey(Long.valueOf("17"));
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
		List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo);

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

			OrgStaffs orgStaffs = orgStaService.selectByPrimaryKey(staffId);
			OrgStaffsNewVo orgStaffsNewVo = new OrgStaffsNewVo();
			orgStaffsNewVo.setStaffId(staffId);
			orgStaffs.setStaffId(staffId);
			List<OrgStaffsNewVo> orgStaffsNewVos = new ArrayList<OrgStaffsNewVo>();
			orgStaffsNewVos.add(orgStaffsNewVo);

			orderPayService.orderPaySuccessToDoForHour(userId, id, orgStaffsNewVos, isChange);
		}
		
		return "redirect:/order/order-list";
	}



	/**
	 * 运营人员 为订单 添加 备注
	 */

	@RequestMapping(value = "remarks_bussiness_form", method = RequestMethod.GET)
	public String toBussinessRemarkForm(Model model, @RequestParam("orderId") Long orderId) {

		Orders orders = orderService.selectByPrimaryKey(orderId);

		model.addAttribute("orderModel", orders);

		return "order/OrderRemarksBussinessForm";
	}

	/**
	 * 运营人员 为订单 添加 备注
	 */

	@RequestMapping(value = "remarks_bussiness_form", method = RequestMethod.POST)
	public String submitBussinessRemarkForm(@ModelAttribute("orderModel") Orders orderForm, BindingResult bindingResult) {

		Long id = orderForm.getId();

		Orders orders = orderService.selectByPrimaryKey(id);

		orders.setRemarksBussinessConfirm(orderForm.getRemarksBussinessConfirm());

		// 这里不设置修改时间。。与1期的定时任务，可能会冲突
		orderService.updateByPrimaryKeySelective(orders);

		String returnUrl = "";

		if (orders.getOrderType() == Constants.ORDER_TYPE_0) {
			// 钟点工订单
			returnUrl = "redirect:order-hour-list";
		} else {
			// 助理订单
			returnUrl = "redirect:order-am-list";
		}

		return returnUrl;
	}
	
	/**
	 * 	取消助理订单
	 * @param orderId
	 * 
	 * */
	@RequestMapping(value="/cancelOrder/{id}",method=RequestMethod.GET)
	public String cancelOrder(Model model,@PathVariable("id") Long orderId){
		
		Map<String,String> map=new HashMap<String,String>();
		Orders order = orderService.selectByPrimaryKey(orderId);
		int status = orderService.cancelByOrder(order);
		if(status==0){
			map.put("success", "取消订单成功！");
		}else{
			map.put("fail", "订单取消失败！");
		}
		model.addAttribute("map", map);
		String orderNo = order.getOrderNo();
		Short orderType = order.getOrderType();
		if(orderType==Constants.ORDER_TYPE_0){
			return "redirect:../order-hour-view?orderNo="+orderNo+"&disStatus="+orderType;
		}
		if(orderType==Constants.ORDER_TYPE_2){
			return "redirect:../order-am-view?orderNo="+orderNo+"&disStatus="+orderType;
		}
		return null;
	}
	
	//判断当前用户的角色
	//判断用户是否有权限操作页面中的按钮， 如果，则显示该按钮，如果没有，则不显示改按钮
	public Map<String,String> isRole(HttpServletRequest request){
		AccountAuth auth = AuthHelper.getSessionAccountAuth(request);
		AccountRole accountRole = auth.getAccountRole();
		List<String> list=new ArrayList<String>();
		list.add("系统管理员");
		list.add("总部运营");
		Map<String,String> map=null;
		if(list.contains(accountRole.getName())){
			map=new HashMap<String,String>();
			map.put("role", "show");
		}
		return map;
	}
}
