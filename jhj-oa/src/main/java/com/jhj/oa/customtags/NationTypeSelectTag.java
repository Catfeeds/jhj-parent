package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;
import com.meijia.utils.StringUtil;


/**
 *
 * @author :hulj
 * @Date : 2015年7月13日下午3:52:07
 * @Description: 
 *
 */
public class NationTypeSelectTag extends SimpleTagSupport{
	
	private String nationName;
	
	 // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";
	
	public NationTypeSelectTag() {
	}
	
	@Override
    public void doTag() throws JspException, IOException {
        try {

        	List<String> optionList = OneCareUtil.getNationType();

            StringBuffer nationTypeSelect = new StringBuffer();
            nationTypeSelect.append("<select id = \"nameId\" name=\"nation\" class=\"form-control\">");

            if (hasAll.equals("1")) {
            	nationTypeSelect.append("<option value='' >请选择民族</option>");
            }
            
            
            String item = "";
            String selected = "";
            for(int i = 0;  i<optionList.size();  i++) {
                item = optionList.get(i);
                selected = "";
                if (!StringUtil.isEmpty(nationName) && i == (Integer.parseInt(nationName))) {
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

	public String getNationName() {
		return nationName;
	}

	public void setNationName(String nationName) {
		this.nationName = nationName;
	}

	public String getHasAll() {
		return hasAll;
	}

	public void setHasAll(String hasAll) {
		this.hasAll = hasAll;
	}
	
	
}
