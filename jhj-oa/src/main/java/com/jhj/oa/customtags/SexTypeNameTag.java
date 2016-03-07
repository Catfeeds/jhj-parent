package com.jhj.oa.customtags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;

public class SexTypeNameTag extends SimpleTagSupport {

    private Short sexType;

    public SexTypeNameTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	String sexTypeName = new String();

        		if (sexType != null) {
        			sexTypeName = OneCareUtil.getSexTypeName(sexType);
            	}
            getJspContext().getOut().write(sexTypeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public Short getSexType() {
		return sexType;
	}

	public void setSexType(Short sexType) {
		this.sexType = sexType;
	}








}
