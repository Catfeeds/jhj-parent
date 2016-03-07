package com.jhj.po.dao.orderReview;

import com.jhj.po.model.orderReview.JhjOrderReview;

public interface JhjOrderReviewMapper {
    int deleteByPrimaryKey(Long id);

    int insert(JhjOrderReview record);

    int insertSelective(JhjOrderReview record);

    JhjOrderReview selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(JhjOrderReview record);

    int updateByPrimaryKey(JhjOrderReview record);
}