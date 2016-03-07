package com.jhj.actioin.app.dict;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.jhj.action.app.JUnitActionBase;


public class TestCardTypeController extends JUnitActionBase  {

	@Test
    public void testGetCards() throws Exception {

		MockHttpServletRequestBuilder getRequest = get("/app/dict/get_cards.json");

	    ResultActions resultActions = this.mockMvc.perform(getRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());
	    //打印所有的信息
//	    resultActions.andDo(MockMvcResultHandlers.print());

	    System.out.print("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());

    }
	
	@Test
    public void testDictList() throws Exception {
		String url = "/app/dict/get_ads.json";
		
		String params = "?ad_type=0";
		MockHttpServletRequestBuilder getRequest = get(url + params);
		
	    ResultActions resultActions = this.mockMvc.perform(getRequest);
	    
	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());
	}
	@Test
	public void testServiceTypeList() throws Exception {
		
		MockHttpServletRequestBuilder getRequest = get("/app/dict/get_serviceType.json");
		
		ResultActions resultActions = this.mockMvc.perform(getRequest);
		
		resultActions.andExpect(content().contentType(this.mediaType));
		resultActions.andExpect(status().isOk());
		
		System.out.print("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());
	}
	/**
	 * 测试获去附加服务列表接口
	 * @throws Exception
	 */
	@Test
	public void testserviceAddonsTypeList() throws Exception {
		
		MockHttpServletRequestBuilder getRequest = get("/app/dict/get_service_addons_type.json");
		
		ResultActions resultActions = this.mockMvc.perform(getRequest);
		
		resultActions.andExpect(content().contentType(this.mediaType));
		resultActions.andExpect(status().isOk());
		
		System.out.print("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());
	}
}
