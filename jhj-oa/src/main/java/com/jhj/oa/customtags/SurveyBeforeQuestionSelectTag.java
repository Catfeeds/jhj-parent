package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhj.po.model.survey.SurveyQuestion;
import com.jhj.service.survey.SurveyQuestionServcie;

/**
 *
 * @author :hulj
 * @Date : 2015年12月16日上午11:33:22
 * @Description: 
 *	
 *		问卷调查--设置 上一题、下一题时的所有题目选择
 *
 */
public class SurveyBeforeQuestionSelectTag extends SimpleTagSupport {

	private String selectId = "0";

    // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";
    
    private SurveyQuestionServcie questionService;
    
	public SurveyBeforeQuestionSelectTag() {
	}
    
    
    @Override
    public void doTag() throws JspException, IOException {
        try {

			WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());
			questionService = springContext.getBean(SurveyQuestionServcie.class);
			
			List<SurveyQuestion> list = questionService.selectByListPage();

            StringBuffer serviceSelect = new StringBuffer();
            serviceSelect.append("<select id = \"beforeQId\" name=\"beforeQId\" class=\"form-control\">" );

            if (hasAll.equals("1")) {
            	serviceSelect.append("<option value='0' >请选择上一题</option>");
            }

//            SurveyBank item = null;
            
            SurveyQuestion item = null;
            
            String selected = "";
            for(int i = 0;  i< list.size();  i++) {
                item = list.get(i);
                selected = "";
                if (selectId != null && item.getqId().toString().equals(selectId)) {
                		selected = "selected=\"selected\"";
                }
                serviceSelect.append("<option value='" + item.getqId()+ "' " + selected + ">" + item.getTitle()+ "</option>");
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
