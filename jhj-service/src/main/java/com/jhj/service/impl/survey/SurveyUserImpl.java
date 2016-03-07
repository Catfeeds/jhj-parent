package com.jhj.service.impl.survey;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.survey.SurveyUserMapper;
import com.jhj.po.model.survey.SurveyUser;
import com.jhj.service.survey.SurveyUserService;
import com.meijia.utils.TimeStampUtil;

/**
 * 
 *
 * @author :hulj
 * @Date : 2015年12月22日上午11:22:46
 * @Description: 
 *		
 *		问卷调查--用户信息service
 */
@Service
public class SurveyUserImpl implements SurveyUserService {
	
	@Autowired
	private SurveyUserMapper userMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(SurveyUser record) {
		return userMapper.insertSelective(record);
	}

	@Override
	public SurveyUser selectByPrimaryKey(Long id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SurveyUser record) {
		return userMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public SurveyUser initUser() {
		
		SurveyUser user = new SurveyUser();
		
		user.setId(0L);
		user.setUserName("");
		user.setSex((short)0);	//0= 男    1=女
		user.setAge(0L);
		user.setMobile("");
		user.setAddTime(TimeStampUtil.getNowSecond());
		
		return user;
	}

	@Override
	public List<SurveyUser> selectByIdList(List<Long> useIdList) {
		return userMapper.selectByIdList(useIdList);
	}

}
