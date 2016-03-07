package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;

public class DegreeTypeSelectTag extends SimpleTagSupport {

    private String degreeId ;

    public DegreeTypeSelectTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {

        		List<String> optionList = OneCareUtil.getDegreeType();

            StringBuffer serviceTypeSelect = new StringBuffer();
            serviceTypeSelect.append("<select id = \"eduId\" name=\"edu\" class=\"form-control\">");

            String item = null;
            String selected = "";
            for(int i = 0;  i<optionList.size();  i++) {
                item = optionList.get(i);
                selected = "";
                if (degreeId != null && i==Integer.valueOf(degreeId)) {
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
	public String getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(String degreeId) {
		this.degreeId = degreeId;
	}
}
