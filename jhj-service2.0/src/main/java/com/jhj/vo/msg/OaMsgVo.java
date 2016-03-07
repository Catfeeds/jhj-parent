package com.jhj.vo.msg;

import com.jhj.po.model.msg.Msg;

/**
 *
 * @author :hulj
 * @Date : 2016年1月28日下午5:51:08
 * @Description: 
 *		
 *		平台--消息列表管理，form表单VO
 */
public class OaMsgVo extends Msg {
	
	private Short sendWay;

	/**
	 * @return the sendWay
	 */
	public Short getSendWay() {
		return sendWay;
	}

	/**
	 * @param sendWay the sendWay to set
	 */
	public void setSendWay(Short sendWay) {
		this.sendWay = sendWay;
	}
}
