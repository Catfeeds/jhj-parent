package com.jhj.action.staff;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.service.bs.OrgStaffDetailPayService;
import com.jhj.service.bs.OrgStaffPayDeptService;
import com.jhj.service.order.OrderDispatchPriceService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.staff.OrgStaffDetailPayOaVo;
import com.meijia.utils.MathBigDecimalUtil;

@Controller
@RequestMapping(value = "/staff")
public class OrgStaffDetailPayController extends BaseController {
	
	@Autowired
	private OrgStaffDetailPayService orgStaffDetailPayService;

	@Autowired
	private OrderQueryService orderQueryService;
	
	@Autowired
	private OrderDispatchPriceService orderDispatchPriceService;
	
	@Autowired
	private OrgStaffPayDeptService orgStaffPayDeptService;
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
			OrderSearchVo searchVo) {

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
		Short[] orderStatusArry = {7,8};
		searchVo.setOrderStatusList(Arrays.asList(orderStatusArry));
				
		List<OrgStaffDetailPay> orgStaffdetailPayList = new ArrayList<OrgStaffDetailPay>();

		PageInfo<OrgStaffDetailPay> plist = orgStaffDetailPayService.selectByListPage(searchVo, pageNo, pageSize);
		orgStaffdetailPayList = plist.getList();
		
		for (int i = 0; i < orgStaffdetailPayList.size(); i++) {
			OrgStaffDetailPay orgStaffDetailPay = orgStaffdetailPayList.get(i);
			OrgStaffDetailPayOaVo oaVo = orgStaffDetailPayService.getOrgStaffPayOaVo(orgStaffDetailPay);
			orgStaffdetailPayList.set(i, oaVo);
		}
		PageInfo result = new PageInfo(orgStaffdetailPayList);

		model.addAttribute("contentModel", result);
		model.addAttribute("searchModel", searchVo);
		
		Map<String ,String> orderStats = orderDispatchPriceService.getTotalOrderMoneyMultiStat(searchVo);

		model.addAttribute("totalOrderMoney", orderStats.get("totalOrderMoney").toString());
		model.addAttribute("totalOrderPay", orderStats.get("totalOrderPay").toString());
		model.addAttribute("totalOrderCoupon", orderStats.get("totalOrderCoupon").toString());
		model.addAttribute("totalOrderIncoming", orderStats.get("totalOrderIncoming").toString());
		model.addAttribute("totalOrderPayType0", orderStats.get("totalOrderPayType0").toString());
		model.addAttribute("totalOrderPayType1", orderStats.get("totalOrderPayType1").toString());
		model.addAttribute("totalOrderPayType2", orderStats.get("totalOrderPayType2").toString());
		model.addAttribute("totalOrderPayType6", orderStats.get("totalOrderPayType6").toString());
		model.addAttribute("totalOrderPayType7", orderStats.get("totalOrderPayType7").toString());
		
		//还款金额
		BigDecimal totalPayDept = orgStaffPayDeptService.totalBySearchVo(searchVo);
		if (totalPayDept == null) totalPayDept = new BigDecimal(0);
		model.addAttribute("totalPayDept", MathBigDecimalUtil.round2(totalPayDept));
		
		return "staff/staffDetailPayList";
	}
}
