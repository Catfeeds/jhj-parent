package com.jhj.action.msg;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.BaseController;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.msg.Msg;
import com.jhj.po.model.user.UserPushBind;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.msg.MsgService;
import com.jhj.service.users.UserPushBindService;
import com.meijia.utils.GsonUtil;
import com.meijia.utils.PushUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年1月29日上午11:03:05
 * @Description: 
 *	
 *		消息推送
 */
@Controller
@RequestMapping(value ="/msg")
public class MsgSendController extends BaseController {
	
	
	@Autowired
	private UserPushBindService bindService;
	@Autowired
	private MsgService msgService;
	
	@Autowired
	private OrgStaffsService staffService;
	
	/*
	 *  为服务人员 推送  定时消息
	 */
	@RequestMapping(value = "send_msg_by_time.json",method = RequestMethod.GET)
	public void  sendMsgByTime() throws Exception{
		
		//可以 接受推送消息的 服务人员
		List<OrgStaffs> list = staffService.selectAbleToSendMsgStaff();
		
		//服务人员消息--定时推送的消息
		List<Msg> msgList = msgService.selectTimeMsgByUserType(Constants.MSG_USER_TYPE_1);
		
		for (OrgStaffs orgStaffs : list) {
			
			//透传消息 参数 map
			HashMap<String, String> paramsMap = new HashMap<String, String>();
			
			Long staffId = orgStaffs.getStaffId();
			
			//取得 cid
			UserPushBind userPushBind = bindService.selectByUserId(staffId);
			
			String clientId = userPushBind.getClientId();
			
			paramsMap.put("cid", clientId);
			
			for (Msg msg : msgList) {
				
				HashMap<String, String> transMap = new HashMap<String, String>();
				
				transMap.put("is_show", "true");
				transMap.put("action", "msg");
				transMap.put("remind_title", msg.getTitle());
				transMap.put("remind_content", msg.getSummary());
				
				String jsonParams = GsonUtil.GsonString(transMap);
				
				paramsMap.put("transmissionContent", jsonParams);
				
				//录入时 设置的 定时发送的时间
				Long sendTime = msg.getSendTime();
				
				//当前的时间戳
				Long nowSecond = TimeStampUtil.getNowSecond();
				
				//定时推送
				if(sendTime< nowSecond+5 && sendTime-5 > nowSecond){
					
					boolean flag = PushUtil.AndroidPushToSingle(paramsMap);
					
					//发送之后,更新 该 消息为 已发送
					msg.setIsSend((short)1);
					
					msgService.updateByPrimaryKeySelective(msg);
					
				}
			}
		}
	}
	
	/*
	 * 为 服务人员 推送  "立即发送的消息"
	 */
	@RequestMapping(value ="send_msg_right_now.json",method = RequestMethod.GET)
	public void sendMsgRightNow() throws Exception{
		
			//可以 接受推送消息的 服务人员
			List<OrgStaffs> list = staffService.selectAbleToSendMsgStaff();
			
			//服务人员消息--立即发送的消息
			List<Msg> msgList = msgService.selectRightNowMsgByUserType(Constants.MSG_USER_TYPE_1);
			
			for (OrgStaffs orgStaffs : list) {
				
				//透传消息 参数 map
				HashMap<String, String> paramsMap = new HashMap<String, String>();
				
				Long staffId = orgStaffs.getStaffId();
				
				//取得 cid
				UserPushBind userPushBind = bindService.selectByUserId(staffId);
				
				String clientId = userPushBind.getClientId();
				
				paramsMap.put("cid", clientId);
				
				for (Msg msg : msgList) {
					
					HashMap<String, String> transMap = new HashMap<String, String>();
					
					transMap.put("is_show", "true");
					transMap.put("action", "msg");
					transMap.put("remind_title", msg.getTitle());
					transMap.put("remind_content", msg.getSummary());
					
					String jsonParams = GsonUtil.GsonString(transMap);
					
					paramsMap.put("transmissionContent", jsonParams);
					
						
					boolean flag = PushUtil.AndroidPushToSingle(paramsMap);
					
					//发送之后,更新 该 消息为 已发送
					msg.setIsSend((short)1);
					
					msgService.updateByPrimaryKeySelective(msg);
					
			}
		}
		
	}
}
