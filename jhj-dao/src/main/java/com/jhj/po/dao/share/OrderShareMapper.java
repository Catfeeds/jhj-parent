package com.jhj.po.dao.share;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.share.OrderShare;

public interface OrderShareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderShare record);

    int insertSelective(OrderShare record);

    OrderShare selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderShare record);

    int updateByPrimaryKey(OrderShare record);
    
    List<OrderShare> selectByShareId(Integer id);
    
    OrderShare selectByShareIdAndUserId(@Param("shareId") Integer shareId, @Param("userId")Integer userId);
}