package com.jhj.action.order;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
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
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OaOrderDisService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.vo.order.OaOrderDisVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

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
	private OrderDispatchsService orderDispatchsService;
	
	@Autowired
	private OrgStaffsService staffService;
		
	/**
	 *   
	 *  订单日历--控件展示
	 *  
	 */
	
//	@AuthPassport
	@RequestMapping(value = "/order-scheduling", method = RequestMethod.GET )
	public String orderCalender(HttpServletRequest request, Model model,
		@RequestParam("org_staff_id") Long orgStaffId) {
		
		model.addAttribute("orgStaffId", orgStaffId);

		return "order/orderCalendarList";
	}
	
}
