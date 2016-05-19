package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhj.po.model.bs.Orgs;
import com.jhj.service.bs.OrgsService;

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
	
	private String selectId = "0";

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	
        	Map<String, String> map = new LinkedHashMap<String, String>();
        	
        	map.put("0", "上午");
        	map.put("1", "下午");
        	map.put("2", "全天");
        	
            StringBuffer leaveDurationSelect = new StringBuffer();
            leaveDurationSelect.append("<select id = \"leaveDuration\" name=\"leaveDuration\" class=\"form-control\">" );

            String selected = "";
            
            Set<Entry<String,String>> set = map.entrySet();
            for (Entry<String, String> entry : set) {
				
            	String key = entry.getKey();
            	selected = "";
            	
            	if(selected != null && key.equals(selected)){
            		selected = "selected=\"selected\"";
            	}
            	leaveDurationSelect.append("<option value='" + key + "' " + selected + ">" + entry.getValue() + "</option>");
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
