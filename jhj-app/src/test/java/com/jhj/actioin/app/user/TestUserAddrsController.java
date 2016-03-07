package com.jhj.actioin.app.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.eclipse.jetty.util.UrlEncoded;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.jhj.action.app.JUnitActionBase;

public class TestUserAddrsController extends JUnitActionBase {
	
	/**
	 * 获取用户地址列表接口
	 * ​http://123.57.232.172/jhj-app/app/user/get_user_addrs.json
	 * @throws Exception
	 */
	@Test
    public void testGetAddress() throws Exception {

		String url = "/app/user/get_user_addrs.json";
		String params = "?user_id=2";
		MockHttpServletRequestBuilder getRequest = get(url + params);

	    ResultActions resultActions = this.mockMvc.perform(getRequest);
	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());

    }
	
	/**
	 * 	 	用户登陆接口 单元测试
	 *     ​http://localhost:8080/jhj-app/app/user/post_del_user_addrs.json
	 */
	@Test
    public void testPostDeleteUserAddrs() throws Exception {

		String url = "/app/user/post_del_user_addrs.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("user_id", "1");
	    postRequest = postRequest.param("addr_id", "1");
	    

	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());

    }
	
	/**
	 * 	          用户地址提交接口
	 *     ​http://localhost:8080/jhj-app/app/user/post_user_addrs.json
	 */
	@Test
    public void testSaveAddress() throws Exception {
		String url = "/app/user/post_user_addrs.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("user_id", "92");
	    postRequest = postRequest.param("addr_id", "0");

	    postRequest = postRequest.param("poi_type", "0");
	    postRequest = postRequest.param("name", "宇飞大厦");
	    postRequest = postRequest.param("address", "东城区东直门外大街42号。。。");
	    postRequest = postRequest.param("latitude","39.946130678559037" );
	    postRequest = postRequest.param("longitude", "116.44400998619697");
	    postRequest = postRequest.param("city", "城市");
	    postRequest = postRequest.param("uid", "93df0dc…");
	    postRequest = postRequest.param("phone", "(010)84608109");
	    postRequest = postRequest.param("post_code", "asd");
	    postRequest = postRequest.param("addr", UrlEncoded.encodeString("17号楼"));
	    postRequest = postRequest.param("is_default", "1");

	    postRequest = postRequest.param("city_id", "0");
	    postRequest = postRequest.param("cell_id", "0");
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());

    }

}
