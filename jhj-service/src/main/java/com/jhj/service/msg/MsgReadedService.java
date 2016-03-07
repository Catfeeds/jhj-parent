package com.jhj.service.msg;

import com.jhj.po.model.msg.MsgReaded;

public interface MsgReadedService {
	
	int deleteByPrimaryKey(Long id);

    int insert(MsgReaded record);

    int insertSelective(MsgReaded record);

    MsgReaded selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MsgReaded record);

    int updateByPrimaryKey(MsgReaded record);

    MsgReaded initMsgReaded();

	MsgReaded selectByUserIdAndMsgId(Long userId, Long msgId);


}
