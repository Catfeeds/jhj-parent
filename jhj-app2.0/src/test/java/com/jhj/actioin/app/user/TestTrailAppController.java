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
public class TestTrailAppController extends JUnitActionBase {

	
	/**
	 * 用户兑换优惠券接口
	 * @throws Exception
	 */
	@Test
    public void testPostTrail() throws Exception {

		String url = "/app/user/post_user_trail.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
		
	    postRequest = postRequest.param("user_id", "17");
     	postRequest = postRequest.param("user_type", "1");
     	postRequest = postRequest.param("lat", "39.945754");
	    postRequest = postRequest.param("lng", "116.443635");
	    

	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());

    }

}
