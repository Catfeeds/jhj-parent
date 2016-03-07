package com.jhj.vo.socials;

import com.jhj.po.model.socials.Socials;

/**
 * @description：
 * @author： kerryg
 * @date:2015年9月9日 
 */
public class SocialsVo extends Socials{
	
	private String titleSmallImg;
	
	private String titleImgs;
	
	private String beginDateStr;
	
	private String endDateStr;
	
	private String amMobile;
	

	public String getAmMobile() {
		return amMobile;
	}

	public void setAmMobile(String amMobile) {
		this.amMobile = amMobile;
	}

	public String getBeginDateStr() {
		return beginDateStr;
	}

	public void setBeginDateStr(String beginDateStr) {
		this.beginDateStr = beginDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getTitleImgs() {
		return titleImgs;
	}

	public void setTitleImgs(String titleImgs) {
		this.titleImgs = titleImgs;
	}

	public String getTitleSmallImg() {
		return titleSmallImg;
	}

	public void setTitleSmallImg(String titleSmallImg) {
		this.titleSmallImg = titleSmallImg;
	}
	
	

}
