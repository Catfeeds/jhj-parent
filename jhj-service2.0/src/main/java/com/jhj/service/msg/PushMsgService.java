package com.jhj.service.msg;

/**
 *
 * @author :hulj
 * @Date : 2016年1月29日下午5:00:25
 * @Description: 
 *		
 *		该接口只负责  消息推送功能
 *		
 *	 任何地方使用，直接调用接口即可		
 */
public interface PushMsgService {
	
	void pushMsg(Long msgId) throws Exception;
}
