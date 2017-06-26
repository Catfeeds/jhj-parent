package com.jhj.actioin.app.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhj.action.app.JUnitActionBase;

/**
 * @description：
 * @author： kerryg
 * @date:2015年8月3日 
 */
public class TestOrderExpCleanController extends JUnitActionBase{
	

	/**
	 * 测试获取深度保洁订单详情接口
	 * @throws Exception
	 */
	@Test
    public void testExpCleanOrderDetail() throws Exception {
		String url = "/app/order/get_exp_clean_order_detail.json";
		
		String params = "?order_no=631364703049744384&user_id=2";
		MockHttpServletRequestBuilder getRequest = get(url+params );
		
	    ResultActions resultActions = this.mockMvc.perform(getRequest);
	    
	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());
	}
	

	@Test
	public void testPostExp() throws Exception{
		String url = "/app/order/post_exp.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
     	
     	
     	postRequest = postRequest.param("service_addons_datas", "[{\"serviceAddonId\":\"27\",\"itemNum\":\"60\"}]");
     	postRequest = postRequest.param("service_type", "53");
     	postRequest = postRequest.param("staff_id", "0");
     	postRequest = postRequest.param("user_id", "12385"); 
     	postRequest = postRequest.param("order_from", "1");
     	postRequest = postRequest.param("addr_id", "12799");
     	postRequest = postRequest.param("service_date", "1498264200");
     	postRequest = postRequest.param("serviceHour", "10"); 
     	postRequest = postRequest.param("mobile", "15210605385");   
     	
 	    
 	    ResultActions resultActions = mockMvc.perform(postRequest);

 	    resultActions.andExpect(content().contentType(this.mediaType));
 	    resultActions.andExpect(status().isOk());


 	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());
     	
	}
	

}
