package com.jhj.service.impl.orderReview;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.orderReview.JhjSettingMapper;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.service.orderReview.SettingService;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月6日上午10:23:34
 * @Description:
 * 		
 * 		独立模块： 订单 评价
 *
 */
@Service
public class SettingServiceImpl implements SettingService {
	@Autowired
	private JhjSettingMapper settingMapper;
	
	@Override
	public JhjSetting initJhjSetting() {
		
		JhjSetting record = new JhjSetting();
		
		record.setId(0L);
		record.setName("");
		record.setSettingType("");
		record.setSettingValue("");
		record.setAddTime(TimeStampUtil.getNow()/1000);
		
		return record;
	}

	@Override
	public int deleteByPrimaryKey(Long id) {

		return settingMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(JhjSetting record) {
		
		return settingMapper.insert(record);
	}

	@Override
	public int insertSelective(JhjSetting record) {
		return settingMapper.insertSelective(record);
	}

	@Override
	public JhjSetting selectByPrimaryKey(Long id) {
		
		return settingMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(JhjSetting record) {
		
		return settingMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(JhjSetting record) {
		
		return settingMapper.updateByPrimaryKey(record);
	}

	@Override
	public JhjSetting selectBySettingType(String settingType) {
		
		return settingMapper.selectBySettingType(settingType);
	}

	@Override
	public List<JhjSetting> selectByListPage() {
		return settingMapper.selectByListPage();
	}

	
}
