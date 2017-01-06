package com.jhj.oa.customtags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;

public class UserDetailOrderTypeNameTag extends SimpleTagSupport {

	  private Short orderTypeId;

    public UserDetailOrderTypeNameTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	String orderTypeName = "";
        	if (orderTypeId != null) {
        		orderTypeName = OneCareUtil.getOrderTypeForDetailPay(orderTypeId);
        	}
            getJspContext().getOut().write(orderTypeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public Short getOrderTypeId() {
		return orderTypeId;
	}

	public void setOrderTypeId(Short orderTypeId) {
		this.orderTypeId = orderTypeId;
	}



}
