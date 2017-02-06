package com.jhj.actioin.app.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.jhj.action.app.JUnitActionBase;


public class TestOrderPayController extends JUnitActionBase  {

	/**
	 * 		提交订单接口 单元测试
	 *     ​http://localhost:8080/onecare/app/order/post_add.json
	 *     http://182.92.160.194/trac/wiki/%E8%AE%A2%E5%8D%95%E6%8F%90%E4%BA%A4%E6%8E%A5%E5%8F%A3
	 */
	@Test
    public void testOrderOnlinePay() throws Exception {

		String url = "/app/order/online_pay_notify.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
     	

	    postRequest = postRequest.param("order_no", "827715205931728896");
	    postRequest = postRequest.param("pay_type", "2");
	    postRequest = postRequest.param("pay_order_type", "0");
	    postRequest = postRequest.param("notify_id", "f30a31bcad7560324b3249ba66ccf7aa");
	    postRequest = postRequest.param("notify_time", "20170204110750");
	    postRequest = postRequest.param("trade_no", "4004122001201702048653336536");
	    postRequest = postRequest.param("trade_status", "SUCCESS");
	    postRequest = postRequest.param("pay_account", "opyMAwe5UDPFV0CCMivsJoPH-_R0");
	    
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());

    }
}
