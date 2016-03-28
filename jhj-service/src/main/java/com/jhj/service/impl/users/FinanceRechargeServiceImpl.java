package com.jhj.service.impl.users;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.user.FinanceRechargeMapper;
import com.jhj.po.model.user.FinanceRecharge;
import com.jhj.po.model.user.Users;
import com.jhj.service.users.FinanceRechargeService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.finance.FinanceSearchVo;
import com.jhj.vo.user.FinanceRechargeVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年3月28日上午11:34:12
 * @Description: 
 *		财务充值记录 
 */
@Service
public class FinanceRechargeServiceImpl implements FinanceRechargeService {
	
	@Autowired
	private FinanceRechargeMapper financeMapper;
	
	@Autowired
	private UsersService userService;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return financeMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(FinanceRecharge record) {
		return financeMapper.insert(record);
	}

	@Override
	public int insertSelective(FinanceRecharge record) {
		return financeMapper.insertSelective(record);
	}

	@Override
	public FinanceRecharge selectByPrimaryKey(Long id) {
		return financeMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(FinanceRecharge record) {
		return financeMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(FinanceRecharge record) {
		return financeMapper.updateByPrimaryKey(record);
	}

	@Override
	public FinanceRecharge initFinace() {
		
		FinanceRecharge recharge = new FinanceRecharge();
		
		recharge.setId(0L);
		recharge.setUserId(0L);
		recharge.setRechargeValue(new BigDecimal(0));
		recharge.setRestMoneyBefore(new BigDecimal(0));
		recharge.setRestMoneyAfter(new BigDecimal(0));
		recharge.setAdminId(0L);
		recharge.setAdminName("");
		recharge.setAdminMobile("");
		recharge.setApproveMobile("");
		recharge.setApproveToken("");
		recharge.setRemarks("");
		recharge.setAddTime(TimeStampUtil.getNowSecond());
		
		return recharge;
	}
	
	@Override
	public List<FinanceRecharge> selectByListPage(FinanceSearchVo searchVo) {
		return financeMapper.selectByListPage(searchVo);
	}
	
	@Override
	public FinanceRechargeVo initVo() {
		
		FinanceRechargeVo chargeVo = new FinanceRechargeVo();
		
		FinanceRecharge finace = initFinace();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(finace, chargeVo);
		
		chargeVo.setUserName("");
		chargeVo.setUserMobile("");
		
		return chargeVo;
	}
	
	@Override
	public FinanceRechargeVo transToFinanceVo(FinanceRecharge recharge) {
		
		FinanceRechargeVo initVo = initVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(recharge, initVo);
		
		Long userId = recharge.getUserId();
		
		Users users = userService.selectByUsersId(userId);
		
		initVo.setUserName(users.getName());
		initVo.setUserMobile(users.getMobile());
		
		return initVo;
	}
}
