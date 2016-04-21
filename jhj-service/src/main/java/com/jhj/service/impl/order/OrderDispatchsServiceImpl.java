package com.jhj.service.impl.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.common.Constants;
import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.service.order.OrderDispatchsService;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月20日下午5:18:49
 * @Description: 
 *
 */
@Service
public class OrderDispatchsServiceImpl implements OrderDispatchsService {
	
	@Autowired
	private OrderDispatchsMapper orderDisMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return orderDisMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderDispatchs record) {
		return orderDisMapper.insert(record);
	}

	@Override
	public int insertSelective(OrderDispatchs record) {
		return orderDisMapper.insertSelective(record);
	}

	@Override
	public OrderDispatchs selectByPrimaryKey(Long id) {
		return orderDisMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrderDispatchs record) {
		return orderDisMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrderDispatchs record) {
		return orderDisMapper.updateByPrimaryKey(record);
	}

	/*@Override
	public List<Long> getBadOrgStaff(Long userId,List<Long> orderIdList) {
		return orderDisMapper.getBadOrgStaff(userId,orderIdList);
	}*/
	
	
	
	@Override
	public OrderDispatchs initOrderDisp() {
		
		OrderDispatchs dispatchs = new OrderDispatchs();
		
		dispatchs.setId(0L);
		dispatchs.setUserId(0L);
		dispatchs.setMobile("");
		dispatchs.setOrderId(0L);
		dispatchs.setOrderNo("");
		dispatchs.setServiceDatePre(0L);
		dispatchs.setServiceDate(0L);
		dispatchs.setServiceHours((short) 0);
		dispatchs.setOrgId(0L);
		dispatchs.setStaffId(0L);
		dispatchs.setStaffName("");
		dispatchs.setStaffMobile("");
		dispatchs.setRemarks("");
		dispatchs.setAmId(0L);
		dispatchs.setDispatchStatus(Constants.ORDER_DIS_ENABLE); //??派工状态 默认有效吧？
	    dispatchs.setPickAddrName("");
	    dispatchs.setPickAddrLat("");
	    dispatchs.setPickAddrLng("");
	    dispatchs.setPickAddr("");
	    dispatchs.setPickDistance(0);
	    dispatchs.setUserAddrDistance(0);
		dispatchs.setAddTime(TimeStampUtil.getNow() / 1000);
		dispatchs.setUpdateTime(0L);
		
		return dispatchs;
	}

	@Override
	public List<OrderDispatchs> getBadOrgStaff(Map<String, Object> map) {
		return orderDisMapper.getBadOrgStaff(map);
	}

	@Override
	public List<OrderDispatchs> selectEnableStaffNow(Long orgId, Long serviceDateStart, Long serviceDateEnd) {
		return orderDisMapper.selectEnableStaffNow(orgId, serviceDateStart, serviceDateEnd);
	}

	@Override
	public OrderDispatchs selectByOrderNo(String orderNo) {
		return orderDisMapper.selectByOrderNo(orderNo);
	}
	@Override
	public OrderDispatchs selectByUserId(Long userId) {
		return orderDisMapper.selectByUserId(userId);
	}
	
	@Override
	public List<HashMap> getUserIdsByListPage(Long staffId, int pageNo, int pageSize) {
		
		PageHelper.startPage(pageNo, pageSize);
		List<HashMap> list = orderDisMapper.getUserIdsByListPage(staffId);
		return list;
	}	
	
	@Override
	public List<OrderDispatchs> selectByOrderIds(List<Long> orderIds) {
		return orderDisMapper.selectByOrderIds(orderIds);
	}

	@Override
	public List<OrderDispatchs> selectAll() {
		return orderDisMapper.selectAll();
	}


	@Override
	public OrderDispatchs selectByOrderId(Long orderId) {
		return orderDisMapper.selectByOrderId(orderId);
	}


	@Override
	public List<OrderDispatchs> selectByNoAndDisStatus(String orderNo, Short disStatus) {
		return orderDisMapper.selectByNoAndDisStatus(orderNo, disStatus);
	}	

	@Override
	public Long getTodayOrderNumForTheSta(Long staffId) {
		return orderDisMapper.getTodayOrderNumForTheSta(staffId);
	}
}
