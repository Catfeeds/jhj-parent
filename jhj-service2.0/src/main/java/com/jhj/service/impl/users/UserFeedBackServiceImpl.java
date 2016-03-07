package com.jhj.service.impl.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.user.UserFeedbackMapper;
import com.jhj.po.model.user.UserFeedback;
import com.jhj.service.users.UserFeedBackService;
import com.meijia.utils.TimeStampUtil;
@Service
public class UserFeedBackServiceImpl implements UserFeedBackService {

	@Autowired
	private UserFeedbackMapper userFeedbackMapper;
	@Override
	public int deleteByPrimaryKey(Long id) {
		return userFeedbackMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(UserFeedback record) {
		return userFeedbackMapper.insert(record);
	}

	@Override
	public int insertSelective(UserFeedback record) {
		return userFeedbackMapper.insertSelective(record);
	}

	@Override
	public UserFeedback selectByPrimaryKey(Long id) {
		return userFeedbackMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(UserFeedback record) {
		return userFeedbackMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(UserFeedback record) {
		return userFeedbackMapper.updateByPrimaryKey(record);
	}

	@Override
	public UserFeedback selectByUserId(Long userId) {
		return userFeedbackMapper.selectByUserId(userId);
	}

	@Override
	public UserFeedback initUserFeedBack() {
		UserFeedback userFeedback = new UserFeedback();
		userFeedback.setUserId(0L);
		userFeedback.setContent("");
		userFeedback.setMobile("");
		userFeedback.setAddTime(TimeStampUtil.getNowSecond());
		return userFeedback;
	}
	

}
