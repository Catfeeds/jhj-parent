package com.jhj.po.dao.orderReview;

import java.util.List;

import com.jhj.po.model.orderReview.JhjSetting;

public interface JhjSettingMapper {
    int deleteByPrimaryKey(Long id);

    int insert(JhjSetting record);

    int insertSelective(JhjSetting record);

    JhjSetting selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(JhjSetting record);

    int updateByPrimaryKey(JhjSetting record);

	JhjSetting selectBySettingType(String settingType);
	
	List<JhjSetting> selectByListPage();
	
}