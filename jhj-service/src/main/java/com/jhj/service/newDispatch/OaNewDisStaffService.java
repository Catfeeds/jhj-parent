package com.jhj.service.newDispatch;

/**
 *
 * @author :hulj
 * @Date : 2016年3月23日下午2:50:04
 * @Description: 
 *		
 *		运营平台--手动派工 service
 */
public interface OaNewDisStaffService {
	
  void	manuDisAmOrder(Long orderId,String fromLat,String fromLng,Long properStaffId);
}
