package com.jhj.actioin.app.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import com.jhj.action.app.JUnitActionBase;

public class TestOrderAmController extends JUnitActionBase{

	@Test
    public void saveUserOrder() throws Exception {

		String url = "/app/order/post_user.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("user_id", "1");
	    postRequest = postRequest.param("service_type", "1");
	    postRequest = postRequest.param("service_content", "上门清洁");
	    postRequest = postRequest.param("order_from", "1");
	    postRequest = postRequest.param("sms_type", "1");
	    
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());

    }
	@Test 
	public void saveUserAm() throws Exception {
		
		String url = "/app/order/post_user_am.json";
		
		MockHttpServletRequestBuilder postRequest = post(url);
		postRequest = postRequest.param("order_id", "1");
		postRequest = postRequest.param("service_content", "客厅清洁，阳台清洁");
		postRequest = postRequest.param("order_money", "500");
		
		ResultActions resultActions = mockMvc.perform(postRequest);
		
		resultActions.andExpect(content().contentType(this.mediaType));
		resultActions.andExpect(status().isOk());
		System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());
	}

}
