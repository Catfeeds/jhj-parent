package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhj.po.model.survey.SurveyService;
import com.jhj.service.survey.SurveyServiceService;

/**
 *
 * @author :hulj
 * @Date : 2015年12月12日下午5:06:59
 * @Description: 
 *
 *		问卷调查--服务大类选择
 */
public class SurveyServiceSelectTag extends SimpleTagSupport {
	
	 	private String selectId = "0";

	    // 是否包含全部，  0 = 不包含  1= 包含
	    private String hasAll =  "1";
	    
	    
	    private SurveyServiceService service;
	    
		public SurveyServiceSelectTag() {
		}

	    @Override
	    public void doTag() throws JspException, IOException {
	        try {

    			WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());
    			service = springContext.getBean(SurveyServiceService.class);
    	        

        		List<SurveyService> list = service.selectByListPage();

	            StringBuffer serviceSelect = new StringBuffer();
	            serviceSelect.append("<select id = \"serviceId\" name=\"serviceId\" class=\"form-control\">" );

	            if (hasAll.equals("1")) {
	            	serviceSelect.append("<option value='0' >请选择服务大类</option>");
	            }

	            SurveyService item = null;
	            String selected = "";
	            for(int i = 0;  i< list.size();  i++) {
	                item = list.get(i);
	                selected = "";
	                if (selectId != null && item.getServiceId().toString().equals(selectId)) {
	                		selected = "selected=\"selected\"";
	                }
	                serviceSelect.append("<option value='" + item.getServiceId() + "' " + selected + ">" + item.getName() + "</option>");
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
