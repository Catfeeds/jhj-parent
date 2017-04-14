package com.jhj.oa.vo;

import java.util.List;

import com.jhj.po.model.period.PeriodServiceType;

public class PeriodServiceTypeVo extends PeriodServiceType{
	
	private List<String> packageTypeList;

	public List<String> getPackageTypeList() {
		return packageTypeList;
	}

	public void setPackageTypeList(List<String> packageTypeList) {
		this.packageTypeList = packageTypeList;
	}

}
