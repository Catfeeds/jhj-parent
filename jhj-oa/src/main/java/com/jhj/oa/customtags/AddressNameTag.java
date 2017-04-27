package com.jhj.oa.customtags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.users.UserAddrsService;

public class AddressNameTag extends SimpleTagSupport{
	
	private Long addrId;
	
	private UserAddrsService addrService;
	
	public AddressNameTag() {}

	@Override
	public void doTag() throws JspException, IOException {
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(((PageContext)getJspContext()).getServletContext());
		addrService = wac.getBean(UserAddrsService.class);
		
		UserAddrs userAddrs = addrService.selectByPrimaryKey(addrId);
		if(userAddrs!=null){
			StringBuffer sb = new StringBuffer();
			sb.append(userAddrs.getName()).append(userAddrs.getAddr());
			getJspContext().getOut().write(sb.toString());
		}else{
			getJspContext().getOut().write("");
		}
	}

	public Long getAddrId() {
		return addrId;
	}

	public void setAddrId(Long addrId) {
		this.addrId = addrId;
	}

}
