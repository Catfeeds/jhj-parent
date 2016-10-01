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
import com.jhj.vo.org.OrgSearchVo;


public class OrgSelectTag extends SimpleTagSupport {

    private String selectId = "0";
    
    private Long sessionOrgId = 0L;

    // 是否包含全部，  0 = 不包含  1= 包含
    private String hasAll =  "1";
    

	private OrgsService orgService;

    public OrgSelectTag() {
    	
    	
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {

    			WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());
    			orgService = springContext.getBean(OrgsService.class);
    	    
			
			OrgSearchVo searchVo = new OrgSearchVo();
			if (sessionOrgId.equals(0L)) {
				searchVo.setIsParent(1);
			} 
			
			if (sessionOrgId > 0L) {
				searchVo.setParentId(sessionOrgId);
			}
			searchVo.setOrgStatus((short) 1);
        	List<Orgs> orgList = orgService.selectBySearchVo(searchVo);
        	
        	

            StringBuffer orgSelect = new StringBuffer();
            
            //对于 “云店管理”时 用到的 门店选择会有用
            orgSelect.append("<select id = \"parentId\" name=\"parentId\" class=\"form-control\">" );

            if (hasAll.equals("1")) {
            	orgSelect.append("<option value='' >请选择门店</option>");
            }

            Orgs item = null;
            String selected = "";
            for(int i = 0;  i< orgList.size();  i++) {
                item = orgList.get(i);
                selected = "";
                if (selectId != null && item.getOrgId().toString().equals(selectId)) {
                		selected = "selected=\"selected\"";
                }
                orgSelect.append("<option value='" + item.getOrgId() + "' " + selected + ">" + item.getOrgName() + "</option>");
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

	public Long getSessionOrgId() {
		return sessionOrgId;
	}

	public void setSessionOrgId(Long sessionOrgId) {
		this.sessionOrgId = sessionOrgId;
	}





}
