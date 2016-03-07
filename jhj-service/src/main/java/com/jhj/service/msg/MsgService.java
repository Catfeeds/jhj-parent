package com.jhj.service.msg;

import java.util.List;

import com.jhj.po.model.msg.Msg;
import com.jhj.vo.MsgSearchVo;
import com.jhj.vo.msg.MsgVo;
import com.jhj.vo.msg.OaMsgVo;

public interface MsgService {
	
	int deleteByPrimaryKey(Long msgId);

    int insert(Msg record);

    int insertSelective(Msg record);

    Msg selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(Msg record);

    int updateByPrimaryKey(Msg record);

    Msg initMsg();

	MsgVo getMsgList(Msg msg);

	List<Msg> selectMsgListBySearchVo(MsgSearchVo searchVo, int page,
			int pAGE_MAX_NUMBER);

	List<Msg> selectListPageBySearchVo(MsgSearchVo searchVo, int pageNo,
			int pageSize);
	
	
	OaMsgVo initOaMsgVo();

	
	//根据用户类型得到 立即发送的 消息
	List<Msg> selectRightNowMsgByUserType(Short userType);
	
	//根据用户类型得到 定时发送的 消息
	List<Msg> selectTimeMsgByUserType(Short userType);
	
}
