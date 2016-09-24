package com.jhj.action.order;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.order.OaOrderService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.order.poi.PoiExportExcelService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.order.OaOrderListNewVo;
import com.jhj.vo.order.OaOrderSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.org.OrgSearchVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.poi.POIUtils;

/**
 *
 * @author :hulj
 * @Date : 2016年5月27日下午5:24:55
 * @Description:
 *
 *               导出表中数据到 excel
 */
@Controller
@RequestMapping(value = "/order")
public class OrderExportController extends BaseController {

	@Autowired
	private OrdersMapper orderMapper;

	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private OaOrderService oaOrderService;

	@Autowired
	private PoiExportExcelService poiExcelService;

	@Autowired
	private OrgsService orgService;

	@Autowired
	private PartnerServiceTypeService partService;
	
	@Autowired
	private OrderQueryService orderQueryService;

	/*
	 * 导出 钟点工订单列表
	 */
	@RequestMapping(value = "export_base_order", method = RequestMethod.GET)
	public void exportBaseOrderTable(OrderSearchVo searchVo, HttpServletRequest request, HttpServletResponse response) throws IOException,
			ParseException {

		// 查询条件的组合，需要做一些逻辑判断
		// 1. 如果为运营人员，则可以看所有的门店和所有状态
		// 2. 如果为店长，则只能看当前门店和已派工到该门店的人员.

		// 查询条件： 设置为钟点工的订单
		if (searchVo == null) {
			searchVo = new OrderSearchVo();
		}

		// 此方法只查普通服务
		searchVo.setOrderType(Constants.ORDER_TYPE_0);

		// 判断是否为店长登陆，如果org > 0L ，则为某个店长，否则为运营人员.
		Long sessionParentId = AuthHelper.getSessionLoginOrg(request);
		if (sessionParentId > 0L)
			searchVo.setParentId(sessionParentId);
		// 处理查询条件云店--------------------------------开始
		// 1) 如果有查询条件云店org_id，则以查询条件的云店为准
		// 2) 如果没有查询条件，则判断是否为店长，并且只能看店长所在门店下的所有云店.

		Long parentId = 0L;
		String parentIdParam = request.getParameter("parentId");
		if (!StringUtil.isEmpty(parentIdParam))
			parentId = Long.valueOf(parentIdParam);

		if (parentId > 0L)
			searchVo.setParentId(parentId);

		Long orgId = 0L;
		String orgIdParam = request.getParameter("orgId");

		if (!StringUtil.isEmpty(orgIdParam))
			orgId = Long.valueOf(orgIdParam);

		if (orgId > 0L)
			searchVo.setOrgId(orgId);

		// 处理查询时间条件--------------------------------开始
		// 下单开始时间
		String startTimeStr = request.getParameter("startTimeStr");
		if (!StringUtil.isEmpty(startTimeStr)) {
			searchVo.setStartAddTime(TimeStampUtil.getMillisOfDay(startTimeStr) / 1000);
		}

		// 下单结束时间
		String endTimeStr = request.getParameter("endTimeStr");
		if (!StringUtil.isEmpty(endTimeStr)) {
			searchVo.setEndAddTime(TimeStampUtil.getMillisOfDay(startTimeStr) / 1000);
		}

		// 服务开始时间
		String serviceStartTime = request.getParameter("serviceStartTime");
		if (!StringUtil.isEmpty(serviceStartTime)) {
			searchVo.setStartServiceTime(TimeStampUtil.getMillisOfDay(serviceStartTime) / 1000);
		}
		// 服务结束时间
		String serviceEndTimeStr = request.getParameter("serviceEndTimeStr");
		if (!StringUtil.isEmpty(serviceEndTimeStr)) {
			searchVo.setEndServiceTime(TimeStampUtil.getMillisOfDay(serviceEndTimeStr) / 1000);
		}
		// 处理查询时间条件--------------------------------结束

		// 处理查询状态条件--------------------------------开始
		if (searchVo.getOrderStatus() == null) {
			// 如果为店长只能看已派工状态之后的订单.
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

		String addrName = searchVo.getAddrName();
		if (!StringUtil.isEmpty(addrName)) {
			addrName = new String(addrName.getBytes("ISO-8859-1"), "UTF-8");
			searchVo.setAddrName(addrName);
			;
		}

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
