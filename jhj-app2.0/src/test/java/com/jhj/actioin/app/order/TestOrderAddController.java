package com.jhj.actioin.app.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.jhj.action.app.JUnitActionBase;


public class TestOrderAddController extends JUnitActionBase  {

	/**
	 * 		提交订单接口 单元测试
	 *     ​http://localhost:8080/onecare/app/order/post_add.json
	 *     http://182.92.160.194/trac/wiki/%E8%AE%A2%E5%8D%95%E6%8F%90%E4%BA%A4%E6%8E%A5%E5%8F%A3
	 */
	@Test
    public void testSaveOrder() throws Exception {

		String url = "/app/order/post_add.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
     	
     	//通用订单. 无需支付
//	    postRequest = postRequest.param("user_id", "92");
//	    postRequest = postRequest.param("sec_id", "2");
//	    postRequest = postRequest.param("mobile", "13520256623");
//	    postRequest = postRequest.param("service_type", "1");
//	    postRequest = postRequest.param("order_pay_type", "0");
//	    postRequest = postRequest.param("service_content", "请我叫个快递，已经打电话给顺丰上门");
     		
     	//通用订单  需要支付
	    postRequest = postRequest.param("user_id", "92");
	    postRequest = postRequest.param("sec_id", "2");
	    postRequest = postRequest.param("mobile", "13520256623");
	    postRequest = postRequest.param("service_type", "6");
	    postRequest = postRequest.param("order_pay_type", "1");
	    postRequest = postRequest.param("service_content", "北京-上海 2015-07-22 18:00:00, 航班号CA5566");
	    postRequest = postRequest.param("order_pay", "10");
	    
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());

    }
}
