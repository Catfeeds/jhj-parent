package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;


/**
 *
 * @author :hulj
 * @Date : 2015年7月13日下午3:52:07
 * @Description: TODO
 *
 */
public class NationTypeSelectTag extends SimpleTagSupport{
	
	private String nameId;
	/**
	 * @return the nameId
	 */
	public String getNameId() {
		return nameId;
	}

	/**
	 * @param nameId the nameId to set
	 */
	public void setNameId(String nameId) {
		this.nameId = nameId;
	}

	public NationTypeSelectTag() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void doTag() throws JspException, IOException {
        try {

        	List<String> optionList = OneCareUtil.getNationType();

            StringBuffer nationTypeSelect = new StringBuffer();
            nationTypeSelect.append("<select id = \"nameId\" name=\"nation\" class=\"form-control\">");

            String item = null;
            String selected = "";
            for(int i = 0;  i<optionList.size();  i++) {
                item = optionList.get(i);
                selected = "";
                if (nameId != null && i==Integer.valueOf(nameId)) {
                	selected = "selected=\"selected\"";
                }
                nationTypeSelect.append("<option value='" +i + "' " + selected + ">" + item + "</option>");
            }
            nationTypeSelect.append("</select>");
            getJspContext().getOut().write(nationTypeSelect.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
