package com.jhj.oa.customtags;

import java.io.IOException;

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
 * @Date : 2015年12月3日下午7:03:34
 * @Description: TODO
 *
 */
public class PartnerServiceTypeNameTag extends SimpleTagSupport{

	private Long typeId;
	
	private PartnerServiceTypeService partService;
	
	public PartnerServiceTypeNameTag() {
	}
	  
    @Override
    public void doTag() throws JspException, IOException {
        try {
        	
        	
        	WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());
			partService = springContext.getBean(PartnerServiceTypeService.class);
        	
        	
        	PartnerServiceType partnerServiceType = partService.selectByPrimaryKey(typeId);
			
            getJspContext().getOut().write(partnerServiceType.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

    
	
}
