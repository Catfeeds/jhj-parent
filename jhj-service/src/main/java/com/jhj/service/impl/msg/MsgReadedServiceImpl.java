package com.jhj.service.impl.msg;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.msg.MsgReadedMapper;
import com.jhj.po.model.msg.MsgReaded;
import com.jhj.service.msg.MsgReadedService;
import com.meijia.utils.TimeStampUtil;

@Service
public class MsgReadedServiceImpl implements MsgReadedService {
	@Autowired
	private MsgReadedMapper msgReadedMapper;
	
	@Override
	public MsgReaded initMsgReaded() {
		MsgReaded record = new MsgReaded();

	    record.setId(0L);
	    record.setUserType((short)0L);
	    record.setUserId(0L);
	    record.setMsgId(0L);
	    record.setAddTime(TimeStampUtil.getNow() / 1000);
		return record;
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		
		return msgReadedMapper.deleteByPrimaryKey(id);
	}


	@Override
	public int insert(MsgReaded record) {
		
		return msgReadedMapper.insert(record);
	}


	@Override
	public int insertSelective(MsgReaded record) {

		return msgReadedMapper.insertSelective(record);
	}


	@Override
	public MsgReaded selectByPrimaryKey(Long id) {
		
		return msgReadedMapper.selectByPrimaryKey(id);
	}


	@Override
	public int updateByPrimaryKeySelective(MsgReaded record) {
		
		return msgReadedMapper.updateByPrimaryKeySelective(record);
	}


	@Override
	public int updateByPrimaryKey(MsgReaded record) {
	
		return msgReadedMapper.updateByPrimaryKey(record);
	}

	@Override
	public MsgReaded selectByUserIdAndMsgId(Long userId, Long msgId) {
	
		return msgReadedMapper.selectByUserIdAndMsgId(userId,msgId);
		
	}

	
}
	

