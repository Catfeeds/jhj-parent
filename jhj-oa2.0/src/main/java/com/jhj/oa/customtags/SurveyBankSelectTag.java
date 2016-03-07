package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhj.po.model.survey.SurveyBank;
import com.jhj.service.survey.SurveyBankService;

/**
 *
 * @author :hulj
 * @Date : 2015年12月14日下午6:08:10
 * @Description: 
 *		
 *		问卷调查--题库选择
 */
public class SurveyBankSelectTag extends SimpleTagSupport {
	
	private String selectId = "0";

    // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";
    
    
    private SurveyBankService service;
    
	public SurveyBankSelectTag() {
	}

    @Override
    public void doTag() throws JspException, IOException {
        try {

			WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());
			service = springContext.getBean(SurveyBankService.class);
			
			List<SurveyBank> list = service.selectByListPage();

            StringBuffer serviceSelect = new StringBuffer();
            serviceSelect.append("<select id = \"bankId\" name=\"bankId\" class=\"form-control\">" );

            if (hasAll.equals("1")) {
            	serviceSelect.append("<option value='0' >请选择题库</option>");
            }

            SurveyBank item = null;
            
            String selected = "";
            for(int i = 0;  i< list.size();  i++) {
                item = list.get(i);
                selected = "";
                if (selectId != null && item.getBankId().toString().equals(selectId)) {
                		selected = "selected=\"selected\"";
                }
                serviceSelect.append("<option value='" + item.getBankId() + "' " + selected + ">" + item.getName() + "</option>");
            }

            serviceSelect.append("</select>");

            getJspContext().getOut().write(serviceSelect.toString());
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

	public String getSelectId() {
		return selectId;
	}

	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}

	
}
