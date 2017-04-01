package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.PageContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhj.po.model.admin.AdminAccount;
import com.jhj.po.model.bs.Orgs;
import com.jhj.service.admin.AdminAccountService;
import com.jhj.service.bs.OrgsService;
import com.jhj.vo.AdminAccountSearchVo;
import com.jhj.vo.org.OrgSearchVo;


/**
 * 
 *
 * 		
 * 	管理员人员选择
 *
 */
public class AdminAccountSelectTag extends SimpleTagSupport {

    private Long roleId = 0L;
    
    private String selectId = "0";
        
	private AdminAccountService adminAccountService;

    public AdminAccountSelectTag() {
    	
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {

			WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());
			adminAccountService = springContext.getBean(AdminAccountService.class);
    	    	
			List<AdminAccount> list = new ArrayList<AdminAccount>();
			
			List<Long> roleIds = new ArrayList<Long>();
			roleIds.add(1L);
			roleIds.add(2L);
			roleIds.add(3L);
			roleIds.add(4L);
			roleIds.add(5L);
			roleIds.add(9L);
			roleIds.add(12L);
			AdminAccountSearchVo searchVo = new AdminAccountSearchVo();
			searchVo.setRoleIds(roleIds);
			list = adminAccountService.selectBySearchVo(searchVo);
			
			
//			if (roleId.equals(5L)) {
//				list = adminAccountService.selectByRoleId(roleId);
//			} else {
//				List<Long> roleIds = new ArrayList<Long>();
//				roleIds.add(2L);
//				roleIds.add(3L);
//				roleIds.add(4L);
//				roleIds.add(5L);
//				AdminAccountSearchVo searchVo = new AdminAccountSearchVo();
//				searchVo.setRoleIds(roleIds);
//				list = adminAccountService.selectBySearchVo(searchVo);
//			}

            StringBuffer selectHtml = new StringBuffer();
            selectHtml.append("<select id = \"adminId\" name=\"adminId\" class=\"form-control\">" );
            selectHtml.append("<option value='0'>全部</option>");
            AdminAccount item = null;
            String selected = "";
            for(int i = 0;  i< list.size();  i++) {
                item = list.get(i);
                selected = "";
                
                if (selectId != null && item.getId().toString().equals(selectId)) {
            		selected = "selected=\"selected\"";
                }
               
                selectHtml.append("<option value='" + item.getId() + "' " + selected + ">" + item.getName() + "</option>");
            }

            selectHtml.append("</select>");

            getJspContext().getOut().write(selectHtml.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public AdminAccountService getAdminAccountService() {
		return adminAccountService;
	}

	public void setAdminAccountService(AdminAccountService adminAccountService) {
		this.adminAccountService = adminAccountService;
	}

	public String getSelectId() {
		return selectId;
	}

	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}

}
