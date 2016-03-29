package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.PageContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhj.po.model.bs.Orgs;
import com.jhj.service.bs.OrgsService;
import com.meijia.utils.OneCareUtil;


/**
 * 
 *
 * @author :hulj
 * @Date : 2016年3月9日17:32:16
 * @Description: 
 * 		
 *   jhj2.1 员工等级 选择
 *
 */
public class StaffLevelSelectTag extends SimpleTagSupport {

    private String level = "0";

    // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";
    
    public StaffLevelSelectTag() {

    }

    @Override
    public void doTag() throws JspException, IOException {
        try {

        	List<String> list = OneCareUtil.getStaffLevel();
        	
            StringBuffer levelSelect = new StringBuffer();
            levelSelect.append("<select id = \"level\" name=\"level\" class=\"form-control\">" );

            if (hasAll.equals("1")) {
            	levelSelect.append("<option value='0' >请选择员工等级</option>");
            }

            String item = "";
            String selected = "";
            for(int i = 0;  i< list.size();  i++) {
                item = list.get(i);
                selected = "";
                if (level != null && item.equals(level)) {
                		selected = "selected=\"selected\"";
                }
                levelSelect.append("<option value='" + (i + 1) + "' " + selected + ">"+ item+ "</option>");
            }

            levelSelect.append("</select>");

            getJspContext().getOut().write(levelSelect.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public String getHasAll() {
		return hasAll;
	}

	public void setHasAll(String hasAll) {
		this.hasAll = hasAll;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
