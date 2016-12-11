package com.jhj.action.order;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.order.OrderRates;
import com.jhj.service.order.OrderRatesService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderRatesVo;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

@Controller
@RequestMapping("/order")
public class OrderRatesController extends BaseController{

	@Autowired
	private OrderRatesService orderRatesService;
	
	@AuthPassport
	@RequestMapping(value="/order-rate-list",method=RequestMethod.GET)
	public String orderRateList(Model model,HttpServletRequest request,OrderDispatchSearchVo searchVo) throws UnsupportedEncodingException{
		
		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		
		// 服务开始时间
		String serviceStartTime = request.getParameter("serviceStartTimeStr");
		if (!StringUtil.isEmpty(serviceStartTime)) {
			searchVo.setStartServiceTime(TimeStampUtil.getMillisOfDayFull(serviceStartTime+":00") / 1000);
		}
		// 服务结束时间
		String serviceEndTimeStr = request.getParameter("serviceEndTimeStr");
		if (!StringUtil.isEmpty(serviceEndTimeStr)) {
			searchVo.setEndServiceTime(TimeStampUtil.getMillisOfDayFull(serviceEndTimeStr+":00") / 1000);
		}
		
		String staffName = request.getParameter("staffName");
		if(staffName!=null && !staffName.equals("")){
			String name = new String(staffName.getBytes("ISO-8859-1"),"UTF-8");
			searchVo.setStaffName(name);
		}
		
		PageInfo<OrderRatesVo> page = orderRatesService.selectByListPage(searchVo, pageNo, ConstantOa.DEFAULT_PAGE_SIZE,false);
		
		List<OrderRatesVo> list = page.getList();
		
		for(int i=0,len=list.size();i<len;i++){
			OrderRates orderRates = (OrderRates)list.get(i);
			OrderRatesVo transVoOa = orderRatesService.transVoOa(orderRates);
			list.set(i, transVoOa);
		}
		String serviceStartStr = request.getParameter("serviceStartTimeStr");
		if (!StringUtil.isEmpty(serviceStartTime))
			model.addAttribute("serviceStartTimeStr", serviceStartStr);

		String serviceEndStr = request.getParameter("serviceEndTimeStr");
		if (!StringUtil.isEmpty(serviceEndTimeStr))
			model.addAttribute("serviceEndTimeStr", serviceEndStr);
		page=new PageInfo<OrderRatesVo>(list);
		
		model.addAttribute("orderDispatchSearchVo", searchVo);
		model.addAttribute("page", page);
		
		return "orderRates/orderRatesList";
	}
	
	
	
	
}
