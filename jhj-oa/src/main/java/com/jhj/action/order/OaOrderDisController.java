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
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.service.order.OaOrderDisService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.vo.OaOrderDisSearchVo;
import com.jhj.vo.order.OaOrderDisVo;
import com.meijia.utils.StringUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月14日下午8:34:11
 * @Description: 
 *				
 *			运营平台--订单管理--派工查询
 *
 */
@Controller
@RequestMapping(value = "/order")
public class OaOrderDisController extends BaseController {
	@Autowired
	private OaOrderDisService oaDisService;
	@Autowired
	private OrderDispatchsService orderDisService;
	
	/*
	 * 下单表 所有记录
	 */
	@AuthPassport
	@RequestMapping(value = "/cal-list" , method =RequestMethod.GET)
	public String getOrderDisList(Model model, HttpServletRequest request,OaOrderDisSearchVo oaOrderDisSearchVo){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		if(oaOrderDisSearchVo == null){
			oaOrderDisSearchVo = new OaOrderDisSearchVo();
		}
		
		//得到 当前登录 的 门店id，并作为搜索条件
		String org = AuthHelper.getSessionLoginOrg(request);
		
		if(!org.equals("0") && !StringUtil.isEmpty(org)){
			//未选择 门店， 且 当前 登录 用户 为 店长 （  session中的  orgId 不为 0）,设置搜索条件为  店长的门店
			oaOrderDisSearchVo.setSearchOrgId(Long.valueOf(org));
		}
		
		List<OrderDispatchs> disList = oaDisService.selectOrderDisByListPage(oaOrderDisSearchVo, pageNo, pageSize);
		
		OrderDispatchs orderDis = null;
		for (int i = 0; i < disList.size(); i++) {
			orderDis = disList.get(i);
			
			OaOrderDisVo oaOrderDisVo = oaDisService.compleVo(orderDis);
			
			disList.set(i, oaOrderDisVo);
		}
		
		PageInfo result = new PageInfo(disList);	
		
		model.addAttribute("oaOrderDisVoModel", result);
		model.addAttribute("oaOrderDisSearchVoModel", oaOrderDisSearchVo);
		
		return "order/orderDisList";
	}
	
//	@AuthPassport
	@RequestMapping(value = "/order-scheduling", method = RequestMethod.GET )
	public String orderCalender(HttpServletRequest request, Model model,
		@RequestParam("org_staff_id") Long orgStaffId) {
		
		model.addAttribute("orgStaffId", orgStaffId);

		return "order/orderCalendarList";
	}
}
