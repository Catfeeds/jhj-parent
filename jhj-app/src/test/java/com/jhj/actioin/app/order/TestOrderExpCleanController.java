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
	 * 测试深度保洁订单提交接口
	 * @throws Exception
	 */
	@Test
    public void PostExpCleanOrder() throws Exception {

		String url = "/app/order/post_exp_clean_order.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("user_id", "1");
	    postRequest = postRequest.param("service_type", "2");
	    postRequest = postRequest.param("service_date", "1438589520");
	    postRequest = postRequest.param("order_from", "1");
	    postRequest = postRequest.param("addr_id", "1");
	    
	    HashMap<String,String> item1 = new HashMap<String,String>();
	    item1.put("serviceAddonId", "3"); 
	    item1.put("itemNum", "2");
	    HashMap<String,String> item2 = new HashMap<String,String>();
	    item2.put("serviceAddonId", "4");
	    item2.put("itemNum", "2");

	    ObjectMapper mapper = new ObjectMapper();
	    List<HashMap> sendDatas = new ArrayList<HashMap>();
	    sendDatas.add(item1);
	    sendDatas.add(item2);
	    postRequest = postRequest.param("service_addons_datas", mapper.writeValueAsString(sendDatas));
	    
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());

    }
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
