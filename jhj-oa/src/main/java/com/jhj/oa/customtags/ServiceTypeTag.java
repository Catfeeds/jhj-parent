package com.jhj.oa.customtags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.jhj.service.dict.DictUtil;

public class ServiceTypeTag extends SimpleTagSupport {

    private String serviceType;

    public ServiceTypeTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	String serviceTypeName = "";
        	if (serviceType != null) {
        		serviceTypeName = DictUtil.getServiceTypeName(Long.valueOf(serviceType) );
        	}
            getJspContext().getOut().write(serviceTypeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}





}
