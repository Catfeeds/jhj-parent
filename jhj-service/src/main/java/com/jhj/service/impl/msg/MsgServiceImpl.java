package com.jhj.service.impl.msg;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.msg.MsgMapper;
import com.jhj.po.model.msg.Msg;
import com.jhj.service.msg.MsgService;
import com.jhj.vo.MsgSearchVo;
import com.jhj.vo.msg.MsgVo;
import com.jhj.vo.msg.OaMsgVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.TimeStampUtil;

@Service
public class MsgServiceImpl implements MsgService {
	@Autowired
	private MsgMapper msgMapper;
	
	@Override
	public Msg initMsg() {
		Msg record = new Msg();
	    
	    record.setMsgId(0L);
	    record.setTitle("");
	    record.setSummary("");
	    record.setGotoUrl("");
	    record.setAppType("");
	    record.setUserType((short)0L);
	    record.setSendTime(0L);
	    record.setIsSend((short)0L);
	    record.setIsEnable((short)0L);
	    record.setContent("");
	    record.setAddTime(TimeStampUtil.getNow() / 1000);
		return record;
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		
		return msgMapper.deleteByPrimaryKey(id);
	}


	@Override
	public int insert(Msg record) {
		
		return msgMapper.insert(record);
	}


	@Override
	public int insertSelective(Msg record) {

		return msgMapper.insertSelective(record);
	}


	@Override
	public Msg selectByPrimaryKey(Long id) {
		
		return msgMapper.selectByPrimaryKey(id);
	}


	@Override
	public int updateByPrimaryKeySelective(Msg record) {
		
		return msgMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Msg record) {
	
		return msgMapper.updateByPrimaryKey(record);
	}
	@Override
	public List<Msg> selectMsgListBySearchVo(MsgSearchVo searchVo, int page,
			int pAGE_MAX_NUMBER) {
		List<Msg> list = msgMapper.selectUserListByUserType(searchVo);
		return list;
	}
	@Override
	public MsgVo getMsgList(Msg msg) {

		MsgVo vo = new MsgVo();
		
		vo.setMsgId(msg.getMsgId());
		vo.setTitle(msg.getTitle());
		vo.setGotoUrl(msg.getGotoUrl());
		vo.setSummary(msg.getSummary());
		
		Long addTime = msg.getAddTime()*1000;
		vo.setAddTimeStr(TimeStampUtil.timeStampToDateStr(addTime, "MM-dd HH:mm"));
	
		return vo;
	}

	@Override
	public List<Msg> selectListPageBySearchVo(MsgSearchVo searchVo, int pageNo,
			int pageSize) {
		List<Msg> list = msgMapper.selectListPageBySearchVo(searchVo);
		return list;
	}

	
	@Override
	public OaMsgVo initOaMsgVo() {
		
		Msg msg = initMsg();
		
		OaMsgVo msgVo = new OaMsgVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(msg, msgVo);
		
		msgVo.setSendWay((short)0); //vo 设置为默认的 立即发送
		
		return msgVo;
	}
	

	@Override
	public List<Msg> selectRightNowMsgByUserType(Short userType) {
		return msgMapper.selectRightNowMsgByUserType(userType);
	}

	@Override
	public List<Msg> selectTimeMsgByUserType(Short userType) {
		return msgMapper.selectTimeMsgByUserType(userType);
	}
}
	

