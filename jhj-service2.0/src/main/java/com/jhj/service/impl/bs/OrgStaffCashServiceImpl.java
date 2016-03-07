package com.jhj.service.impl.bs;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.bs.OrgStaffCashMapper;
import com.jhj.po.model.bs.OrgStaffCash;
import com.jhj.service.bs.OrgStaffCashService;
import com.jhj.vo.OrgStaffCashSearchVo;
import com.meijia.utils.MathBigDeciamlUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午2:44:25
 * @Description: 
 *
 */
@Service
public class OrgStaffCashServiceImpl implements OrgStaffCashService {

	@Autowired
	private OrgStaffCashMapper orgStaffCashMapper;

	@Override
	public int deleteByPrimaryKey(Long orderId) {
		
		return orgStaffCashMapper.deleteByPrimaryKey(orderId);
	}

	@Override
	public int insert(OrgStaffCash record) {
		
		return orgStaffCashMapper.insert(record);
	}

	@Override
	public int insertSelective(OrgStaffCash record) {
	
		return orgStaffCashMapper.insertSelective(record);
	}

	@Override
	public OrgStaffCash selectByPrimaryKey(Long orderId) {
		
		return orgStaffCashMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffCash record) {
		
		return orgStaffCashMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffCash record) {
		
		return orgStaffCashMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrgStaffCash initOrgStaffCash() {
		
		OrgStaffCash record = new OrgStaffCash();

		record.setOrderId(0L);
		record.setOrderNo("");
		record.setStaffId(0L);
		record.setMobile("");
		record.setAccount("");
		record.setOrderMoney(new BigDecimal(0));
		record.setOrderStatus((short)0L);
		record.setRemarks("");
		record.setAddTime(TimeStampUtil.getNowSecond());
        record.setUpdateTime(TimeStampUtil.getNowSecond());
		
		return record; 
	}

	@Override
	public List<OrgStaffCash> selectByStaffId(Long userId) {
		
		return orgStaffCashMapper.selectByStaffId(userId);
	}

	@Override
	public List<OrgStaffCash> selectVoByListPage(OrgStaffCashSearchVo searchVo,
			int pageNo, int pageSize) {

		PageHelper.startPage(pageNo, pageSize);
		List<OrgStaffCash> list = orgStaffCashMapper.selectVoByListPage(searchVo);
		
		return list;
	}

	@Override
	public BigDecimal getTotalCashMoney(Long staffId) {

		BigDecimal totalCashMoney = orgStaffCashMapper.getTotalCashMoney(staffId);

		if (totalCashMoney == null) {
			BigDecimal a = new BigDecimal(0);
			return a;
		}
		totalCashMoney = MathBigDeciamlUtil.round(totalCashMoney, 2);
		return totalCashMoney;
	}

	
}
