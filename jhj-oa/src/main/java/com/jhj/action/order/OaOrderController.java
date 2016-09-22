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
import com.jhj.vo.OaOrderSearchVo;
import com.jhj.vo.OrderSearchVo;
import com.jhj.vo.OrgSearchVo;
import com.jhj.vo.order.OaOrderListNewVo;
import com.jhj.vo.order.OaOrderListVo;
import com.jhj.vo.order.OrgStaffsNewVo;
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
public class OaOrderController extends BaseController {
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
	private OrderDispatchsService disService;

	/*
	 * 查看订单列表
	 */
	@AuthPassport
	@RequestMapping(value = "/order-list", method = RequestMethod.GET)
	public String getOrderList(Model model, HttpServletRequest request, OaOrderSearchVo oaOrderSearchVo) {
		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		// 分页
		PageHelper.startPage(pageNo, pageSize);

		if (oaOrderSearchVo == null) {
			oaOrderSearchVo = new OaOrderSearchVo();
		}

		// 得到 当前登录 的 门店id，并作为搜索条件
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {
			// 未选择 门店， 且 当前 登录 用户 为 店长 （ session中的 orgId 不为 0）,设置搜索条件为 店长的门店
			oaOrderSearchVo.setSearchOrgId(sessionOrgId);
		}

		// 如果在 订单列表页面，选择了 门店 作为搜索 条件
		String jspOrgId = request.getParameter("orgId");
		if (!StringUtil.isEmpty(jspOrgId) && !jspOrgId.equals("0")) {
			oaOrderSearchVo.setSearchOrgId(Long.valueOf(jspOrgId));
		}

		List<Orders> orderList = oaOrderService.selectVoByListPage(oaOrderSearchVo, pageNo, pageSize);

		Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaOrderListNewVo completeVo = oaOrderService.completeVo(orders);
			orderList.set(i, completeVo);
		}

		PageInfo result = new PageInfo(orderList);

		model.addAttribute("loginOrgId", sessionOrgId); // 当前登录的 id,动态显示搜索 条件
		model.addAttribute("oaOrderListVoModel", result);
		model.addAttribute("oaOrderSearchVoModel", oaOrderSearchVo);

		return "order/orderList";
	}

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
	@AuthPassport
	@RequestMapping(value = "/order-hour-list", method = RequestMethod.GET)
	public String getOrderHourList(Model model, HttpServletRequest request, OaOrderSearchVo oaOrderSearchVo) throws ParseException, UnsupportedEncodingException {

		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		// 分页
		
		//查询条件的组合，需要做一些逻辑判断
		//1. 如果为运营人员，则可以看所有的门店和所有状态
		//2. 如果为店长，则只能看当前门店和已派工到该门店的人员.
		
		//查询条件： 设置为钟点工的订单
		if (oaOrderSearchVo == null) {
			oaOrderSearchVo = new OaOrderSearchVo();
			//钟点工
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_0);
		} else {
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_0);
		}
		
		//判断是否为店长登陆，如果org > 0L ，则为某个店长，否则为运营人员.
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);
		
		
		// 处理查询条件云店--------------------------------开始
		// 1) 如果有查询条件云店org_id，则以查询条件的云店为准
		// 2) 如果没有查询条件，则判断是否为店长，并且只能看店长所在门店下的所有云店.
		String paramOrgId = request.getParameter("orgId");
		List<Long> cloudIdList = new ArrayList<Long>();
		if (!StringUtil.isEmpty(paramOrgId) && !paramOrgId.equals("0")) {
			cloudIdList.add(Long.valueOf(paramOrgId));
		} else {
			
			if (sessionOrgId > 0L) {
				OrgSearchVo searchVo = new OrgSearchVo();
				searchVo.setParentId(sessionOrgId);
				searchVo.setOrgStatus((short) 1);
				List<Orgs> cloudList = orgService.selectBySearchVo(searchVo);

				for (Orgs orgs : cloudList) {
					cloudIdList.add(orgs.getOrgId());
				}
			}
		}

		if (!cloudIdList.isEmpty()) {
			oaOrderSearchVo.setSearchCloudOrgIdList(cloudIdList);
		}
		// 处理查询条件云店--------------------------------结束

		// 处理查询时间条件--------------------------------开始
		//下单开始时间
		String startTimeStr = oaOrderSearchVo.getStartTimeStr();
		if (!StringUtil.isEmpty(startTimeStr)) {
			oaOrderSearchVo.setStartTime(DateUtil.getUnixTimeStamp(DateUtil.getBeginOfDay(startTimeStr)));
		}
		
		//下单结束时间
		String endTimeStr = oaOrderSearchVo.getEndTimeStr();
		if (!StringUtil.isEmpty(endTimeStr)) {
			oaOrderSearchVo.setEndTime(DateUtil.getUnixTimeStamp(DateUtil.getEndOfDay(endTimeStr)));
		}
		
		//服务开始时间
		String serviceStartTime=oaOrderSearchVo.getServiceStartTime();
		if(!StringUtil.isEmpty(serviceStartTime)){
			oaOrderSearchVo.setServiceTime1(DateUtil.getUnixTimeStamp(DateUtil.toDate(serviceStartTime)));
		}
		//服务结束时间
		String serviceEndTime=oaOrderSearchVo.getServiceEndTime();
		if(!StringUtil.isEmpty(serviceEndTime)){
			oaOrderSearchVo.setServiceTime2(DateUtil.getUnixTimeStamp(DateUtil.toDate(serviceEndTime)));
		}
		// 处理查询时间条件--------------------------------结束
		
		// 处理查询状态条件--------------------------------开始
		if (oaOrderSearchVo.getOrderStatus() == null) {
			//如果为店长只能看已派工状态之后的订单.
			if (sessionOrgId > 0L) {
				List<Short> searchOrderStatusList = new ArrayList<Short>();

	            // 店长 可查看的 钟点工 订单状态列表： 已派工 之后的都可以查看
	            // public static short ORDER_HOUR_STATUS_3=3;//已派工
	            // public static short ORDER_HOUR_STATUS_5=5;//开始服务
	            // public static short ORDER_HOUR_STATUS_7=7;//完成服务
	            // public static short ORDER_HOUR_STATUS_8=8;//已评价
	            // public static short ORDER_HOUR_STATUS_9=9;//已关闭

	            searchOrderStatusList.add(Constants.ORDER_HOUR_STATUS_3);
	            searchOrderStatusList.add(Constants.ORDER_HOUR_STATUS_5);
	            searchOrderStatusList.add(Constants.ORDER_HOUR_STATUS_7);
	            searchOrderStatusList.add(Constants.ORDER_HOUR_STATUS_8);
	            searchOrderStatusList.add(Constants.ORDER_HOUR_STATUS_9);

	            oaOrderSearchVo.setSearchOrderStatusList(searchOrderStatusList);
			}
		}
		
		String flag=request.getParameter("flag");
		if(flag!=null && flag!=""){
			if(flag.equals("1")){
				Long startTime = TimeStampUtil.getMillisOfDayFull(DateUtil.getBeginOfDay()) / 1000;
				Long endTime = TimeStampUtil.getMillisOfDayFull(DateUtil.getEndOfDay()) / 1000;
				oaOrderSearchVo.setStartTime(startTime);
				oaOrderSearchVo.setEndTime(endTime);
			}
		}
		
		String address = oaOrderSearchVo.getAddress();
		if(address!=null){
			String addr = new String(address.getBytes("ISO-8859-1"),"UTF-8");
			oaOrderSearchVo.setAddress(addr);
		}
		List<Orders> orderList = oaOrderService.selectVoByListPage(oaOrderSearchVo, pageNo, pageSize);

		Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaOrderListNewVo completeVo = oaOrderService.completeNewVo(orders);
			orderList.set(i, completeVo);
		}

		PageInfo result = new PageInfo(orderList);

		model.addAttribute("loginOrgId", sessionOrgId); // 当前登录的 id,动态显示搜索 条件
		model.addAttribute("oaOrderListVoModel", result);
		model.addAttribute("oaOrderSearchVoModel", oaOrderSearchVo);

		return "order/orderHourList";
	}

	/**
	 * 深度保洁---订单列表---orderType=1
	 * 
	 * @param model
	 * @param request
	 * @param oaOrderSearchVo
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/order-exp-list", method = RequestMethod.GET)
	public String getOrderExpList(Model model, HttpServletRequest request, OaOrderSearchVo oaOrderSearchVo) {

		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		// 分页
		PageHelper.startPage(pageNo, pageSize);

		if (oaOrderSearchVo == null) {
			oaOrderSearchVo = new OaOrderSearchVo();
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_1);
		} else {
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_1);
		}

		// 得到 当前登录 的 门店id，并作为搜索条件
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {
			// 未选择 门店， 且 当前 登录 用户 为 店长 （ session中的 orgId 不为 0）,设置搜索条件为 店长的门店
			oaOrderSearchVo.setSearchOrgId(sessionOrgId);
		}

		// //如果在 订单列表页面，选择了 门店 作为搜索 条件
		// String jspOrgId = request.getParameter("orgId");
		// if(!StringUtil.isEmpty(jspOrgId) && !jspOrgId.equals("0")){
		// oaOrderSearchVo.setSearchOrgId(Long.valueOf(jspOrgId));
		// }

		List<Orders> orderList = oaOrderService.selectVoByListPage(oaOrderSearchVo, pageNo, pageSize);

		Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaOrderListNewVo completeVo = oaOrderService.completeNewVo(orders);
			orderList.set(i, completeVo);
		}

		PageInfo result = new PageInfo(orderList);

		model.addAttribute("loginOrgId", sessionOrgId); // 当前登录的 id,动态显示搜索 条件
		model.addAttribute("oaOrderListVoModel", result);
		model.addAttribute("oaOrderSearchVoModel", oaOrderSearchVo);

		return "order/orderExpList";
	}

	/**
	 * 助理---订单列表---orderType=2
	 * 
	 * @param model
	 * @param request
	 * @param oaOrderSearchVo
	 * @return
	 * @throws ParseException
	 */
	@AuthPassport
	@RequestMapping(value = "/order-am-list", method = RequestMethod.GET)
	public String getOrderAmList(Model model, HttpServletRequest request, OaOrderSearchVo oaOrderSearchVo) throws ParseException {

		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		// 分页
//		PageHelper.startPage(pageNo, pageSize);

		if (oaOrderSearchVo == null) {
			oaOrderSearchVo = new OaOrderSearchVo();
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_2);
		} else {
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_2);
		}

		// 得到 当前登录 的 门店id，并作为搜索条件
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		

		List<Long> cloudIdList = new ArrayList<Long>();

		if (sessionOrgId > 0L) {

			OrgSearchVo searchVo = new OrgSearchVo();
			searchVo.setParentId(sessionOrgId);
			searchVo.setOrgStatus((short) 1);
			List<Orgs> cloudList = orgService.selectBySearchVo(searchVo);

			for (Orgs orgs : cloudList) {
				cloudIdList.add(orgs.getOrgId());
			}

			// 店长 可查看的 助理 订单状态列表： 已派工 之后的都可以查看
			// public static short ORDER_AM_STATUS_4=4;//已派工
			// public static short ORDER_AM_STATUS_5=5;//开始服务
			// public static short ORDER_AM_STATUS_7=7;//完成服务
			// public static short ORDER_AM_STATUS_9=9;//已关闭

			List<Short> searchOrderStatusList = new ArrayList<Short>();

			searchOrderStatusList.add(Constants.ORDER_AM_STATUS_4);
			searchOrderStatusList.add(Constants.ORDER_AM_STATUS_5);
			searchOrderStatusList.add(Constants.ORDER_AM_STATUS_7);
			searchOrderStatusList.add(Constants.ORDER_AM_STATUS_9);

			oaOrderSearchVo.setSearchOrderStatusList(searchOrderStatusList);

		} else {
			// 如果是 运营 人员，则能 查看全部 订单, 查看所有 云店
			OrgSearchVo searchVo = new OrgSearchVo();
			searchVo.setIsCloud((short) 1);
			searchVo.setOrgStatus((short) 1);
			List<Orgs> cloudOrgList = orgService.selectBySearchVo(searchVo);

			for (Orgs orgs : cloudOrgList) {
				cloudIdList.add(orgs.getOrgId());
			}

			/*
			 * 对于 未派工的 订单，，没有 云店， 保证 运营和 市场 人员可以看到即可 ，以便 后续处理
			 */
			cloudIdList.add(0L);
		}

		// 根据 登录 角色的门店，确定 云店
//		oaOrderSearchVo.setSearchCloudOrgIdList(cloudIdList);

		// 如果在 列表页面，选择了 云店 作为搜索 条件
		String jspOrgId = request.getParameter("orgId");
		if (!StringUtil.isEmpty(jspOrgId) && !jspOrgId.equals("0")) {

			cloudIdList.clear();
			cloudIdList.add(Long.valueOf(jspOrgId));
			oaOrderSearchVo.setSearchCloudOrgIdList(cloudIdList);
		}

		/*
		 * 2016年4月13日11:10:21 处理 与 助理 相关的 统计的 数据下钻。
		 * 
		 * 统计图表，统计的是 助理下的服务大类。如 贴心家事
		 * 
		 * 而此处 列表页是 服务大类的 子类，如贴心家事--安心托管、、、
		 * 
		 * 需要 转换大类-->子类集合
		 */
		Long parentServiceType = oaOrderSearchVo.getParentServiceType();

		if (parentServiceType != null && parentServiceType != 0L) {

			List<Long> list = partService.selectChildIdByParentId(parentServiceType);

			oaOrderSearchVo.setChildServiceTypeList(list);
		}

		// 转换为数据库 参数字段
		String startTimeStr = oaOrderSearchVo.getStartTimeStr();
		if (!StringUtil.isEmpty(startTimeStr)) {
			oaOrderSearchVo.setStartTime(DateUtil.getUnixTimeStamp(DateUtil.getBeginOfDay(startTimeStr)));
		}

		String endTimeStr = oaOrderSearchVo.getEndTimeStr();
		if (!StringUtil.isEmpty(endTimeStr)) {
			oaOrderSearchVo.setEndTime(DateUtil.getUnixTimeStamp(DateUtil.getEndOfDay(endTimeStr)));
		}
		
		String flag=request.getParameter("flag");
		if(flag!=null && flag!="" && flag.equals("2")){
	    	Long startTime = TimeStampUtil.getMillisOfDayFull(DateUtil.getBeginOfDay()) / 1000;
			Long endTime = TimeStampUtil.getMillisOfDayFull(DateUtil.getEndOfDay()) / 1000;
			oaOrderSearchVo.setStartTime(startTime);
			oaOrderSearchVo.setEndTime(endTime);
		}

		List<Orders> orderList = oaOrderService.selectVoByListPage(oaOrderSearchVo, pageNo, pageSize);

		Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaOrderListNewVo completeVo = oaOrderService.completeNewVo(orders);

			orderList.set(i, completeVo);
		}

		PageInfo result = new PageInfo(orderList);

		model.addAttribute("loginOrgId", sessionOrgId); // 当前登录的 id,动态显示搜索 条件

		model.addAttribute("oaOrderListVoModel", result);
		model.addAttribute("oaOrderSearchVoModel", oaOrderSearchVo);

		return "order/orderAmList";
	}

	/**
	 * 配送--- 订单列表
	 * 
	 * @param model
	 * @param request
	 * @param oaOrderSearchVo
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/order-del-list", method = RequestMethod.GET)
	public String getOrderDelList(Model model, HttpServletRequest request, OaOrderSearchVo oaOrderSearchVo) {

		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		// 分页
		PageHelper.startPage(pageNo, pageSize);

		if (oaOrderSearchVo == null) {
			oaOrderSearchVo = new OaOrderSearchVo();
			//配送服务订单
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_3);
		} else {
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_3);
		}

		// 得到 当前登录 的 门店id，并作为搜索条件
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {
			// 未选择 门店， 且 当前 登录 用户 为 店长 （ session中的 orgId 不为 0）,设置搜索条件为 店长的门店
			oaOrderSearchVo.setSearchOrgId(sessionOrgId);
		}

		// 如果在 订单列表页面，选择了 门店 作为搜索 条件
		String jspOrgId = request.getParameter("orgId");
		if (!StringUtil.isEmpty(jspOrgId) && !jspOrgId.equals("0")) {
			oaOrderSearchVo.setSearchOrgId(Long.valueOf(jspOrgId));
		}

		List<Orders> orderList = oaOrderService.selectVoByListPage(oaOrderSearchVo, pageNo, pageSize);

		Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaOrderListNewVo completeVo = oaOrderService.completeNewVo(orders);
			orderList.set(i, completeVo);
		}

		PageInfo result = new PageInfo(orderList);

		model.addAttribute("loginOrgId", sessionOrgId); // 当前登录的 id,动态显示搜索 条件
		model.addAttribute("oaOrderListVoModel", result);
		model.addAttribute("oaOrderSearchVoModel", oaOrderSearchVo);

		return "order/orderDelList";
	}

	/*
	 * 查看订单详情
	 * 
	 * @param 订单号。当前 派工状态
	 */
	// @AuthPassport
	@RequestMapping(value = "/orderView", method = RequestMethod.GET)
	public String orderDetail(String orderNo, Short disStatus, Model model) {
		OaOrderListVo oaOrderListVo = oaOrderService.getOrderVoDetail(orderNo, disStatus);
		model.addAttribute("oaOrderListVoModel", oaOrderListVo);
		return "order/orderViewForm";
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

		OrderSearchVo searchVo = new OrderSearchVo();

		searchVo.setServiceDateStart(orders.getServiceDate());
		searchVo.setServiceDateEnd(orders.getServiceDate() + orders.getServiceHour() * 3600);
		
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

	/**
	 * 助理---订单详情
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
	@RequestMapping(value = "/order-am-view", method = RequestMethod.GET)
	public String orderAmDetail(String orderNo, Short disStatus, Model model,HttpServletRequest request) {
		OaOrderListVo oaOrderListVo = oaOrderService.getOrderVoDetailAm(orderNo, disStatus);
		oaOrderListVo.setFlag("0");// 标识是否显示服务人员列表
		model.addAttribute("oaOrderListVoModel", oaOrderListVo);
		Map<String, String> map = isRole(request);
		model.addAttribute("role", map);
		short orderType = oaOrderListVo.getOrderType();
		if (orderType == Constants.ORDER_TYPE_2) {// 助理
			return "order/orderAmViewForm";
		} else {
			return "order/orderViewForm";
		}
	}

	/**
	 * 配送---订单详情
	 * 
	 * @param orderNo
	 * @param disStatus
	 * @param model
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/order-del-view", method = RequestMethod.GET)
	public String orderDelDetail(String orderNo, Short disStatus, Model model) {
		OaOrderListVo oaOrderListVo = oaOrderService.getOrderVoDetailDel(orderNo, disStatus);
		model.addAttribute("oaOrderListVoModel", oaOrderListVo);
		short orderType = oaOrderListVo.getOrderType();
		if (orderType == Constants.ORDER_TYPE_3) {// 配送
			return "order/orderDelViewForm";
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
	 * 助理订单确认派工
	 * 
	 * @param 订单用户Id, 订单Id, 订单（有过派工） 对应 的阿姨 Id
	 */
	@RequestMapping(value = "/disStaffByAmOrderNo", method = RequestMethod.POST)
	public String disStaffForAmOrder(HttpServletRequest request, Long userId, Long id, Long staffId, String poiLongitude, String poiLatitude,
			String userAddrDistance) {

		OrgStaffs orgStaffs = orgStaService.selectByPrimaryKey(staffId);
		Orders order = orderService.selectByPrimaryKey(id);
		Users user = usersService.selectByUsersId(userId);
		String pickAddrName = request.getParameter("pickAddrsName");
		String pickAddr = request.getParameter("pickAddrs");

		// 如果订单为已预约=1，更新订单状态为已派工=2
		if (order.getOrderStatus() == Constants.ORDER_AM_STATUS_1) {
			order.setOrderStatus(Constants.ORDER_AM_STATUS_2);
			orderService.updateByPrimaryKeySelective(order);

			// 更新派工表 + 日志表
			dispatchStaffFromOrderService.disStaff(orgStaffs, order, user, poiLongitude, poiLatitude, pickAddrName, pickAddr, userAddrDistance);
			
			SmsUtil.SendSms(user.getMobile(), Constants.MESSAGE_ORDER_PAY_HINT, new String[]{});
		}
		return "redirect:/order/order-am-list";
	}

	/*
	 * 配送订单确认派工： 更新派工表，更新日志表
	 * 
	 * @param 订单用户Id, 订单Id, 订单（有过派工） 对应 的阿姨 Id
	 */
	@RequestMapping(value = "/disStaffByDelOrderNo", method = RequestMethod.POST)
	public String disStaffForDelOrder(Long userId, Long id, Long staffId) {

		OrgStaffs orgStaffs = orgStaService.selectByPrimaryKey(staffId);
		Orders order = orderService.selectByPrimaryKey(id);
		Users user = usersService.selectByUsersId(userId);

		// 如果订单为已支付=4，更新订单状态为服务中=5
		if (order.getOrderStatus() == Constants.ORDER_STATUS_4) {
			order.setOrderStatus(Constants.ORDER_STATUS_5);
			orderService.updateByPrimaryKeySelective(order);

			// 更新派工表 + 日志表
			dispatchStaffFromOrderService.disStaff(orgStaffs, order, user);

			// 后期增加给服务人员推送消息

		}
		return "redirect:/order/order-del-list";
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
		Users user = usersService.selectByUsersId(userId);

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
		OrderDispatchs disStatuYes = disService.selectByOrderId(id);
		
		if (disStatuYes != null) {
			Long staId = disStatuYes.getStaffId();
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

	/*
	 * 提交 已预约 的 助理订单。。变为 已确认。。并生成 订单价格等信息
	 */

	@RequestMapping(value = "oa_submit_am_order.json", method = RequestMethod.GET)
	public AppResultData<Object> oaSubAmOrder(@RequestParam("orderId") Long orderId, @RequestParam("remarksConfirm") String remarksCon,
			@RequestParam("orderMoney") BigDecimal orderMoney,
			@RequestParam(value = "serviceDateStart", required = false, defaultValue = "0") Long serviceDateStart,
			@RequestParam(value = "serviceDateEnd", required = false, defaultValue = "0") Long serviceDateEnd) throws UnsupportedEncodingException,
			ParseException {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");

		Orders order = orderService.selectbyOrderId(orderId);

		Short orderStatus = order.getOrderStatus();

		// 如果 订单状态不是 已预约。则 不让提交 订单
		if (orderStatus != Constants.ORDER_AM_STATUS_1) {

			result.setStatus(Constants.ERROR_999);
			result.setMsg("只有已预约的订单,可以修改订单价格信息");

			return result;
		}

		/*
		 * decode() 需要处理 特殊字符%
		 */
		remarksCon = remarksCon.replaceAll("%(?![0-9a-fA-F]{2})", "%25");

		String decode = URLDecoder.decode(remarksCon, "UTF-8");

		// 修改订单为 已确认
		order.setOrderStatus(Constants.ORDER_AM_STATUS_2);
		order.setRemarksConfirm(decode);

		/*
		 * 对于 大类 为 深度养护的 订单，需要 设置 服务 时间 和 服务时长
		 */
		Long serviceType = order.getServiceType();
		PartnerServiceType partnerServiceType = partService.selectByPrimaryKey(serviceType);

		if (partnerServiceType.getParentId() == 26) {

			// 向上 取整, 有小数 都 +1
			double floor = Math.ceil((serviceDateEnd - serviceDateStart) / 3600);
			order.setServiceHour((short) floor);
			order.setServiceDate(serviceDateStart);
		}

		OrderPrices orderPrice = priceService.initOrderPrices();

		orderPrice.setUserId(order.getUserId());
		orderPrice.setMobile(order.getMobile());
		orderPrice.setOrderId(orderId);
		orderPrice.setOrderNo(order.getOrderNo());

		orderPrice.setOrderMoney(orderMoney);

		// 更新订单状态
		orderService.updateByPrimaryKeySelective(order);

		// 生成订单价格
		priceService.insertSelective(orderPrice);

		/*
		 * 2016年4月14日11:05:05 新增短信
		 * 
		 * 客服完善订单信息
		 * 
		 * 对于 助理 类订单。 订单由 已预约--已确认（客服在平台手动完成）
		 * 
		 * 发短信
		 */

		Long userId = order.getUserId();

		Users users = usersService.selectByUsersId(userId);

//		String[] paySuccessForUser = new String[] {"您的订单已确认，请在30分钟之内支付"};
//
//		SmsUtil.SendSms(users.getMobile(), Constants.MESSAGE_ORDER_NEED_TO_PAY, paySuccessForUser);

		// 发短信
		orderService.userOrderAmSuccessTodo(order.getOrderNo());

		result.setMsg("订单确认成功");

		return result;
	}

	/**
	 * 运营人员 为订单 添加 备注
	 */

	@RequestMapping(value = "remarks_bussiness_form", method = RequestMethod.GET)
	public String toBussinessRemarkForm(Model model, @RequestParam("orderId") Long orderId) {

		Orders orders = orderService.selectbyOrderId(orderId);

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
