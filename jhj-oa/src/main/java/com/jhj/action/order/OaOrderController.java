package com.jhj.action.order;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.DispatchStaffFromOrderService;
import com.jhj.service.order.OaOrderService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.OaOrderSearchVo;
import com.jhj.vo.order.OaOrderListNewVo;
import com.jhj.vo.order.OaOrderListVo;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.meijia.utils.StringUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年8月10日下午6:12:56
 * @Description: 
 *		
 *		运营平台--订单管理模块
 */
@Controller
@RequestMapping(value = "/order")
public class OaOrderController extends BaseController {
	@Autowired
	private OaOrderService oaOrderService;
	@Autowired
	private OrderDispatchsService orderDisService;
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
	
	/*
	 * 查看订单列表
	 */
	@AuthPassport
	@RequestMapping(value = "/order-list", method = RequestMethod.GET)
	public String getOrderList(Model model, HttpServletRequest request, OaOrderSearchVo oaOrderSearchVo){
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		if(oaOrderSearchVo == null){
			oaOrderSearchVo  = new OaOrderSearchVo();
		}
		
		//得到 当前登录 的 门店id，并作为搜索条件
		String org = AuthHelper.getSessionLoginOrg(request);
		
		if(!org.equals("0") && !StringUtil.isEmpty(org)){
			//未选择 门店， 且 当前 登录 用户 为 店长 （  session中的  orgId 不为 0）,设置搜索条件为  店长的门店
			oaOrderSearchVo.setSearchOrgId(Long.valueOf(org));
		}
		
		//如果在 订单列表页面，选择了 门店  作为搜索 条件
		String jspOrgId = request.getParameter("orgId");
		if(!StringUtil.isEmpty(jspOrgId) && !jspOrgId.equals("0")){
			oaOrderSearchVo.setSearchOrgId(Long.valueOf(jspOrgId));
		}
		
        List<Orders> orderList = oaOrderService.selectVoByListPage(oaOrderSearchVo,pageNo,pageSize);
		
        Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaOrderListNewVo completeVo = oaOrderService.completeVo(orders);
			orderList.set(i, completeVo);
		}
		
		PageInfo result = new PageInfo(orderList);	
		
		
		model.addAttribute("loginOrgId", org);	//当前登录的 id,动态显示搜索 条件
		model.addAttribute("oaOrderListVoModel", result);
		model.addAttribute("oaOrderSearchVoModel", oaOrderSearchVo);
		
		return "order/orderList";
	}
	/**
	 * 钟点工-----订单列表---orderType=0
	 * @param model
	 * @param request
	 * @param oaOrderSearchVo
	 * @return
	 */
//	@AuthPassport
	@RequestMapping(value = "/order-hour-list", method = RequestMethod.GET)
	public String getOrderHourList(Model model, HttpServletRequest request, OaOrderSearchVo oaOrderSearchVo){
	
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		if(oaOrderSearchVo == null){
			oaOrderSearchVo  = new OaOrderSearchVo();
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_0);
		}else {
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_0);
		}
		
		//得到 当前登录 的 门店id，并作为搜索条件
		String org = AuthHelper.getSessionLoginOrg(request);
		
		if(!org.equals("0") && !StringUtil.isEmpty(org)){
			//未选择 门店， 且 当前 登录 用户 为 店长 （  session中的  orgId 不为 0）,设置搜索条件为  店长的门店
			oaOrderSearchVo.setSearchOrgId(Long.valueOf(org));
		}
		
		//如果在 订单列表页面，选择了 门店  作为搜索 条件
		String jspOrgId = request.getParameter("orgId");
		if(!StringUtil.isEmpty(jspOrgId) && !jspOrgId.equals("0")){
			oaOrderSearchVo.setSearchOrgId(Long.valueOf(jspOrgId));
		}
		
        List<Orders> orderList = oaOrderService.selectVoByListPage(oaOrderSearchVo,pageNo,pageSize);
		
        Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaOrderListNewVo completeVo = oaOrderService.completeNewVo(orders);
			orderList.set(i, completeVo);
		}
		
		PageInfo result = new PageInfo(orderList);	
		
		
		model.addAttribute("loginOrgId", org);	//当前登录的 id,动态显示搜索 条件
		model.addAttribute("oaOrderListVoModel", result);
		model.addAttribute("oaOrderSearchVoModel", oaOrderSearchVo);
		
		return "order/orderHourList";
	}
	/**
	 * 深度保洁---订单列表---orderType=1
	 * @param model
	 * @param request
	 * @param oaOrderSearchVo
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/order-exp-list", method = RequestMethod.GET)
	public String getOrderExpList(Model model, HttpServletRequest request, OaOrderSearchVo oaOrderSearchVo){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		if(oaOrderSearchVo == null){
			oaOrderSearchVo  = new OaOrderSearchVo();
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_1);
		}else {
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_1);
		}
		
		//得到 当前登录 的 门店id，并作为搜索条件
		String org = AuthHelper.getSessionLoginOrg(request);
		
		if(!org.equals("0") && !StringUtil.isEmpty(org)){
			//未选择 门店， 且 当前 登录 用户 为 店长 （  session中的  orgId 不为 0）,设置搜索条件为  店长的门店
			oaOrderSearchVo.setSearchOrgId(Long.valueOf(org));
		}
		
		//如果在 订单列表页面，选择了 门店  作为搜索 条件
		String jspOrgId = request.getParameter("orgId");
		if(!StringUtil.isEmpty(jspOrgId) && !jspOrgId.equals("0")){
			oaOrderSearchVo.setSearchOrgId(Long.valueOf(jspOrgId));
		}
		
        List<Orders> orderList = oaOrderService.selectVoByListPage(oaOrderSearchVo,pageNo,pageSize);
		
        Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaOrderListNewVo completeVo = oaOrderService.completeNewVo(orders);
			orderList.set(i, completeVo);
		}
		
		PageInfo result = new PageInfo(orderList);	
		
		
		model.addAttribute("loginOrgId", org);	//当前登录的 id,动态显示搜索 条件
		model.addAttribute("oaOrderListVoModel", result);
		model.addAttribute("oaOrderSearchVoModel", oaOrderSearchVo);
		
		return "order/orderExpList";
	}
	
	/**
	 * 助理---订单列表---orderType=2
	 * @param model
	 * @param request
	 * @param oaOrderSearchVo
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/order-am-list", method = RequestMethod.GET)
	public String getOrderAmList(Model model, HttpServletRequest request, OaOrderSearchVo oaOrderSearchVo){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		if(oaOrderSearchVo == null){
			oaOrderSearchVo  = new OaOrderSearchVo();
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_2);
		}else {
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_2);
		}
		
		//得到 当前登录 的 门店id，并作为搜索条件
		String org = AuthHelper.getSessionLoginOrg(request);
		
		if(!org.equals("0") && !StringUtil.isEmpty(org)){
			//未选择 门店， 且 当前 登录 用户 为 店长 （  session中的  orgId 不为 0）,设置搜索条件为  店长的门店
			oaOrderSearchVo.setSearchOrgId(Long.valueOf(org));
		}
		
		//如果在 订单列表页面，选择了 门店  作为搜索 条件
		String jspOrgId = request.getParameter("orgId");
		if(!StringUtil.isEmpty(jspOrgId) && !jspOrgId.equals("0")){
			oaOrderSearchVo.setSearchOrgId(Long.valueOf(jspOrgId));
		}
		
        List<Orders> orderList = oaOrderService.selectVoByListPage(oaOrderSearchVo,pageNo,pageSize);
		
        Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaOrderListNewVo completeVo = oaOrderService.completeNewVo(orders);
			orderList.set(i, completeVo);
		}
		
		PageInfo result = new PageInfo(orderList);	
		
		
		model.addAttribute("loginOrgId", org);	//当前登录的 id,动态显示搜索 条件
		model.addAttribute("oaOrderListVoModel", result);
		model.addAttribute("oaOrderSearchVoModel", oaOrderSearchVo);
		
		return "order/orderAmList";
	}
	/**
	 * 配送--- 订单列表
	 * @param model
	 * @param request
	 * @param oaOrderSearchVo
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/order-del-list", method = RequestMethod.GET)
	public String getOrderDelList(Model model, HttpServletRequest request, OaOrderSearchVo oaOrderSearchVo){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		if(oaOrderSearchVo == null){
			oaOrderSearchVo  = new OaOrderSearchVo();
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_3);
		}else {
			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_3);
		}
		
		//得到 当前登录 的 门店id，并作为搜索条件
		String org = AuthHelper.getSessionLoginOrg(request);
		
		if(!org.equals("0") && !StringUtil.isEmpty(org)){
			//未选择 门店， 且 当前 登录 用户 为 店长 （  session中的  orgId 不为 0）,设置搜索条件为  店长的门店
			oaOrderSearchVo.setSearchOrgId(Long.valueOf(org));
		}
		
		//如果在 订单列表页面，选择了 门店  作为搜索 条件
		String jspOrgId = request.getParameter("orgId");
		if(!StringUtil.isEmpty(jspOrgId) && !jspOrgId.equals("0")){
			oaOrderSearchVo.setSearchOrgId(Long.valueOf(jspOrgId));
		}
		
        List<Orders> orderList = oaOrderService.selectVoByListPage(oaOrderSearchVo,pageNo,pageSize);
		
        Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaOrderListNewVo completeVo = oaOrderService.completeNewVo(orders);
			orderList.set(i, completeVo);
		}
		
		PageInfo result = new PageInfo(orderList);	
		
		model.addAttribute("loginOrgId", org);	//当前登录的 id,动态显示搜索 条件
		model.addAttribute("oaOrderListVoModel", result);
		model.addAttribute("oaOrderSearchVoModel", oaOrderSearchVo);
		
		return "order/orderDelList";
	}
	
	
	
	/*
	 * 查看订单详情
	 * 
	 * 	@param 订单号。当前 派工状态
	 * 	
	 */
	@AuthPassport
	@RequestMapping(value = "/orderView",method = RequestMethod.GET)
	public String  orderDetail(String orderNo,Short disStatus,Model model){
		OaOrderListVo oaOrderListVo = oaOrderService.getOrderVoDetail(orderNo,disStatus);
		model.addAttribute("oaOrderListVoModel", oaOrderListVo);
			return "order/orderViewForm";
	}
	/**
	 * 钟点工---订单详情
	 * @param orderNo
	 * @param disStatus
	 * @param model
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/order-hour-view",method = RequestMethod.GET)
	public String  orderHourDetail(String orderNo,Short disStatus,Model model){
		OaOrderListVo oaOrderListVo = oaOrderService.getOrderVoDetailHour(orderNo,disStatus);
		model.addAttribute("oaOrderListVoModel", oaOrderListVo);
		short orderType = oaOrderListVo.getOrderType();
		if(orderType==Constants.ORDER_TYPE_0){//钟点工
			return "order/orderHourViewForm";
		}else {
			return "order/orderViewForm";
		}
	}
	/**
	 * 深度保洁---订单详情
	 * @param orderNo
	 * @param disStatus
	 * @param model
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/order-exp-view",method = RequestMethod.GET)
	public String  orderExpDetail(String orderNo,Short disStatus,Model model){
		OaOrderListVo oaOrderListVo = oaOrderService.getOrderExpVoDetail(orderNo,disStatus);
		model.addAttribute("oaOrderListVoModel", oaOrderListVo);
		short orderType = oaOrderListVo.getOrderType();
		 if (orderType==Constants.ORDER_TYPE_1) {//深度保洁
			return "order/orderExpViewForm";
		}else {
			return "order/orderViewForm";
		}
	}
	/**
	 * 助理---订单详情
	 * @param orderNo
	 * @param disStatus
	 * @param model
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/order-am-view",method = RequestMethod.GET)
	public String  orderAmDetail(String orderNo,Short disStatus,Model model){
		OaOrderListVo oaOrderListVo = oaOrderService.getOrderVoDetailAm(orderNo,disStatus);
		oaOrderListVo.setFlag("0");//标识是否显示服务人员列表
		model.addAttribute("oaOrderListVoModel", oaOrderListVo);
		short orderType = oaOrderListVo.getOrderType();
		 if (orderType==Constants.ORDER_TYPE_2) {//助理
			return "order/orderAmViewForm";
		}else {
			return "order/orderViewForm";
		}
	}
	/**
	 * 配送---订单详情
	 * @param orderNo
	 * @param disStatus
	 * @param model
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/order-del-view",method = RequestMethod.GET)
	public String  orderDelDetail(String orderNo,Short disStatus,Model model){
		OaOrderListVo oaOrderListVo = oaOrderService.getOrderVoDetailDel(orderNo,disStatus);
		model.addAttribute("oaOrderListVoModel", oaOrderListVo);
		short orderType = oaOrderListVo.getOrderType();
		 if (orderType==Constants.ORDER_TYPE_3) {//配送
			return "order/orderDelViewForm";
		}else {
			return "order/orderViewForm";
		}
	}
	/*
	 * 跳转显示可用服务人员列表
	 * @param orderNo
	 * @param disStatus
	 * @param model
	 * @param poiLongitude
	 * @param poiLatitude
	 * @param pickAddrName
	 * @param pickAddr
	 * @return
	 */
//	@AuthPassport
	@RequestMapping(value = "/order-am-staff",method = RequestMethod.POST)
	public String  orderAmDetail(String orderNo,Short disStatus,Model model,
		String poiLongitude,String poiLatitude,String pickAddrName,String pickAddr,String userAddrKey){
		OaOrderListVo oaOrderListVo = oaOrderService.getOrderVoDetailAm(orderNo,disStatus,poiLongitude,poiLatitude);
		oaOrderListVo.setPoiLongitude(poiLongitude);//服务地址经度
		oaOrderListVo.setPoiLatitude(poiLatitude);//服务地址维度
		oaOrderListVo.setPickAddr(pickAddr);//服务地址门牌号
		oaOrderListVo.setPickAddrName(pickAddrName);//服务地址名称
		oaOrderListVo.setUserAddrKey(userAddrKey);
		oaOrderListVo.setFlag("1");//是否显示服务人员列表1=显示，0=不显示
		model.addAttribute("oaOrderListVoModel", oaOrderListVo);
		short orderType = oaOrderListVo.getOrderType();
		 if (orderType==Constants.ORDER_TYPE_2) {//助理
			return "order/orderAmViewForm";
		}else {
			return "order/orderViewForm";
		}
	}
	/*
	 *  助理订单确认派工
	 *  @param 订单用户Id,  订单Id,  订单（有过派工） 对应 的阿姨 Id
	 */
	@RequestMapping(value = "/disStaffByAmOrderNo" ,method = RequestMethod.POST)
	public String disStaffForAmOrder(HttpServletRequest request,Long userId, Long id,Long staffId,
		String poiLongitude,String poiLatitude,String userAddrDistance){
		 
		OrgStaffs orgStaffs = orgStaService.selectByPrimaryKey(staffId);
		Orders order = orderService.selectByPrimaryKey(id);
		Users user = usersService.selectByUsersId(userId);
		String pickAddrName = request.getParameter("pickAddrsName");
		String pickAddr = request.getParameter("pickAddrs");
		
		//如果订单为已预约=1，更新订单状态为已派工=2
		if(order.getOrderStatus()==Constants.ORDER_AM_STATUS_1){
			order.setOrderStatus(Constants.ORDER_AM_STATUS_2);
			orderService.updateByPrimaryKeySelective(order);
			
			//更新派工表 + 日志表
			dispatchStaffFromOrderService.disStaff(orgStaffs, order, user,poiLongitude, poiLatitude, pickAddrName, pickAddr,userAddrDistance);
			
			//后期增加给服务人员推送消息
		}
		return "redirect:/order/order-am-list";
	}
	/*
	 *  配送订单确认派工：
	 *  更新派工表，更新日志表
	 *  @param 订单用户Id,  订单Id,  订单（有过派工） 对应 的阿姨 Id
	 */
	@RequestMapping(value = "/disStaffByDelOrderNo" ,method = RequestMethod.POST)
	public String disStaffForDelOrder(Long userId, Long id,Long staffId){
		
		OrgStaffs orgStaffs = orgStaService.selectByPrimaryKey(staffId);
		Orders order = orderService.selectByPrimaryKey(id);
		Users user = usersService.selectByUsersId(userId);
		
		//如果订单为已支付=4，更新订单状态为服务中=5
		if(order.getOrderStatus()==Constants.ORDER_STATUS_4){
			order.setOrderStatus(Constants.ORDER_STATUS_5);
			orderService.updateByPrimaryKeySelective(order);
			
			//更新派工表 + 日志表
			dispatchStaffFromOrderService.disStaff(orgStaffs, order, user);
			
			//后期增加给服务人员推送消息
			
		}
		return "redirect:/order/order-del-list";
	}
	/*
	 * 深度保洁派工----固定派工人员
	 * @param userId
	 * @param id
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/disStaffByExpOrderNo" ,method = RequestMethod.POST)
	public String disStaffForExpOrder(Long userId, Long id){
		
		OrgStaffs orgStaffs = orgStaService.selectByPrimaryKey(Long.valueOf("17"));
		Orders order = orderService.selectByPrimaryKey(id);
		Users user = usersService.selectByUsersId(userId);
		
		//如果订单为已支付=4，更新订单状态为服务中=5
		if(order.getOrderStatus()==Constants.ORDER_DEP_STATUS_2){//已支付的订单才进行派工
			order.setOrderStatus(Constants.ORDER_DEP_STATUS_3);
			orderService.updateByPrimaryKeySelective(order);
			
			//更新派工表 + 日志表
			dispatchStaffFromOrderService.disStaff(orgStaffs, order, user);
			
		}
		return "redirect:/order/order-exp-list";
	}
	
	
	
	
	
	/*
	 * 订单换人操作
	 *  @param 订单用户Id,  订单Id,  订单（有过派工） 对应 的阿姨 Id
	 */
	@RequestMapping(value = "/updateStaffByOrderNo" ,method = RequestMethod.POST)
	public String updateStaff(Long userId, Long id,Long staffId){
		
		
		/*
		 *  订单在 order_dispatch中  派工状态 最多有 两种
		 * 
		 * 如果 前台 传参  staffId，未发生变化。。则 无后续 更新及发短信操作
		 */
		
		boolean isChange = true;
		
		
		//如果 能 找到  派工 为 1  的 订单记录    , mybatis已修改。 selectByOrderId() 得到 的 是 有效派工  的记录 。 
		OrderDispatchs disStatuYes = orderDisService.selectByOrderId(id);
		
		if(disStatuYes !=null){
			Long staId = disStatuYes.getStaffId();
			// 如果 没有 选择 新的  staffId ，或 这  staId 为空。。
			if(staId == staffId || staId == 0L){
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
	
	@RequestMapping(value = "oa_submit_am_order.json",method =RequestMethod.GET)
	public AppResultData<Object> oaSubAmOrder(
			@RequestParam("orderId")Long orderId,
			@RequestParam("remarksConfirm")String remarksCon,
			@RequestParam("orderMoney")BigDecimal orderMoney){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		Orders order = orderService.selectbyOrderId(orderId);
		
		Short orderStatus = order.getOrderStatus();
		
		//如果 订单状态不是  已预约。则 不让提交 订单
		if(orderStatus != Constants.ORDER_AM_STATUS_1){
				
			result.setStatus(Constants.ERROR_999);
			result.setMsg("只有已预约的订单,可以修改订单价格信息");
			
			return result;
		}
		
		String decode = URLDecoder.decode(remarksCon);
		
		// 修改订单为 已确认
		order.setOrderStatus(Constants.ORDER_AM_STATUS_2);
		order.setRemarksConfirm(decode);
		
		OrderPrices orderPrice = priceService.initOrderPrices();
		
		orderPrice.setUserId(order.getUserId());
		orderPrice.setMobile(order.getMobile());
		orderPrice.setOrderId(orderId);
		orderPrice.setOrderNo(order.getOrderNo());

		orderPrice.setOrderMoney(orderMoney);
		
		// 更新订单状态
		orderService.updateByPrimaryKeySelective(order);
		
		//生成订单价格
		priceService.insertSelective(orderPrice);
		
		//发短信
		orderService.userOrderAmSuccessTodo(order.getOrderNo());
		
		result.setMsg("订单确认成功");
		
		return result;
	}
	
}
