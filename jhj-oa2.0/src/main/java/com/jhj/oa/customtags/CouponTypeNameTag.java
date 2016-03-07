package com.jhj.oa.customtags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;

public class CouponTypeNameTag extends SimpleTagSupport {

    private Short couponType;

    public CouponTypeNameTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	String couponTypeName = "";
        	if (couponType != null) {
        		couponTypeName = OneCareUtil.getCouponTypeName( couponType  );
        	}
            getJspContext().getOut().write(couponTypeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public Short getCouponType() {
		return couponType;
	}

	public void setCouponType(Short couponType) {
		this.couponType = couponType;
	}



}
