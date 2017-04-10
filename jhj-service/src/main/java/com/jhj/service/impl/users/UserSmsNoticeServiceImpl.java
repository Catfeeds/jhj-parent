package com.jhj.service.impl.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.common.Constants;
import com.jhj.po.dao.user.UserSmsNoticeMapper;
import com.jhj.po.model.user.UserSmsNotice;
import com.jhj.service.users.UserSmsNoticeService;
import com.jhj.vo.user.UserSmsNoticeSearchVo;
import com.meijia.utils.TimeStampUtil;

@Service
public class UserSmsNoticeServiceImpl implements UserSmsNoticeService {
	
	@Autowired
	private UserSmsNoticeMapper mapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(UserSmsNotice record) {
		return mapper.insert(record);
	}

	@Override
	public int insertSelective(UserSmsNotice record) {
		return mapper.insertSelective(record);
	}

	@Override
	public UserSmsNotice selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(UserSmsNotice record) {
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(UserSmsNotice record) {
		return mapper.updateByPrimaryKey(record);
	}

	@Override
	public UserSmsNotice initPo() {

		UserSmsNotice record = new UserSmsNotice();
		record.setId(0L);
		record.setUserId(0L);
		record.setMobile("");
		record.setUserType((short) 0);
		record.setLastMonth((short) 0);
		record.setSmsTemplateId("");
		record.setRemarks("");
		record.setIsSuceess(Constants.SMS_SUCCESS);
		record.setSmsReturn("");
		record.setAddTime(TimeStampUtil.getNowSecond());
		record.setUpdateTime(TimeStampUtil.getNowSecond());
		return record;
	}

	@Override
	public PageInfo selectByListPage(UserSmsNoticeSearchVo searchVo,
			int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<UserSmsNotice> list = mapper.selectByListPage(searchVo);
		
		PageInfo pageInfo = new PageInfo(list);
		return pageInfo;
	}

	@Override
	public List<UserSmsNotice> selectBySearchVo(UserSmsNoticeSearchVo searchVo) {
		return mapper.selectBySearchVo(searchVo);
	}
	 
	@Override
	public Integer totalBySearchVo(UserSmsNoticeSearchVo searchVo) {
		Integer total = mapper.totalBySearchVo(searchVo);
		
		if (total == null) total = 0;
		return total;
	}

}
