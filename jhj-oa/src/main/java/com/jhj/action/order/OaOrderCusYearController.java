package com.jhj.action.order;

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
import com.jhj.po.model.order.OrderCustomizationYear;
import com.jhj.service.orderYear.OrderCustomizYearService;
import com.jhj.vo.order.year.OrderCusYearVo;

/**
 *
 * @author :hulj
 * @Date : 2016年4月2日下午12:14:41
 * @Description: 
 *
 */
@Controller
@RequestMapping(value = "/order")
public class OaOrderCusYearController extends BaseController {
	
	@Autowired
	private OrderCustomizYearService cusService;
		
	/*
	 * 全年订制 -- 订单列表
	 */
	@RequestMapping(value = "order_cus_year_list",method=RequestMethod.GET)
	public String getCusOrderList(Model model,HttpServletRequest request){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		List<OrderCustomizationYear> list = cusService.selectByListPage();
		
		
		OrderCustomizationYear cusOrder = null;
		
		for (int i = 0; i < list.size(); i++) {
			cusOrder = list.get(i);
			OrderCusYearVo vo = cusService.transToVo(cusOrder);
			list.set(i,vo);
		}
		
		PageInfo result = new PageInfo(list);	
		
		model.addAttribute("cusYearModel", result);
		
		return "order/cusYearOrderList";
	}
	
	@RequestMapping(value = "order_cus_year_detail",method = RequestMethod.GET)
	public String getCusOrderDetail(Model model,
			@RequestParam("id")Long id){
		
		OrderCustomizationYear customizationYear = cusService.selectByPrimaryKey(id);
		
		OrderCusYearVo transToVo = cusService.transToVo(customizationYear);
		
		model.addAttribute("cusYearDetailVoModel", transToVo);
		
		return "order/cusYearOrderForm";
	}
}
