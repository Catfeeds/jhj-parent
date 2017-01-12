package com.jhj.action.app.cleanup;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.jhj.action.app.JUnitActionBase;

public class TestCleanupUserDetailPayController extends JUnitActionBase{

	@Test
    public void testCleanupUserDetailPay() throws Exception {

		String url = "/app/job/cleanup/detail_pay.json";
		MockHttpServletRequestBuilder getRequest = get(url);

	    ResultActions resultActions = mockMvc.perform(getRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());
    }
}
