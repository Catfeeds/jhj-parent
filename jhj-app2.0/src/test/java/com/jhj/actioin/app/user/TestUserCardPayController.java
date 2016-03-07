package com.jhj.actioin.app.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.eclipse.jetty.util.UrlEncoded;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jhj.action.app.JUnitActionBase;

public class TestUserCardPayController extends JUnitActionBase  {

	/**
	 * 		会员充值在线支付成功同步接口
	 *     ​http://localhost:8080/onecare/app/user/card_online_pay.json
	 *     http://182.92.160.194/trac/wiki/%E4%BC%9A%E5%91%98%E5%85%85%E5%80%BC%E6%8E%A5%E5%8F%A3%E6%8E%A5%E5%8F%A3
	 */
	@Test
    public void testCardOnlinePay() throws Exception {

		String url = "/app/user/card_online_pay.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("user_id", "1");
	    postRequest = postRequest.param("card_order_no", "634215554466451456");
	    postRequest = postRequest.param("pay_type", "2");
	    postRequest = postRequest.param("notify_id", "1212121234123412341234");
	    postRequest = postRequest.param("notify_time", "2015-08-19 18:50:23");
	    postRequest = postRequest.param("trade_no", "12121211111234567890");
	    postRequest = postRequest.param("trade_status", "SUCCESS");

	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());

    }
}
