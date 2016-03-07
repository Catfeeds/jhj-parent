package com.jhj.oa.customtags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;

public class PayTypeNameTag extends SimpleTagSupport {

    private Short payType;
    private Short orderStatus;

    public PayTypeNameTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	String payTypeName = "";
        	if (orderStatus >=2) {
            		payTypeName = OneCareUtil.getPayTypeName(payType);
            	}
            getJspContext().getOut().write(payTypeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public Short getPayType() {
		return payType;
	}

	public void setPayType(Short payType) {
		this.payType = payType;
	}

	public Short getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}

}
