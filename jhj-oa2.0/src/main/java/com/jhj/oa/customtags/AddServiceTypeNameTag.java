package com.jhj.oa.customtags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.jhj.service.dict.DictUtil;

public class AddServiceTypeNameTag extends SimpleTagSupport {

    private String addServiceType;

    public AddServiceTypeNameTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	StringBuffer serviceTypeName = new StringBuffer("");

        		String str[] = addServiceType.split(",");
        		if(str[0].equals("0")){
        			getJspContext().getOut().write("钟点工");

        		}else {
        			for (int i = 0; i < str.length; i++) {
                		if (addServiceType != null) { 
                    		serviceTypeName.append( DictUtil.getAddServiceTypeItemName(str[i]));
                    	}
        			}
        			 getJspContext().getOut().write(serviceTypeName.toString());
				}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public String getAddServiceType() {
		return addServiceType;
	}

	public void setAddServiceType(String addServiceType) {
		this.addServiceType = addServiceType;
	}

	







}
