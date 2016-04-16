package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.university.PartnerServiceTypeService;

/**
 *
 * @author :hulj
 * @Date : 2016年4月13日下午2:54:35
 * @Description: 
 *	
 *		jhj2.1    服务大类选择
 *
 *		对比之前的 直接选择 具体服务，2.1版本 多了 服务大类 选择，
 *
 *		如 贴心家事--安心托管。。。
 *			
 *    此自定义标签 用于  在 叮当大学--培训学习，选择 服务大类时 使用		
 *	
 */
public class PartnerNoParentServiceSelectTag extends SimpleTagSupport{
	
	private String selectId = "0";

    // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";
    
    
    private PartnerServiceTypeService partService;

	public PartnerNoParentServiceSelectTag() {
		
	}
	
    @Override
    public void doTag() throws JspException, IOException {
        try {

			WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());
			partService = springContext.getBean(PartnerServiceTypeService.class);
	        
			
			List<PartnerServiceType> list = partService.selectNoParentServiceObj();
    				
            StringBuffer orgSelect = new StringBuffer();
            orgSelect.append("<select id = \"serviceTypeId\" name=\"serviceTypeId\" class=\"form-control\">" );

            if (hasAll.equals("1")) {
            	orgSelect.append("<option value='0' >请选择服务大类</option>");
            }

            PartnerServiceType item = null;
            String selected = "";
            for(int i = 0;  i< list.size();  i++) {
                item = list.get(i);
                selected = "";
                if (selectId != null && item.getServiceTypeId().toString().equals(selectId)) {
                		selected = "selected=\"selected\"";
                }
                orgSelect.append("<option value='" + item.getServiceTypeId() + "' " + selected + ">" + item.getName() + "</option>");
            }

            orgSelect.append("</select>");

            getJspContext().getOut().write(orgSelect.toString());
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
