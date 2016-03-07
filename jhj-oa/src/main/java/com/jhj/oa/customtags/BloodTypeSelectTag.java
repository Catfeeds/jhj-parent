package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年9月16日上午11:51:11
 * @Description: 
 *		
 *		血型选择
 */
public class BloodTypeSelectTag extends SimpleTagSupport {
	
	private String bloodTypeId ;

	public BloodTypeSelectTag() {
	}
	
    @Override
    public void doTag() throws JspException, IOException {
        try {

        	List<String> list = OneCareUtil.getBloodType();	

            StringBuffer bloodTypeSelect = new StringBuffer();
            bloodTypeSelect.append("<select id = \"bloodType\" name=\"bloodType\" class=\"form-control\">");

            String item = null;
            String selected = "";
            for(int i = 0;  i<list.size();  i++) {
                item = list.get(i);
                selected = "";
                if (bloodTypeId != null && i==Short.valueOf(bloodTypeId)) {
                	selected = "selected=\"selected\"";
                }
                bloodTypeSelect.append("<option value='" +i + "' " + selected + ">" + item + "</option>");
            }
            bloodTypeSelect.append("</select>");
            getJspContext().getOut().write(bloodTypeSelect.toString());
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
