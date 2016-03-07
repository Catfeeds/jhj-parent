package com.jhj.oa.customtags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.jhj.service.dict.DictUtil;

public class ServiceTypeNameTag extends SimpleTagSupport {

    private String serviceType;

    public ServiceTypeNameTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	StringBuffer serviceTypeName = new StringBuffer("");

        		String str[] = serviceType.split(",");
        		if(str[0].equals("0")){
        			getJspContext().getOut().write("钟点工");

        		}else {
        			for (int i = 0; i < str.length; i++) {
                		if (serviceType != null) { 
                    		serviceTypeName.append( DictUtil.getServiceTypeItemName(str[i]));
                    	}
        			}
        			 getJspContext().getOut().write(serviceTypeName.toString());
				}
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
