package com.jhj.service.impl.bs;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.bs.OrgStaffFinanceMapper;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.vo.OrgStaffFinanceSearchVo;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午2:44:25
 * @Description: 
 *
 */
@Service
public class OrgStaffFinanceServiceImpl implements OrgStaffFinanceService {

	@Autowired
	private OrgStaffFinanceMapper orgStaffFinanceMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {
		
		return orgStaffFinanceMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrgStaffFinance record) {
		
		return orgStaffFinanceMapper.insert(record);
	}

	@Override
	public int insertSelective(OrgStaffFinance record) {
	
		return orgStaffFinanceMapper.insertSelective(record);
	}

	@Override
	public OrgStaffFinance selectByPrimaryKey(Long orderId) {
		
		return orgStaffFinanceMapper.selectByPrimaryKey(orderId);
	}
	
	

	@Override
	public int updateByPrimaryKeySelective(OrgStaffFinance record) {
		
		return orgStaffFinanceMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffFinance record) {
		
		return orgStaffFinanceMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrgStaffFinance initOrgStaffFinance() {
		
		OrgStaffFinance record = new OrgStaffFinance();

		record.setId(0L);
		record.setStaffId(0L);
		record.setMobile("");
		record.setTotalIncoming(new BigDecimal(0));
		record.setTotalDept(new BigDecimal(0));
		record.setTotalCash(new BigDecimal(0));
		record.setRestMoney(new BigDecimal(0));
		record.setAddTime(TimeStampUtil.getNowSecond());
        record.setUpdateTime(TimeStampUtil.getNowSecond());
		
		return record; 
	}

	@Override
	public OrgStaffFinance selectByStaffId(Long userId) {
		
		return orgStaffFinanceMapper.selectByStaffId(userId);
	}

	@Override
	public List<OrgStaffFinance> selectByListPage(
			OrgStaffFinanceSearchVo searchVo, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<OrgStaffFinance> list = orgStaffFinanceMapper.selectVoByListPage(searchVo);
		return list;
	}

}
