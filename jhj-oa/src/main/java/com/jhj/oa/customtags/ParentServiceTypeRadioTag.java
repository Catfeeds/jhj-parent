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


/**
 * @author hulj
 *
 */
public class ParentServiceTypeRadioTag extends SimpleTagSupport {

    private String radioId = "0";

    // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";
    
    private PartnerServiceTypeService partService;

    public ParentServiceTypeRadioTag() {
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

            StringBuffer serviceTypeRadio = new StringBuffer();
            
            PartnerServiceType item = null;
            for(int i = 0;  i<optionList.size();  i++) {
            	String checked = "";
                item = optionList.get(i);
                if (radioId != null && item.getServiceTypeId().toString().equals(radioId)) {
                	checked = "checked=\"checked\"";
                }
                if(item.getServiceTypeId().toString().equals("23")){
                	checked = "checked=\"checked\"";
                }
         
                serviceTypeRadio.append("<label class='checkbox-inline'><input type='radio' name=\"parentServiceType\" "+checked+" class='parentServiceType' value='"+item.getServiceTypeId()+"'>")
                .append(item.getName()).append("</label>");
            }

            getJspContext().getOut().write(serviceTypeRadio.toString());
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

	public String getRadioId() {
		return radioId;
	}

	public void setRadioId(String radioId) {
		this.radioId = radioId;
	}

}
