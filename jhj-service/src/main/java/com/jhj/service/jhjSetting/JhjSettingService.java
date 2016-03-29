package com.jhj.service.jhjSetting;

import java.util.List;

import com.jhj.po.model.orderReview.JhjSetting;

/**
 *
 * @author :hulj
 * @Date : 2016年3月29日下午6:38:41
 * @Description: TODO
 *
 */
public interface JhjSettingService {
	
	int deleteByPrimaryKey(Long id);

    int insert(JhjSetting record);

    int insertSelective(JhjSetting record);

    JhjSetting selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(JhjSetting record);

    int updateByPrimaryKey(JhjSetting record);

	JhjSetting selectBySettingType(String settingType);
	
	List<JhjSetting> selectByListPage();
}
