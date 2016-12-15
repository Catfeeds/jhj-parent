package com.jhj.action.app.staff;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.jhj.action.app.JUnitActionBase;

public class TestOrderController extends JUnitActionBase{
	/**
	 * 当日统计数接口
	 * @throws Exception
	 */
	
	@Test
    public void testTotalToday() throws Exception {

		String url = "/app/staff/order/total_today.json";
		String params = "?user_id=4";
		MockHttpServletRequestBuilder getRequest = get(url + params);
	  
		 ResultActions resultActions = this.mockMvc.perform(getRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());
    }
	
	@Test
    public void orderBegin() throws Exception {

		String url = "/app/staff/order/post_begin.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("staff_id", "2");
	    postRequest = postRequest.param("order_id", "41");
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());
    }
	
	
	@Test
    public void PostInviteTime() throws Exception {

		String url = "/app/staff/order/post_done.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("staff_id", "5");
	    postRequest = postRequest.param("order_id", "5");
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());
    }
	
	@Test
    public void orderOverWork() throws Exception {

		String url = "/app/staff/order/post_overwork.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("staff_id", "5");
	    postRequest = postRequest.param("order_id", "5");
	    postRequest = postRequest.param("service_hour", "2");
	    postRequest = postRequest.param("order_pay", "100");
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());
    }
}
