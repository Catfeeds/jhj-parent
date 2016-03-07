package com.jhj.oa.customtags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.OneCareUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年9月4日下午7:35:15
 * @Description: 
 *		星座选择
 */
public class AstroSelectTag extends SimpleTagSupport {
	
	private String astroId ;

	public AstroSelectTag() {
		
	}
	
    @Override
    public void doTag() throws JspException, IOException {
        try {

        	List<String> list = OneCareUtil.getAstro();	

            StringBuffer astroSelect = new StringBuffer();
            astroSelect.append("<select id = \"astro\" name=\"astro\" class=\"form-control\">");

            String item = null;
            String selected = "";
            for(int i = 0;  i<list.size();  i++) {
                item = list.get(i);
                selected = "";
                if (astroId != null && i==Short.valueOf(astroId)) {
                	selected = "selected=\"selected\"";
                }
                astroSelect.append("<option value='" +i + "' " + selected + ">" + item + "</option>");
            }
            astroSelect.append("</select>");
            getJspContext().getOut().write(astroSelect.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * @return the astroId
	 */
	public String getAstroId() {
		return astroId;
	}

	/**
	 * @param astroId the astroId to set
	 */
	public void setAstroId(String astroId) {
		this.astroId = astroId;
	}

    
	
}
