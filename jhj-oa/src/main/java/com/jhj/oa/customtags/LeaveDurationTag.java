package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author :hulj
 * @Date : 2016年5月12日下午4:54:59
 * @Description: 
 *		
 *		请假时间段选择
 *		
 *		可选选项    
 *				8点-12点	、8点-21点、12点-21点
 *
 */
public class LeaveDurationTag extends SimpleTagSupport {
	
	private String selectId;

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	
        	Map<String, String> map = new LinkedHashMap<String, String>();
        	
        	map.put("0", "上午");
        	map.put("1", "全天");
        	map.put("2", "下午");
        	
            StringBuffer leaveDurationSelect = new StringBuffer();
            leaveDurationSelect.append("<select id = \"leaveDuration\" name=\"leaveDuration\" class=\"form-control\">" );

            
            String selected = this.getSelectId();
            
            Set<Entry<String,String>> set = map.entrySet();
            for (Entry<String, String> entry : set) {
				
            	String isSelect = "";
            	String key = entry.getKey();
            	
            	if(isSelect != null && key.equals(selected)){
            		isSelect = "selected=\"selected\"";
            	}
            	leaveDurationSelect.append("<option value='" + key + "' " + isSelect + ">" + entry.getValue() + "</option>");
			}

            leaveDurationSelect.append("</select>");

            getJspContext().getOut().write(leaveDurationSelect.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public String getSelectId() {
		return selectId;
	}

	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}
	
}
