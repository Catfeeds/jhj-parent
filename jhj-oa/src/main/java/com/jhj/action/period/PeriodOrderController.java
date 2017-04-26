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
import com.jhj.po.model.period.PeriodOrder;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.period.PeriodOrderAddonsService;
import com.jhj.service.period.PeriodOrderService;
import com.jhj.service.users.UserAddrsService;

@Controller
@RequestMapping("/period")
public class PeriodOrderController extends BaseController{
	
	@Autowired
	private PeriodOrderService periodOrderService;
	
	@Autowired
	private PeriodOrderAddonsService periodOrderAddonsService;
	
	@Autowired
	private UserAddrsService userAddresService;
	
	@RequestMapping(value = "/periodOrderList", method = RequestMethod.GET)
	public String periodOrderList(PeriodOrder periodOrder, Model model, HttpServletRequest request){
		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		
		List<PeriodOrder> periodOrderListPage = periodOrderService.periodOrderListPage(periodOrder, pageNo, Constants.PAGE_MAX_NUMBER);
		
		PageInfo<PeriodOrder> page = new PageInfo<PeriodOrder>(periodOrderListPage);
		
		model.addAttribute("periodOrderListPage", page);
		
		return "period/periodOrderList";
		
	}
	
	@RequestMapping(value="/updatePeriodOrder", method = RequestMethod.GET)
	public String updatePeriodOrder(@RequestParam("periodOrderId") Integer periodOrderId,Model model){
		
		PeriodOrder periodOrder = periodOrderService.selectByPrimaryKey(periodOrderId);
		model.addAttribute("periodOrder", periodOrder);
		
		List<UserAddrs> userAddrsList = userAddresService.selectByUserId(periodOrder.getUserId().longValue());
		model.addAttribute("userAddrsList", userAddrsList);
		
		
		return "period/periodOrder";
	}
	
	@RequestMapping(value="/updatePeriodOrder", method = RequestMethod.POST)
	public String updatePeriodOrder(@RequestParam("periodOrderId") Integer periodOrderId){
		
		PeriodOrder periodOrder = periodOrderService.selectByPrimaryKey(periodOrderId);
		
		
		
		return "redirect:periodOrderList";
	}
	
	
	
	


}