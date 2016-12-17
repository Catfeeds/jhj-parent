package com.jhj.actioin.app.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.jhj.action.app.JUnitActionBase;


public class TestOrderWxPayController extends JUnitActionBase  {

	@Test
    public void testWxPay() throws Exception {

		String url = "/app/wxpay";
		String params = "?payOrderType=0&orderType=0&serviceTypeId=28&state=STATE&code=031iXwWd2hLZZB081xUd2FNAWd2iXwWC&userCouponId=&orderId=5472";
		MockHttpServletRequestBuilder getRequest = get(url + params);

	    ResultActions resultActions = this.mockMvc.perform(getRequest);
	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());



	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());

    }
	
	@Test
    public void testZhifubaoPay() throws Exception {

		String url = "/app/order/online_pay_cardOrder_notify.json";
		String params = "?trade_no=2016121721001004990231104133&notify_time=2016-12-1717:47:47&pay_type=1&notify_id=604b305db8f7436450b291d12b7dfe4nn2&order_no=810058855336644608&trade_status=TRADE_SUCCESS&pay_order_type=1&pay_account=15201023689";
		MockHttpServletRequestBuilder getRequest = post(url + params);

	    ResultActions resultActions = this.mockMvc.perform(getRequest);
	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());


    }
	
}
