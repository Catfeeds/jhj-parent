package com.jhj.action.app.job.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.jhj.action.app.JUnitActionBase;

public class TestOrderQueryController extends JUnitActionBase{
	
	@Test
    public void testgetOrderMoney() throws Exception {

		String url = "/app/order/get_orderMoney.json";
		String params = "?staff_id=4&order_status=7";
		MockHttpServletRequestBuilder getRequest = get(url + params);

	  
	    ResultActions resultActions = mockMvc.perform(getRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());
    }

}
