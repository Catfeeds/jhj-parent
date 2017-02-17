package com.jhj.actioin.app.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import com.jhj.action.app.JUnitActionBase;

public class TestOrderDispatchController extends JUnitActionBase{

	@Test
    public void checkDispatch() throws Exception {

		String url = "/app/order/check_dispatch.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("service_type_id", "28");
	    postRequest = postRequest.param("service_date_str", "2017-02-15");
	    postRequest = postRequest.param("addr_id", "3276");

	    
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());

    }


}
