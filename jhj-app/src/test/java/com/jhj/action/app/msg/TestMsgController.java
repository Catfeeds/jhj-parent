package com.jhj.action.app.msg;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.JUnitActionBase;

/**
 * @description：
 * @author： kerryg
 * @date:2015年7月27日 
 */
public class TestMsgController extends JUnitActionBase {

	@Test
    public void testMsgList() throws Exception {

		String url = "/app/msg/get_list.json";
		String params = "?user_id=60&user_type=0&page=1";
		MockHttpServletRequestBuilder getRequest = get(url + params);

	    ResultActions resultActions = this.mockMvc.perform(getRequest);
	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());
    }
/*	@Test
    public void testMsgPostRead() throws Exception {

		String url = "/app/msg/post_read.json";

     	MockHttpServletRequestBuilder postRequest = post(url);

	    postRequest = postRequest.param("user_id", "1");
     	postRequest = postRequest.param("user_type", "0");
     	postRequest = postRequest.param("msg_id", "2");
	    
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());

    }*/
}
