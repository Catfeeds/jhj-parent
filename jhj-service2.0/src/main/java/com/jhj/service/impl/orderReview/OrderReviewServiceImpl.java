package com.jhj.service.impl.orderReview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.orderReview.JhjOrderReviewMapper;
import com.jhj.po.model.orderReview.JhjOrderReview;
import com.jhj.service.orderReview.OrderReviewService;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月6日上午10:23:34
 * @Description:
 * 		
 * 		独立模块： 订单 评价
 *
 */
@Service
public class OrderReviewServiceImpl implements OrderReviewService {
	@Autowired
	private JhjOrderReviewMapper orderReviewMapper;
	
	@Override
	public JhjOrderReview initOrderReview() {
		
		JhjOrderReview orderReview = new JhjOrderReview();
		
		orderReview.setId(0L);
		orderReview.setStaffNo("");
		orderReview.setServiceDate(0L);
		orderReview.setServiceHour("");
		orderReview.setArrriveOnTime((short)1);	//默认准时到达
		orderReview.setCleanReview((short)3);  	//默认都是3星评价
		orderReview.setServiceDetail((short)3);
		orderReview.setAppearance((short)3);
		orderReview.setAfterService((short)3);
		orderReview.setOverAllReview((short)3);
		orderReview.setAddTime(TimeStampUtil.getNow()/1000);
		
		return orderReview;
	}

	@Override
	public void insertOrderReview(JhjOrderReview orderReview) {
		orderReviewMapper.insertSelective(orderReview);
	}

}
