package com.jhj.actioin.app.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.jhj.action.app.JUnitActionBase;

public class TestUserController extends JUnitActionBase {
	
	/**
	 * 验证获取验证码接口
	 * ​http://123.57.232.172/jhj/app/user/get_sms_token.json
	 * @throws Exception
	 */
	@Test
    public void testGetSmsToken() throws Exception {

		String url = "/app/user/get_sms_token.json";
		String params = "?mobile=13146012753&sms_type=0";
		MockHttpServletRequestBuilder getRequest = get(url + params);

	    ResultActions resultActions = this.mockMvc.perform(getRequest);
	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());
    }
	
	/**
	 * 	 	用户登陆接口 单元测试
	 *     ​http://localhost:8080/jhj-oa/app/user/login.json
	 */
	@Test
    public void testPostLogin() throws Exception {

		String url = "/app/user/login.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("mobile", "13146012753");
	    postRequest = postRequest.param("sms_token", "000000");
	    postRequest = postRequest.param("login_from", "1");
	    

	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());

    }

}
