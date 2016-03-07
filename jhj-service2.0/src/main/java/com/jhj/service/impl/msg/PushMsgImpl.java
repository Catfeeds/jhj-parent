package com.jhj.service.impl.msg;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.msg.Msg;
import com.jhj.po.model.user.UserPushBind;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.msg.MsgService;
import com.jhj.service.msg.PushMsgService;
import com.jhj.service.users.UserPushBindService;
import com.meijia.utils.GsonUtil;
import com.meijia.utils.PushUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年1月29日下午5:01:45
 * @Description: 
 *		
 *		 消息推送接口的具体实现
 *			
 *			目前只推送给 服务人员
 */
@Service
public class PushMsgImpl implements PushMsgService {
	
	@Autowired
	private OrgStaffsService staffService;
	@Autowired
	private MsgService msgService;
	@Autowired
	private UserPushBindService bindService;
	
	@Override
	public void pushMsg(Long msgId) throws Exception {
		
		Msg msg = msgService.selectByPrimaryKey(msgId);
		
		//可以 接受推送消息的 --服务人员
		List<OrgStaffs> list = staffService.selectAbleToSendMsgStaff();
		
		for (OrgStaffs orgStaffs : list) {
			
			/*
			 *  1. 构造 消息map
			 */
			// 透传消息 参数 map
			HashMap<String, String> paramsMap = new HashMap<String, String>();
			
			Long staffId = orgStaffs.getStaffId();
			
			//取得 cid
			UserPushBind userPushBind = bindService.selectByUserId(staffId);
			
			String clientId = userPushBind.getClientId();
			
			paramsMap.put("cid", clientId);
			
			HashMap<String, String> transMap = new HashMap<String, String>();
			
			transMap.put("is_show", "true");
			transMap.put("action", "msg");
			transMap.put("remind_title", msg.getTitle());
			transMap.put("remind_content", msg.getSummary());
			
			String jsonParams = GsonUtil.GsonString(transMap);
			paramsMap.put("transmissionContent", jsonParams);
			
			/*
			 * 2.判断是 定时推送 还是 立即推送
			 */
			
			Long sendTime = msg.getSendTime();
			
			Long nowSecond = TimeStampUtil.getNowSecond();
			
			if(sendTime == 0){
				//立即推送
				boolean flag = PushUtil.AndroidPushToSingle(paramsMap);
				
				//发送之后,更新 该 消息为 已发送
				msg.setIsSend((short)1);
				
				msgService.updateByPrimaryKeySelective(msg);
			}else{
				
				//定时推送
				if(nowSecond == sendTime){
					
					boolean flag = PushUtil.AndroidPushToSingle(paramsMap);
					
					//发送之后,更新 该 消息为 已发送
					msg.setIsSend((short)1);
					
					msgService.updateByPrimaryKeySelective(msg);
				}
			}
		}
		
	}
}
