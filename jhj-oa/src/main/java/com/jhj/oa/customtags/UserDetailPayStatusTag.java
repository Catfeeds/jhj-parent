package com.jhj.oa.customtags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.Orders;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrdersService;



/**
 * 用户消费明细表中添加订单状态
 * 
 * */
public class UserDetailPayStatusTag extends SimpleTagSupport{
	
	//'0 =  订单支付，1 = 充值卡支付，2 = 手机充值卡支付，3 =  订单补差价支付，4 =  订单加时  5=取消订单，退款'
	private short orderType;
	
	private Long orderId;
	
	private Long orderNo;
	
	public UserDetailPayStatusTag() {
		super();
	}

	public short getOrderType() {
		return orderType;
	}

	public void setOrderType(short orderType) {
		this.orderType = orderType;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	@Override
	public void doTag() throws JspException, IOException {
		
		String status = "";
		
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());
		
		if(orderType==0 || orderType==5 || orderType==3 || orderType==4){
			OrdersService orderService = wac.getBean(OrdersService.class);
			Orders order = orderService.selectByPrimaryKey(orderId);
			if(order!=null){
				Short orderStatus = order.getOrderStatus();
				switch (orderStatus) {
				case 0:
					status="已取消";break;
				case 1:
					status="未支付";break;
				case 2:
					status="已支付";break;
				case 3:
					status="已派工";break;
				case 5:
					status="开始服务";break;
				case 7:
					status="完成服务";break;
				case 8:
					status="已评价";break;
				case 9:
					status="已关闭";break;
				default:
					break;
				}
			}
			
		}
		if(orderType==1){
			OrderCardsService orderCardService = wac.getBean(OrderCardsService.class);
			OrderCards orderCard = orderCardService.selectByPrimaryKey(orderId);
			Short orderStatus = orderCard.getOrderStatus();
			if(orderStatus==0){
				status="未支付";
			}
			if(orderStatus==1){
				status="已支付";
			}
		}
		if(orderType==9){
			OrderPriceExtService orderPriceExtService = wac.getBean(OrderPriceExtService.class);
			OrderPriceExt orderPriceExt = orderPriceExtService.selectByOrderNoExt(String.valueOf(orderNo));
			int orderStatus = orderPriceExt.getOrderStatus();
			switch (orderStatus) {
			case 0:
				status="已取消";break;
			case 1:
				status="支付中";break;
			case 2:
				status="完成支付";break;
			case 9:
				status="已关闭";break;
			default:
				break;
			}
		}
		
		getJspContext().getOut().write(status);
	}

}
