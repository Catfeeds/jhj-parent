package com.jhj.oa.customtags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.jhj.po.model.survey.SurveyService;
import com.jhj.service.survey.SurveyServiceService;

/**
 *
 * @author :hulj
 * @Date : 2015年12月12日下午5:18:32
 * @Description: 
 *
 */
public class SurveyServiceNameTag extends SimpleTagSupport {
	
	 	private Long serviceId;
	 	
	  	
		public SurveyServiceNameTag() {
		}
	 	
		
		private SurveyServiceService service;
		
	 
	    @Override
	    public void doTag() throws JspException, IOException {
	        try {
	        	String serviceTypeName = "";
	        	if (serviceId != null) {
	        		
	        		SurveyService key = service.selectByPrimaryKey(serviceId);
	        		
	        		serviceTypeName  = key.getName();
	        		
	        	}
	            getJspContext().getOut().write(serviceTypeName);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }


		public Long getServiceId() {
			return serviceId;
		}


		public void setServiceId(Long serviceId) {
			this.serviceId = serviceId;
		}

	    
	
}
