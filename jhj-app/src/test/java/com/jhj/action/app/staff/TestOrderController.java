package com.jhj.action.app.staff;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jhj.action.app.JUnitActionBase;

public class TestOrderController extends JUnitActionBase{
	/**
	 * 当日统计数接口
	 * @throws Exception
	 */
	
	@Test
    public void testTotalToday() throws Exception {

		String url = "/app/staff/order/total_today.json";
		String params = "?user_id=1";
		MockHttpServletRequestBuilder getRequest = get(url + params);
	  
		 ResultActions resultActions = this.mockMvc.perform(getRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());
    }
	
	@Test
    public void orderBegin() throws Exception {

		String url = "/app/staff/order/post_begin.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("staff_id", "1");
	    postRequest = postRequest.param("order_id", "1");
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());
    }
	
	
	@Test
    public void PostInviteTime() throws Exception {

		String url = "/app/staff/order/post_done.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("staff_id", "1");
	    postRequest = postRequest.param("order_id", "1");
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());
    }

	@Test
    public void testPostOrderDone() throws Exception {

		String url = "/app/staff/order/post_done.json?staff_id=1&order_id=1";

     	MockHttpServletRequestBuilder postRequest = post(url);
     	
     	HashMap<String, String> contentTypeParams = new HashMap<String, String>();
     	contentTypeParams.put("boundary", "265001916915724");
	   
	    MockMultipartFile image = new MockMultipartFile("imgs", "logo-yuan.png", "", imageToByteArray("/Users/lnczx/Pictures/logo-yuan.png"));
	    
	    MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

		ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.fileUpload(url)
                .file(image)        
                .contentType(mediaType))
                .andExpect(status().isOk());
	    
	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());


	    System.out.println("RestultActons: " + resultActions.andReturn().getResponse().getContentAsString());

    }
	
	@Test
    public void orderOverWork() throws Exception {

		String url = "/app/staff/order/post_overwork.json";

     	MockHttpServletRequestBuilder postRequest = post(url);
	    postRequest = postRequest.param("staff_id", "142");
	    postRequest = postRequest.param("order_id", "8011");
	    postRequest = postRequest.param("service_hour", "2");
	    postRequest = postRequest.param("order_pay", "40");
	    ResultActions resultActions = mockMvc.perform(postRequest);

	    resultActions.andExpect(content().contentType(this.mediaType));
	    resultActions.andExpect(status().isOk());

	    System.out.println("RestultActions: " + resultActions.andReturn().getResponse().getContentAsString());
    }
	
	public static byte[] imageToByteArray(String imgPath) {
		BufferedInputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(imgPath));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int size = 0;
			byte[] temp = new byte[1024];
			while ((size = in.read(temp)) != -1) {
				out.write(temp, 0, size);
			}
			in.close();
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
