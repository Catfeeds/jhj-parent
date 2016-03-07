package com.jhj.service.orderReview;

import java.util.List;

import com.jhj.po.model.orderReview.JhjSetting;
/**
 * 
 *
 * @author :hulj
 * @Date : 2016年2月22日上午10:59:08
 * @Description: 
 *		
 *	 后台--基础配置--app参数配置
 */
public interface SettingService {
	
	int deleteByPrimaryKey(Long id);

    int insert(JhjSetting record);

    int insertSelective(JhjSetting record);

    JhjSetting selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(JhjSetting record);

    int updateByPrimaryKey(JhjSetting record);
    
    JhjSetting initJhjSetting();

	JhjSetting selectBySettingType(String settingType);
	
	List<JhjSetting> selectByListPage();
	
}
