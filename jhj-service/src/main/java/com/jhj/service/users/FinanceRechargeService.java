package com.jhj.service.users;

import java.util.List;

import com.jhj.po.model.user.FinanceRecharge;
import com.jhj.vo.finance.FinanceSearchVo;
import com.jhj.vo.user.FinanceRechargeVo;

/**
 *
 * @author :hulj
 * @Date : 2016年3月28日上午11:33:01
 * @Description: 
 *		
 *		财务充值记录 service
 */
public interface FinanceRechargeService {
	
	int deleteByPrimaryKey(Long id);

    int insert(FinanceRecharge record);

    int insertSelective(FinanceRecharge record);

    FinanceRecharge selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FinanceRecharge record);

    int updateByPrimaryKey(FinanceRecharge record);
    
    FinanceRecharge initFinace();
    
    List<FinanceRecharge> selectByListPage(FinanceSearchVo searchVo);
    
    FinanceRechargeVo transToFinanceVo(FinanceRecharge recharge);
    
    FinanceRechargeVo initVo();
    
}
