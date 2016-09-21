package com.jhj.po.dao.msg;

import java.util.List;

import com.jhj.po.model.msg.Msg;
import com.jhj.vo.MsgSearchVo;

public interface MsgMapper {
    int deleteByPrimaryKey(Long msgId);

    int insert(Msg record);

    int insertSelective(Msg record);

    Msg selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(Msg record);

    int updateByPrimaryKeyWithBLOBs(Msg record);

    int updateByPrimaryKey(Msg record);

	List<Msg> selectUserListByUserType(MsgSearchVo searchVo);

	List<Msg> selectListPageBySearchVo(MsgSearchVo searchVo);
	
	//根据用户类型得到 立即发送的 消息
	List<Msg> selectRightNowMsgByUserType(Short userType);
	
	//根据用户类型得到 定时发送的 消息
	List<Msg> selectTimeMsgByUserType(Short userType);
	
}