package com.jhj.oa.customtags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

public class TimeStampTag extends SimpleTagSupport {

    private String t;
    private String patten;

    public TimeStampTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	String output = "";
        	if (!StringUtil.isNull(t)) {
        		Long tt = Long.valueOf(t);
        		if (tt > 0) {
        			output = TimeStampUtil.timeStampToDateStr(tt, patten);
        		}
        	}
            getJspContext().getOut().write(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getPatten() {
		return patten;
	}

	public void setPatten(String patten) {
		this.patten = patten;
	}

}
