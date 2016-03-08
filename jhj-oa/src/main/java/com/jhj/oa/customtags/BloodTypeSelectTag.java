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
/**
 *
 * @author :hulj
 * @Date : 2016年3月8日上午10:51:06
 * @Description: TODO
 *
 */
public class BloodTypeSelectTag extends SimpleTagSupport {
	
	private String bloodTypeId ;

	 // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";
	
	public BloodTypeSelectTag() {
	}
	
    @Override
    public void doTag() throws JspException, IOException {
        try {

        	List<String> list = OneCareUtil.getBloodType();	

            StringBuffer bloodTypeSelect = new StringBuffer();
            bloodTypeSelect.append("<select id = \"bloodType\" name=\"bloodType\" class=\"form-control\">");
            
            if (hasAll.equals("1")) {
            	bloodTypeSelect.append("<option value='0' >请选择血型</option>");
            }
            
            String item = null;
            String selected = "";
            for(int i = 0;  i<list.size();  i++) {
                item = list.get(i);
                selected = "";
                if (bloodTypeId != null && item.equals(bloodTypeId)) {
                	selected = "selected=\"selected\"";
                }
                bloodTypeSelect.append("<option value='" +item + "' " + selected + ">" + item + "</option>");
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

	/**
	 * @return the hasAll
	 */
	public String getHasAll() {
		return hasAll;
	}

	/**
	 * @param hasAll the hasAll to set
	 */
	public void setHasAll(String hasAll) {
		this.hasAll = hasAll;
	}

	
}
