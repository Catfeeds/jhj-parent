package com.jhj.actioin.app.sec;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.jhj.action.app.JUnitActionBase;

public class TestSecController extends JUnitActionBase{
	
	/**
	 * 		秘书登陆接口 单元测试
	 *     ​http://localhost:8080/onecare/app/user/login.json
	 *     http://182.92.160.194/trac/wiki/%E7%94%A8%E6%88%B7%E7%99%BB%E9%99%86%E6%8E%A5%E5%8F%A3
	 */
	@Test
    public void testPostLogin() throws Exception {

		String url = "/app/am/login.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("mobile", "17710623062");
	    postRequest = postRequest.param("sms_token", "3351");
	    postRequest = postRequest.param("login_from", "1");
	
	  
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());

    }
	/**
	 * 秘书信息获取接口 
	 * @throws Exception
	 */
	@Test
    public void testSec() throws Exception {
		String url = "/app/am/get_secinfo.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
     	postRequest = postRequest.param("staff_id", "5");
	    postRequest = postRequest.param("mobile", "18610807136");
	    
	    
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());

	
    }
	/**
	 * 秘书信息修改
	 * @throws Exception
	 */
	
	@Test
    public void testGetSec() throws Exception {
		String url = "/app/am/post_secinfo.json";
     	MockHttpServletRequestBuilder postRequest = post(url);
     	postRequest = postRequest.param("staff_id", "6");
     	 
     	postRequest = postRequest.param("mobile", "17090052706");
     	postRequest = postRequest.param("name", "杨杨12345");
     	//postRequest = postRequest.param("nick_name", "哈雷2222");
     	postRequest = postRequest.param("sex", "1");
     	postRequest = postRequest.param("birth", "1980-07-01");
     	postRequest = postRequest.param("city_id", "5");
     	postRequest = postRequest.param("head_img", "");
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());

    }
}
