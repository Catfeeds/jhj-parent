package com.jhj.actioin.app.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.jhj.action.app.JUnitActionBase;

/**
 *
 * @author :hulj
 * @Date : 2015年7月24日上午11:58:17
 * @Description: TODO
 *	
 */
public class TestOrderHourAddController extends JUnitActionBase {
	/**
	 * 		提交订单接口 单元测试
	 *     http://localhost:8080/jhj-app/app/order/post_hour.json
	 *     
	 *     http://182.92.160.194/trac/wiki/%E8%AE%A2%E5%8D%95%E6%8F%90%E4%BA%A4%E6%8E%A5%E5%8F%A3
	 *     
	 *     http://182.92.160.194/jhj/app/order/post_hour.json
	 */
	@Test
	public void testSumbmitOrder() throws Exception{
		String url = "/app/order/post_hour.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
     	
     	postRequest = postRequest.param("user_id", "2");                
     	postRequest = postRequest.param("service_type", "1");
 	    postRequest = postRequest.param("service_content", "让你干啥你干啥");
 	    postRequest = postRequest.param("service_date", "7");
 	    postRequest = postRequest.param("addr_id", "2");
 	    postRequest = postRequest.param("service_hour", "4");
 	    postRequest = postRequest.param("service_addons", "1,2");
 	    
 	    ResultActions resultActions = mockMvc.perform(postRequest);

 	    resultActions.andExpect(content().contentType(this.mediaType));
 	    resultActions.andExpect(status().isOk());


 	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());
     	
	}
	@Test
	public void tesPostPay() throws Exception{
		String url = "/app/order/post_pay.json";
		
		MockHttpServletRequestBuilder postRequest = post(url);
		
		postRequest = postRequest.param("user_id", "5");                
		postRequest = postRequest.param("order_no", "692632110820753408");
		postRequest = postRequest.param("order_pay_type", "0");
		postRequest = postRequest.param("user_coupon_id", "0");
		
		ResultActions resultActions = mockMvc.perform(postRequest);
		
		resultActions.andExpect(content().contentType(this.mediaType));
		resultActions.andExpect(status().isOk());
		
		
		System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());
		
	}
	
	
}
