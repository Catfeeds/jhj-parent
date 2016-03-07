package com.jhj.po.dao.msg;

import com.jhj.po.model.msg.MsgReaded;

public interface MsgReadedMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MsgReaded record);

    int insertSelective(MsgReaded record);

    MsgReaded selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MsgReaded record);

    int updateByPrimaryKey(MsgReaded record);

	MsgReaded selectByUserIdAndMsgId(Long userId, Long msgId);

}