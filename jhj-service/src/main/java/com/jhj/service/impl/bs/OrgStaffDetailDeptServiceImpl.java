package com.jhj.service.impl.bs;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.bs.OrgStaffDetailDeptMapper;
import com.jhj.po.model.bs.OrgStaffDetailDept;
import com.jhj.service.bs.OrgStaffDetailDeptService;
import com.jhj.vo.OrgStaffDetailPaySearchVo;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午2:44:25
 * @Description:
 *
 */
@Service
public class OrgStaffDetailDeptServiceImpl implements OrgStaffDetailDeptService {

	@Autowired
	private OrgStaffDetailDeptMapper orgStaffDetailDeptMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {

		return orgStaffDetailDeptMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrgStaffDetailDept record) {

		return orgStaffDetailDeptMapper.insert(record);
	}

	@Override
	public int insertSelective(OrgStaffDetailDept record) {

		return orgStaffDetailDeptMapper.insertSelective(record);
	}

	@Override
	public OrgStaffDetailDept selectByPrimaryKey(Long id) {

		return orgStaffDetailDeptMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffDetailDept record) {

		return orgStaffDetailDeptMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffDetailDept record) {

		return orgStaffDetailDeptMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrgStaffDetailDept initOrgStaffDetailDept() {

		OrgStaffDetailDept record = new OrgStaffDetailDept();
	
		record.setId(0L);
		record.setStaffId(0L);
		record.setMobile("");
		record.setOrderType((short) 0L);
		record.setOrderId(0L);
		record.setOrderNo("");
		record.setOrderMoney(new BigDecimal(0));
		record.setOrderDept(new BigDecimal(0));
		record.setOrderStatusStr("");
		record.setAddTime(TimeStampUtil.getNowSecond());

		return record;
	}
	
	@Override
	public List<OrgStaffDetailDept> selectBySearchVo(OrgStaffDetailPaySearchVo searchVo) {		
		return orgStaffDetailDeptMapper.selectBySearchVo(searchVo);
	}

	
}
