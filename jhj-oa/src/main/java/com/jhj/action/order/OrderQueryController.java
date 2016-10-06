package com.jhj.action.order;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AccountAuth;
import com.jhj.oa.auth.AccountRole;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.cooperate.CooperateBusinessService;
import com.jhj.service.order.DispatchStaffFromOrderService;
import com.jhj.service.order.OaOrderService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPayService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.order.poi.PoiExportExcelService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.dict.CooperativeBusinessSearchVo;
import com.jhj.vo.order.OaOrderListNewVo;
import com.jhj.vo.order.OaOrderListVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.order.OrderServiceAddonViewVo;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.poi.POIUtils;

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
public class OrderQueryController extends BaseController {
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
	private PoiExportExcelService poiExcelService;
	
	@Autowired
	private OrderServiceAddonsService orderServiceAddonsService;

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

		// 查询条件： 设置为钟点工的订单
		if (searchVo == null)
			searchVo = new OrderSearchVo();
		Long sessionParentId = AuthHelper.getSessionLoginOrg(request);
		
		Short orderType = Constants.ORDER_TYPE_0;

		searchVo = orderQueryService.getOrderSearchVo(request, searchVo, Constants.ORDER_TYPE_0, sessionParentId);

		PageInfo result = orderQueryService.selectByListPage(searchVo, pageNo, pageSize);

		List<Orders> orderList = result.getList();
		Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaOrderListNewVo completeVo = oaOrderService.completeNewVo(orders);
			orderList.set(i, completeVo);
		}

		result = new PageInfo(orderList);

		String startTimeStr = request.getParameter("startTimeStr");
		if (!StringUtil.isEmpty(startTimeStr))
			model.addAttribute("startTimeStr", startTimeStr);

		// 下单结束时间
		String endTimeStr = request.getParameter("endTimeStr");
		if (!StringUtil.isEmpty(endTimeStr))
			model.addAttribute("endTimeStr", endTimeStr);
		// 服务开始时间
		String serviceStartTime = request.getParameter("serviceStartTime");
		if (!StringUtil.isEmpty(serviceStartTime))
			model.addAttribute("serviceStartTime", serviceStartTime);

		// 服务结束时间
		String serviceEndTimeStr = request.getParameter("serviceEndTimeStr");
		if (!StringUtil.isEmpty(serviceEndTimeStr))
			model.addAttribute("serviceEndTimeStr", serviceEndTimeStr);
		CooperativeBusinessSearchVo businessSearchVo=new CooperativeBusinessSearchVo();
		businessSearchVo.setEnable((short)1);
		List<CooperativeBusiness> businessList = cooperateBusinessService.selectCooperativeBusinessVo(businessSearchVo);
		model.addAttribute("businessList", businessList);
		
		model.addAttribute("loginOrgId", sessionParentId); // 当前登录的 id,动态显示搜索 条件
		model.addAttribute("oaOrderListVoModel", result);
		model.addAttribute("searchModel", searchVo);
		model.addAttribute("listUrl", "order-hour-list");


		return "order/orderList";
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

		model.addAttribute("oaOrderListVoModel", oaOrderListVo);

		// 得到 当前登录 的 店长所在 门店id，
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {

			/*
			 * 派工调整时，选择 门店
			 */
			model.addAttribute("loginOrgId", sessionOrgId);
		}

		return "order/orderHourViewForm";
		
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@AuthPassport
	@RequestMapping(value = "/order-exp-list", method = RequestMethod.GET)
	public String getOrderDeepList(Model model, HttpServletRequest request, OrderSearchVo searchVo) throws ParseException, UnsupportedEncodingException {

		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ConstantOa.DEFAULT_PAGE_SIZE;
		// 分页

		// 查询条件： 设置为钟点工的订单
		if (searchVo == null)
			searchVo = new OrderSearchVo();
		Long sessionParentId = AuthHelper.getSessionLoginOrg(request);
		
		Short orderType = Constants.ORDER_TYPE_1;

		searchVo = orderQueryService.getOrderSearchVo(request, searchVo, orderType, sessionParentId);

		PageInfo result = orderQueryService.selectByListPage(searchVo, pageNo, pageSize);

		List<Orders> orderList = result.getList();
		Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaOrderListNewVo completeVo = oaOrderService.completeNewVo(orders);
			orderList.set(i, completeVo);
		}

		result = new PageInfo(orderList);

		String startTimeStr = request.getParameter("startTimeStr");
		if (!StringUtil.isEmpty(startTimeStr))
			model.addAttribute("startTimeStr", startTimeStr);

		// 下单结束时间
		String endTimeStr = request.getParameter("endTimeStr");
		if (!StringUtil.isEmpty(endTimeStr))
			model.addAttribute("endTimeStr", endTimeStr);
		// 服务开始时间
		String serviceStartTime = request.getParameter("serviceStartTime");
		if (!StringUtil.isEmpty(serviceStartTime))
			model.addAttribute("serviceStartTime", serviceStartTime);

		// 服务结束时间
		String serviceEndTimeStr = request.getParameter("serviceEndTimeStr");
		if (!StringUtil.isEmpty(serviceEndTimeStr))
			model.addAttribute("serviceEndTimeStr", serviceEndTimeStr);
		
		CooperativeBusinessSearchVo businessSearchVo=new CooperativeBusinessSearchVo();
		businessSearchVo.setEnable((short)1);
		List<CooperativeBusiness> businessList = cooperateBusinessService.selectCooperativeBusinessVo(businessSearchVo);
		model.addAttribute("businessList", businessList);

		model.addAttribute("loginOrgId", sessionParentId); // 当前登录的 id,动态显示搜索 条件
		model.addAttribute("oaOrderListVoModel", result);
		model.addAttribute("searchModel", searchVo);
		model.addAttribute("listUrl", "order-exp-list");

		return "order/orderList";
	}	

	/**
	 * 深度保洁---订单详情
	 * 
	 * 
	 * @param orderNo
	 * @param disStatus
	 * 
	 * @param model
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/order-exp-view", method = RequestMethod.GET)
	public String orderExpDetail(HttpServletRequest request, String orderNo, Short disStatus, Model model) {
		OaOrderListVo oaOrderListVo = oaOrderService.getOrderExpVoDetail(orderNo, disStatus);
		model.addAttribute("oaOrderListVoModel", oaOrderListVo);
		
		// 得到 当前登录 的 店长所在 门店id，
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {

			/*
			 * 派工调整时，选择 门店
			 */
			model.addAttribute("loginOrgId", sessionOrgId);
		}
		
		//服务附加信息
		List<OrderServiceAddonViewVo> orderAddonVos = new ArrayList<OrderServiceAddonViewVo>();
		Long orderId = oaOrderListVo.getId();
		List<OrderServiceAddons> orderAddons = orderServiceAddonsService.selectByOrderId(orderId);
		
		if (!orderAddons.isEmpty()) {
			orderAddonVos = orderServiceAddonsService.changeToOrderServiceAddons(orderAddons);
		}
		model.addAttribute("orderAddonVos", orderAddonVos);
		
		
		return "order/orderExpViewForm";		
	}

	/*
	 * 导出订单列表
	 */
	@RequestMapping(value = "export_base_order", method = RequestMethod.GET)
	public void exportBaseOrderTable(OrderSearchVo searchVo, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {

		if (searchVo == null)
			searchVo = new OrderSearchVo();
		Long sessionParentId = AuthHelper.getSessionLoginOrg(request);

		searchVo = orderQueryService.getOrderSearchVo(request, searchVo, Constants.ORDER_TYPE_0, sessionParentId);

		List<Orders> orderList = orderQueryService.selectBySearchVo(searchVo);

		/*
		 * 2. 转换导出字段为 页面的 vo字段
		 */
		List<OaOrderListNewVo> voList = new ArrayList<OaOrderListNewVo>();

		for (Orders orders : orderList) {
			voList.add(oaOrderService.completeNewVo(orders));
		}

		String fileName = "基础服务订单列表";

		/*
		 * 3. 映射 excel 字段和数据
		 */
		List<Map<String, Object>> list = poiExcelService.createExcelRecord(voList);

		String columnNames[] = { "门店名称", "云店名称", "服务人员", "下单时间", "订单类型", "服务日期", "用户手机号", "服务地址", "订单状态", "支付方式", "总金额", "支付金额" };// 列名

		String keys[] = { "orgName", "cloudOrgName", "staffName", "addTime", "orderTypeName", "serviceDate", "mobile", "orderAddress", "orderStatusName",
				"payTypeName", "orderMoney", "orderPay" };// map中的key

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			POIUtils.createWorkBook(list, keys, columnNames).write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}

}
