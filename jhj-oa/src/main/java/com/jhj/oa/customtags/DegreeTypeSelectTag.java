package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;
import com.meijia.utils.StringUtil;

public class DegreeTypeSelectTag extends SimpleTagSupport {

    private String edu ;

    // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";
    
    public DegreeTypeSelectTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {

        	List<String> optionList = OneCareUtil.getDegreeType();

            StringBuffer serviceTypeSelect = new StringBuffer();
            serviceTypeSelect.append("<select id = \"eduId\" name=\"edu\" class=\"form-control\">");

            if (hasAll.equals("1")) {
            	serviceTypeSelect.append("<option value='' >请选择学历</option>");
            }
            
            
            String item = "";
            String selected = "";
            for(int i = 0;  i<optionList.size();  i++) {
                item = optionList.get(i);
                selected = "";
                if (!StringUtil.isEmpty(edu)&& i== Integer.valueOf(edu)) {
                	selected = "selected=\"selected\"";
                }
                serviceTypeSelect.append("<option value='" +i + "' " + selected + ">" + item + "</option>");
            }
            serviceTypeSelect.append("</select>");
            getJspContext().getOut().write(serviceTypeSelect.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public String getEdu() {
		return edu;
	}

	public void setEdu(String edu) {
		this.edu = edu;
	}

	public String getHasAll() {
		return hasAll;
	}

	public void setHasAll(String hasAll) {
		this.hasAll = hasAll;
	}
	
}
