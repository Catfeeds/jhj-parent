package com.jhj.action.period;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.period.PeriodOrder;
import com.jhj.po.model.period.PeriodServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.cooperate.CooperateBusinessService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.period.PeriodOrderAddonsService;
import com.jhj.service.period.PeriodOrderService;
import com.jhj.service.period.PeriodServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.vo.dict.CooperativeBusinessSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.period.PeriodOrderDetailVo;
import com.jhj.vo.period.PeriodOrderSearchVo;
import com.jhj.vo.period.PeriodOrderVo;

@Controller
@RequestMapping("/period")
public class PeriodOrderController extends BaseController{
	
	@Autowired
	private PeriodOrderService periodOrderService;
	
	@Autowired
	private PeriodOrderAddonsService periodOrderAddonsService;
	
	@Autowired
	private UserAddrsService userAddresService;
	
	@Autowired
	private CooperateBusinessService cooperateBusinessService;
	
	@Autowired
	private PeriodServiceTypeService periodServiceTypeService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@AuthPassport
	@RequestMapping(value = "/periodOrderList", method = RequestMethod.GET)
	public String periodOrderList(Model model, HttpServletRequest request, PeriodOrderSearchVo searchVo){
		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		
		if (searchVo == null) searchVo = new PeriodOrderSearchVo();
		
		List<PeriodOrder> periodOrderListPage = periodOrderService.selectByListPage(searchVo, pageNo, Constants.PAGE_MAX_NUMBER);
		
		PageInfo<PeriodOrder> page = new PageInfo<PeriodOrder>(periodOrderListPage);
		
		List<PeriodOrder> list = page.getList();
		
		for (int i = 0 ; i < list.size(); i++) {
			PeriodOrder item = list.get(i);
			PeriodOrderVo vo = periodOrderService.getVos(item);
			list.set(i, vo);
		}
		page = new PageInfo<PeriodOrder>(list);
		model.addAttribute("periodOrderListPage", page);
		model.addAttribute("periodSearchModel", searchVo);
		
		return "period/periodOrderList";
		
	}
	
	@AuthPassport
	@RequestMapping(value="/periodOrderListDetail", method = RequestMethod.GET)
	public String updatePeriodOrder(@RequestParam("periodOrderId") Integer periodOrderId,Model model){
		
		PeriodOrder periodOrder = periodOrderService.selectByPrimaryKey(periodOrderId);
		PeriodOrderDetailVo detailVo = periodOrderService.getDetailVo(periodOrder);
		model.addAttribute("contentModel", detailVo);
		
		List<UserAddrs> userAddrsList = userAddresService.selectByUserId(periodOrder.getUserId().longValue());
		model.addAttribute("userAddrsList", userAddrsList);
		
		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setPeriodOrderId(periodOrderId);
		List<Orders> orderList = orderQueryService.selectBySearchVo(searchVo);
		
		model.addAttribute("orderList",orderList);
		
		return "period/periodOrderDetail";
	}
	
	@AuthPassport
	@RequestMapping(value="/addPeriodOrder", method = RequestMethod.GET)
	public String addPeriodOrder(Model model){
		
		CooperativeBusinessSearchVo vo = new CooperativeBusinessSearchVo();
		vo.setEnable((short) 1);
		List<CooperativeBusiness> CooperativeBusinessList = cooperateBusinessService
				.selectCooperativeBusinessVo(vo);
		if (CooperativeBusinessList != null) {
			model.addAttribute("cooperativeBusiness", CooperativeBusinessList);
		}
		PeriodServiceType periodServiceType = new PeriodServiceType();
		periodServiceType.setPackageType("1");
		List<PeriodServiceType> list = periodServiceTypeService.getList(periodServiceType);
		model.addAttribute("serviceTypeList", list);
		
		return "period/addPeriodOrder";
	}
	


}