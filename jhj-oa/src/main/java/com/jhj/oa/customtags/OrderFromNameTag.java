package com.jhj.oa.customtags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;

public class OrderFromNameTag extends SimpleTagSupport {

    private String orderFrom;

    public OrderFromNameTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	String orderFromName = "";
        	if (orderFrom != null) {
        		orderFromName = OneCareUtil.getOrderFromName( Short.valueOf(orderFrom)  );
        	}
            getJspContext().getOut().write(orderFromName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

}
