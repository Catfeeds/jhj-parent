package com.jhj.oa.customtags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年9月16日下午2:35:55
 * @Description:
 *		列表页， 血型 name 展示
 */
public class BloodTypeNameTag extends SimpleTagSupport {
	
	private String  bloodTypeId;
	
	public BloodTypeNameTag() {
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		try {
        	String bloodTypeName = "";
        	if (bloodTypeId != null) {
        		bloodTypeName = OneCareUtil.getBloodTypeName(bloodTypeId);
        	}
            getJspContext().getOut().write(bloodTypeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	/**
	 * @return the bloodTypeId
	 */
	public String getBloodTypeId() {
		return bloodTypeId;
	}

	/**
	 * @param bloodTypeId the bloodTypeId to set
	 */
	public void setBloodTypeId(String bloodTypeId) {
		this.bloodTypeId = bloodTypeId;
	}
	
	
	
	
}
