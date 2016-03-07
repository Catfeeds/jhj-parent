package com.jhj.oa.customtags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年9月5日上午10:55:11
 * @Description: 
 *		星座名称--- 助理人员列表页展示
 */	
public class AstroNameTag extends SimpleTagSupport{
	
	private Short astroId;	//数据库存的是 0~11 ，代表 魔羯座。。。射手座
	
	public AstroNameTag() {
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		try {
        	String astroName = "";
        	if (astroId != null) {
        		astroName = OneCareUtil.getAstroName(astroId);
        	}
            getJspContext().getOut().write(astroName);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public Short getAstroId() {
		return astroId;
	}

	public void setAstroId(Short astroId) {
		this.astroId = astroId;
	}
	
	
}
