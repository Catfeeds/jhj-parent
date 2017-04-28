package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.PartnerServiceTypeSearchVo;


public class ParentServiceTypeSelectTag extends SimpleTagSupport {

    private String selectId = "0";

    // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";
    
    private PartnerServiceTypeService partService;

    public ParentServiceTypeSelectTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	
        	WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());
			setPartService(springContext.getBean(PartnerServiceTypeService.class));
			PartnerServiceTypeSearchVo searchVo = new PartnerServiceTypeSearchVo();
			
			
			List<Long> serviceTypeIds = new ArrayList<Long>();
			serviceTypeIds.add(23L);
			serviceTypeIds.add(24L);
			serviceTypeIds.add(26L);
			serviceTypeIds.add(57L);
			serviceTypeIds.add(66L);
			searchVo.setServiceTypeIds(serviceTypeIds);
			
        	List<PartnerServiceType> optionList = partService.selectBySearchVo(searchVo);

            StringBuffer serviceTypeSelect = new StringBuffer();
            serviceTypeSelect.append("<select id = \"parentServiceType\" name=\"parentServiceType\" class=\"form-control\">");
            
            if (hasAll.equals("1")) {
            	serviceTypeSelect.append("<option value='0' >请选择服务大类</option>");
            }
            
            PartnerServiceType item = null;
            String selected = "";
            for(int i = 0;  i<optionList.size();  i++) {
                item = optionList.get(i);
                if (selectId != null && item.getServiceTypeId().toString().equals(selectId)) {
                	selected = "selected=\"selected\"";
                }
                
                serviceTypeSelect.append("<option value='" + item.getServiceTypeId() + "' " + selected + ">" + item.getName() + "</option>");
            }

            serviceTypeSelect.append("</select>");

            getJspContext().getOut().write(serviceTypeSelect.toString());
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

	public PartnerServiceTypeService getPartService() {
		return partService;
	}

	public void setPartService(PartnerServiceTypeService partService) {
		this.partService = partService;
	}

	public String getSelectId() {
		return selectId;
	}

	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}





}
