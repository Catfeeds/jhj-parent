package com.jhj.oa.customtags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;

public class OrderVoStatusNameTag extends SimpleTagSupport {

    private Short orderStatus;
    
    private Short orderType;
    
    public OrderVoStatusNameTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	String orderStatusName = "";
        	if (orderStatus != null) {
        		
//        		orderStatusName = OneCareUtil.getOrderStausName( orderStatus  );
        		
        		orderStatusName = OneCareUtil.getJhjOrderStausNameFromOrderType(orderType,orderStatus);
        	}
            getJspContext().getOut().write(orderStatusName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public Short getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Short getOrderType() {
		return orderType;
	}

	public void setOrderType(Short orderType) {
		this.orderType = orderType;
	}

	

}
