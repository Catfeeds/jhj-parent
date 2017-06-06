package com.jhj.service.share;

import com.jhj.po.model.share.OrderShare;

public interface OrderShareService {
	int deleteByPrimaryKey(Integer id);

    int insert(OrderShare record);

    int insertSelective(OrderShare record);

    OrderShare selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderShare record);

    int updateByPrimaryKey(OrderShare record);
}
