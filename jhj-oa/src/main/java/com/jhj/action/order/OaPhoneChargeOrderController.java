package com.jhj.action.order;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.po.model.order.Orders;
import com.jhj.service.order.OaPhoneChargeOrderService;
import com.jhj.vo.OaPhoneChargeOrderSearchVo;
import com.jhj.vo.order.OaPhoneChargeOrderVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.StringUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年10月24日下午2:45:48
 * @Description: 
 *		
 *		运营后台--订单管理，话费充值类订单列表
 */
@Controller
@RequestMapping(value = "/order")
public class OaPhoneChargeOrderController extends BaseController {

	@Autowired
	private OaPhoneChargeOrderService phoneService;
	
	@RequestMapping(value = "/phone_charge_order_list", method = RequestMethod.GET)
	public String getPhoneOrderList(Model model, HttpServletRequest request,OaPhoneChargeOrderSearchVo searchVo) throws UnsupportedEncodingException{
		
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		if(searchVo == null){
			searchVo = new OaPhoneChargeOrderSearchVo();
		}
		
		/*
		 * 搜索条件
		 */
		
		//充值手机号
		String chargeMobile = request.getParameter("chargeMobile");
		//用户手机号
		String userMobile =  request.getParameter("userMobile");
		if(!StringUtil.isEmpty(chargeMobile)){
			searchVo.setChargeMobile(new String(chargeMobile.getBytes("iso-8859-1"),"utf-8"));
		}
		if(!StringUtil.isEmpty(userMobile)){
			searchVo.setUserMobile(new String(userMobile.getBytes("iso-8859-1"),"utf-8"));
		}
		
		
		//统计图 和 列表页 共用参数 -- 充值金额
		
		String money = request.getParameter("money");
		if(!StringUtil.isEmpty(money)){
			searchVo.setMoney(new String(money.getBytes("iso-8859-1"),"utf-8"));
		}
		
		//统计图表参数--日期范围：开始和结束时间
		String startTimeChart = request.getParameter("startTime");
		if(!StringUtil.isEmpty(startTimeChart)){
			searchVo.setStartTimeStr(DateUtil.timeStamp2Date(startTimeChart, "yyyy-MM-dd"));
		}
		
		String endTimeChart = request.getParameter("endTime");
		if(!StringUtil.isEmpty(endTimeChart)){
			searchVo.setEndTimeStr(DateUtil.timeStamp2Date(endTimeChart, "yyyy-MM-dd"));
		}
		
		/*
		 * 特殊参数： status
		 * 		这里并不完全等价于  orders表的 order_status字段
		 * 
		 * 		根据业务，做特殊处理
		 * 
		 * 		status = 1  -->  order_status = 13 表示 微信支付失败
		 * 		
		 * 		status = 2  -->  order_status = 14 & remarks = wxSuccess 表示 微信支付成功，充值失败 
		 * 
		 * 		status = 3  --> order_status = 14 & remarks = apiSuccess 表示微信支付成功，充值成功
		 * 
		 * 		status = 4 --> order_status = 15 
		 * 
		 * 
		 */
		
		//用户选择的订单状态
		String searchStatus = request.getParameter("searchStatus");
		
		List<Short> staList = new ArrayList<Short>();
		List<String> remarkList = new ArrayList<String>();
		
		
		if(!StringUtil.isEmpty(searchStatus)){
			//充值成功
			if(searchStatus.equals("101")){
				staList.add((short)14);
				remarkList.add("10002");
			}
			
			//微信支付成功、话费充值失败
			if(searchStatus.equals("102")){
				staList.add((short)13);
				staList.add((short)15);
				staList.add((short)16);
				
				remarkList.add("10001");
				remarkList.add("10003");
			}
			
			//取消充值 
			if(searchStatus.equals("103")){
//				staList.add((short)13);
				staList.add((short)16);
				remarkList.add("10000");
			}
		}
		
		searchVo.setStaList(staList);
		searchVo.setRemarkList(remarkList);
		
		List<Orders>  orderList = phoneService.selectList(pageNo, pageSize, searchVo);
		
		
		Orders orders = null;
		for (int i = 0; i < orderList.size(); i++) {
			orders = orderList.get(i);
			OaPhoneChargeOrderVo vo = phoneService.tranVo(orders);
			orderList.set(i, vo);
		}
		
		PageInfo result = new PageInfo(orderList);	
		
		model.addAttribute("oaPhoneChargeOrderListVoModel", result);
		model.addAttribute("oaPhoneChargeOrderSearchVoModel", searchVo);
		
		return "order/phoneChargeOrderList";
		
		
	}
}
