package com.jhj.service.impl.jhjSetting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.orderReview.JhjSettingMapper;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.service.jhjSetting.JhjSettingService;

/**
 *
 * @author :hulj
 * @Date : 2016年3月29日下午6:40:59
 * @Description: 
 *
 */
@Service
public class JhjSettingServiceImpl implements JhjSettingService {
	
	@Autowired
	private JhjSettingMapper settingMapper;
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<JhjSetting> selectByListPage() {
		// TODO Auto-generated method stub
		return null;
	}

}
