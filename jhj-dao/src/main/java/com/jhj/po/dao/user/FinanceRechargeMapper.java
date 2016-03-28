package com.jhj.po.dao.user;

import java.util.List;

import com.jhj.po.model.user.FinanceRecharge;

public interface FinanceRechargeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FinanceRecharge record);

    int insertSelective(FinanceRecharge record);

    FinanceRecharge selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FinanceRecharge record);

    int updateByPrimaryKey(FinanceRecharge record);
    
    List<FinanceRecharge> selectByListPage();
}