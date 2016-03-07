package com.jhj.oa.customtags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.jhj.service.dict.DictUtil;

public class CityNameTag extends SimpleTagSupport {

    private String cityId;

    public CityNameTag() {
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        	String cityName = "";
        	if (cityId != null) {
        		cityName = DictUtil.getCityName(Long.valueOf( cityId) );
        	}
            getJspContext().getOut().write(cityName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}





}
