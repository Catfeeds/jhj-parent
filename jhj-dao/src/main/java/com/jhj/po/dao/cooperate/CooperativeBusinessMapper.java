package com.jhj.po.dao.cooperate;

import com.jhj.po.model.cooperate.CooperativeBusiness;

public interface CooperativeBusinessMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CooperativeBusiness record);

    int insertSelective(CooperativeBusiness record);

    CooperativeBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CooperativeBusiness record);

    int updateByPrimaryKey(CooperativeBusiness record);
}