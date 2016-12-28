package com.jhj.action.staff;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.service.bs.OrgStaffDetailPayService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrderStatService;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.staff.OrgStaffDetailPayOaVo;
import com.jhj.vo.staff.OrgStaffPayVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

@Controller
@RequestMapping(value = "/staff")
public class OrgStaffDetailPayController extends BaseController {
	@Autowired
	private OrgStaffDetailPayService orgStaffDetailPayService;

	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private OrgsService orgService;
	
	@Autowired
	private OrderPricesService orderPriceService;
	
	@Autowired
	private OrderPriceExtService orderPriceExtService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@Autowired
	private OrderStatService orderStatService;

	/**
	 * 服务人员财务明细
	 * 
	 * @param model
	 * @param request
	 * @param searchVo
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException 
	 */
	@AuthPassport
	@RequestMapping(value = "/staffPay-list", method = RequestMethod.GET)
	public String getStaffPayList(Model model, HttpServletRequest request, 
			@RequestParam(value = "staff_id", required = false, defaultValue = "0") Long staffId,
			OrderSearchVo searchVo) throws ParseException, UnsupportedEncodingException {

		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		// 分页
		PageHelper.startPage(pageNo, pageSize);

		if (searchVo == null) {
			searchVo = new OrderSearchVo();
		}
		
		if (staffId > 0L) searchVo.setStaffId(staffId);
		
		Long sessionParentId = AuthHelper.getSessionLoginOrg(request);
		searchVo = orderQueryService.getOrderSearchVo(request, searchVo, null, sessionParentId);
		
		// 服务开始时间
		String serviceStartTime = request.getParameter("serviceStartTimeStr");
		if (!StringUtil.isEmpty(serviceStartTime)) {

			searchVo.setStartAddTime(TimeStampUtil.getMillisOfDayFull(serviceStartTime+" 00:00:00") / 1000);
		}
		// 服务结束时间
		String serviceEndTimeStr = request.getParameter("serviceEndTimeStr");
		if (!StringUtil.isEmpty(serviceEndTimeStr)) {
			

			
			searchVo.setEndAddTime(TimeStampUtil.getMillisOfDayFull(serviceEndTimeStr+" 23:59:59") / 1000);
		}
		
		
		List<OrgStaffDetailPay> orgStaffdetailPayList = new ArrayList<OrgStaffDetailPay>();
		//目前仅支持查询某一个时间段，不支持查询全部的.
		if (searchVo.getStartServiceTime() == null) {
			PageInfo resultDefault = new PageInfo(orgStaffdetailPayList);

			model.addAttribute("contentModel", resultDefault);
			model.addAttribute("searchModel", searchVo);
			model.addAttribute("totalOrderMoney", 0);
			return "staff/staffDetailPayList";
		}
	
		PageInfo plist = orgStaffDetailPayService.selectByListPage(searchVo, pageNo, pageSize);
		orgStaffdetailPayList = plist.getList();
		
		List<OrgStaffDetailPayOaVo> orgStaffPayVoList = new ArrayList<OrgStaffDetailPayOaVo>();
		for (int i = 0; i < orgStaffdetailPayList.size(); i++) {
			OrgStaffDetailPay orgStaffDetailPay = orgStaffdetailPayList.get(i);
			OrgStaffDetailPayOaVo oaVo = orgStaffDetailPayService.getOrgStaffPayOaVo(orgStaffDetailPay);
			orgStaffdetailPayList.set(i, oaVo);
		}
		PageInfo result = new PageInfo(orgStaffdetailPayList);

		model.addAttribute("contentModel", result);
		model.addAttribute("searchModel", searchVo);
		
		//统计查询
//		Map<String, Double> statisticalData = orgStaffDetailPayService.selectTotalData(searchVo);
//		model.addAllAttributes(statisticalData);
		
		//1. 订单总金额
		OrderSearchVo statSearchVo = new OrderSearchVo();
		if (staffId > 0L) searchVo.setStaffId(staffId);
		statSearchVo = orderQueryService.getOrderSearchVo(request, statSearchVo, null, sessionParentId);
		
		List<Short> orderStatusList = new ArrayList<Short>();
		orderStatusList.add(Constants.ORDER_HOUR_STATUS_2);
		orderStatusList.add(Constants.ORDER_HOUR_STATUS_3);
		orderStatusList.add(Constants.ORDER_HOUR_STATUS_5);
		orderStatusList.add(Constants.ORDER_HOUR_STATUS_7);
		orderStatusList.add(Constants.ORDER_HOUR_STATUS_8);
		statSearchVo.setOrderStatusList(orderStatusList);
		
		Map<String ,String> orderStats = orderStatService.getTotalOrderMoneyMultiStat(statSearchVo);

		model.addAttribute("totalOrderMoney", orderStats.get("totalOrderMoney").toString());
		model.addAttribute("totalOrderPay", orderStats.get("totalOrderPay").toString());
		model.addAttribute("totalOrderCoupon", orderStats.get("totalOrderCoupon").toString());
		model.addAttribute("totalOrderIncoming", orderStats.get("totalOrderIncoming").toString());
		model.addAttribute("totalOrderPayType0", orderStats.get("totalOrderPayType0").toString());
		model.addAttribute("totalOrderPayType1", orderStats.get("totalOrderPayType1").toString());
		model.addAttribute("totalOrderPayType2", orderStats.get("totalOrderPayType2").toString());
		model.addAttribute("totalOrderPayType6", orderStats.get("totalOrderPayType6").toString());
		model.addAttribute("totalOrderPayType7", orderStats.get("totalOrderPayType7").toString());
		
		
		return "staff/staffDetailPayList";
	}
}
