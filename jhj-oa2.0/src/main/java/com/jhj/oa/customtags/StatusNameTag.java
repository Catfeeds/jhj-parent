package com.jhj.oa.customtags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;

public class StatusNameTag extends SimpleTagSupport {

    private Short status;

    public StatusNameTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	String dispatchStatusName = "";
        	if (status != null) {
        		dispatchStatusName = OneCareUtil.getStatusName( status  );
        	}
            getJspContext().getOut().write(dispatchStatusName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}




}
