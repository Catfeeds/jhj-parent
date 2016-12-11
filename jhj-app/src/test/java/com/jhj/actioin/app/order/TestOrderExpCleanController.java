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
	

}
