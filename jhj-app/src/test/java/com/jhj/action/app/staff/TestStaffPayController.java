package com.jhj.action.app.staff;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.JUnitActionBase;

public class TestStaffPayController extends JUnitActionBase{
	
	/**
	 * 交易明细接口
	 * @throws Exception
	 */
	/*@Test
    public void testGetPayDetail() throws Exception {

		String url = "/app/staff/pay/get_detail.json";
		String params = "?staff_id=4&&year=2016&&month=1";
		MockHttpServletRequestBuilder getRequest = get(url + params);

	    ResultActions resultActions = this.mockMvc.perform(getRequest);
	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());
    }*/
	
	@Test
    public void testGetPayDept() throws Exception {

		String url = "/app/staff/pay/pay_dept.json";
		String params = "?staff_id=2&pay_type=1";
		MockHttpServletRequestBuilder getRequest = get(url + params);

	    ResultActions resultActions = this.mockMvc.perform(getRequest);
	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());
    }
	@Test
    public void testGetPayDeptSuccess() throws Exception {
		
		String url = "/app/staff/pay/pay_dept_success.json";
		MockHttpServletRequestBuilder postRequest = post(url);
	
     	//通用订单  需要支付
	    postRequest = postRequest.param("trade_no", "2016101821001004930273746221");
	    postRequest = postRequest.param("notify_time", "2016-10-18 16:53:33");
	    postRequest = postRequest.param("pay_type", "1");
	    postRequest = postRequest.param("notify_id", "3a22665b35b6ba413dff866eaf630aen6e");
	    postRequest = postRequest.param("order_no", "788301132345638912");
	    postRequest = postRequest.param("trade_status", "TRADE_SUCCESS");
	    postRequest = postRequest.param("pay_account", "lnczx@tom.com");
//	    postRequest = postRequest.param("order_pay", "10");
	    
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());

    }

	
}
