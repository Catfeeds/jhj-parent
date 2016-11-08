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
import com.jhj.vo.PartnerServiceTypeVo;

/**
 *
 * @author :hulj
 * @Date : 2015年12月3日下午3:55:52
 * @Description: 
 *	
 *
 *		叮当大学--服务类别选择
 */
public class PartnerServiceTypeSelectTag extends SimpleTagSupport{
	
	private String selectId = "0";

    // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";
    
    
    private PartnerServiceTypeService partService;

	public PartnerServiceTypeSelectTag() {
	}
	
    @Override
    public void doTag() throws JspException, IOException {
        try {

    			WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());
    			partService = springContext.getBean(PartnerServiceTypeService.class);
    	        
    			
//    			List<PartnerServiceType> list = partService.selectAll();
    			PartnerServiceTypeVo serviceTypeVo = new PartnerServiceTypeVo();
    			serviceTypeVo.setEnable((short)1);
    			List<PartnerServiceType> list = partService.selectByPartnerServiceTypeVo(serviceTypeVo);
    				
            StringBuffer orgSelect = new StringBuffer();
            orgSelect.append("<select id = \"serviceTypeId\" name=\"serviceTypeId\" class=\"form-control\">" );

            if (hasAll.equals("1")) {
            	orgSelect.append("<option value='0' >请选择类别</option>");
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
