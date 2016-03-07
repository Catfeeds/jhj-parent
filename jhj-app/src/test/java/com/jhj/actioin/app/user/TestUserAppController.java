package com.jhj.actioin.app.user;

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
public class TestUserAppController extends JUnitActionBase {

/*	@Test
    public void testGetInfo() throws Exception {

		String url = "/app/user/get_sms_token.json";
		String params = "?mobile=17710623062&&sms_type=0";
		MockHttpServletRequestBuilder getRequest = get(url + params);

	    ResultActions resultActions = this.mockMvc.perform(getRequest);
	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());
    }*/
	/**
	 * 用户兑换优惠券接口
	 * @throws Exception
	 */
	@Test
    public void testPostPushBind() throws Exception {

		String url = "/app/user/post_push_bind.json";

     	MockHttpServletRequestBuilder postRequest = post(url);

		
	    postRequest = postRequest.param("user_id", "17");
     	postRequest = postRequest.param("user_type", "0");
     	postRequest = postRequest.param("client_id", "684ff852fb14bb25b2f13c32355404bb");
	    postRequest = postRequest.param("device_type", "android");
	    

	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());

    }
	/*@Test
    public void testPostPushBind() throws Exception {

		String url = "/app/user/login.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
		
	    postRequest = postRequest.param("mobile", "15712917308");
     	postRequest = postRequest.param("sms_token", "3377");
	    postRequest = postRequest.param("login_from", "1");

	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());

    }*/
}
