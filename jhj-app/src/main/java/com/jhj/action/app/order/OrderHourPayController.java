package com.jhj.action.app.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderHourPayService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.meijia.utils.vo.AppResultData;
import com.jhj.vo.order.OrderHourPayVo;

/**
 *
 * @author :hulj
 * @Date : 2015年7月28日下午4:35:40
 * @Description: 保洁类-钟点工订单支付接口
 *
 */
@Controller
@RequestMapping(value = "/app/order")
public class OrderHourPayController extends BaseController {
	@Autowired
	private OrderPricesService orderPriceService;
	
	@Autowired
	private OrderDispatchsService orderDispatchService;
	
	@Autowired
	private UserAddrsService userAddrService;
	
	@Autowired
	private OrderHourPayService orderHourPayService;
	
	@Autowired
	private OrdersService ordersService;	
	
	/*
	 * 跳转到支付页面的 预加载
	 */
	@RequestMapping(value = "to_order_hour_pay.json", method = RequestMethod.GET)
	public AppResultData<Object> orderHourPay(
			@RequestParam("userId") Long userId,
			@RequestParam("orderId") Long orderId,
			@RequestParam("addrId") Long addrId){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		/*
		 * 订单支付，主要涉及 
		 * 		是否使用优惠券
		 * 	         支付方式（微信支付，账户余额支付。。）
		 */
		
		OrderHourPayVo orderHourPayVo = orderHourPayService.initOrderHourPayVo();
		
		Orders order = ordersService.selectByPrimaryKey(orderId);
		//总价
		OrderPrices orderPrices = orderPriceService.selectByOrderNo(order.getOrderNo());
		orderHourPayVo.setOrderMoney(orderPrices.getOrderMoney());
		
		
//		OrderDispatchs orderDispatchs = orderDispatchService.selectByOrderNo(orderNo);
		//服务开始时间
		orderHourPayVo.setServiceDate(order.getServiceDate());
		
		//服务地址
		UserAddrs userAddrs = userAddrService.selectByUserIdAndAddrId(addrId, userId);
		orderHourPayVo.setServiceAddr(userAddrs.getName()+" "+userAddrs.getAddr());
		
		//备注
		orderHourPayVo.setRemark(order.getServiceContent());
		
		//优惠券(可用的)
		
		List<DictCoupons> dictList = orderHourPayService.getCouponsByUserId(userId);
		
		orderHourPayVo.setDictList(dictList);
		//支付方式 TODO 
		
		result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, orderHourPayVo);
		
		return result;
	}
}
