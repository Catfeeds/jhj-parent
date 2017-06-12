package com.jhj.service.impl.share;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.share.OrderShareMapper;
import com.jhj.po.model.share.OrderShare;
import com.jhj.service.share.OrderShareService;

@Service
public class OrderShareServiceImpl implements OrderShareService{
	
	@Autowired
	private OrderShareMapper orderShareMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return orderShareMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderShare record) {
		return orderShareMapper.insert(record);
	}

	@Override
	public int insertSelective(OrderShare record) {
		return orderShareMapper.insertSelective(record);
	}

	@Override
	public OrderShare selectByPrimaryKey(Integer id) {
		return orderShareMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrderShare record) {
		return orderShareMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrderShare record) {
		return orderShareMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<OrderShare> selectByShareId(Integer shareId) {
		
		return orderShareMapper.selectByShareId(shareId);
	}

	@Override
	public OrderShare selectByShareIdAndUserId(Integer shareId, Integer userId) {
		return orderShareMapper.selectByShareIdAndUserId(shareId,userId);
	}

}
