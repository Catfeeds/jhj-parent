package com.jhj.service.impl.bs;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.bs.OrgStaffPayDeptMapper;
import com.jhj.po.model.bs.OrgStaffPayDept;
import com.jhj.service.bs.OrgStaffPayDeptService;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午2:44:25
 * @Description:
 *
 */
@Service
public class OrgStaffPayDeptServiceImpl implements OrgStaffPayDeptService {

	@Autowired
	private OrgStaffPayDeptMapper orgStaffPayDeptMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {

		return orgStaffPayDeptMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrgStaffPayDept record) {

		return orgStaffPayDeptMapper.insert(record);
	}

	@Override
	public int insertSelective(OrgStaffPayDept record) {

		return orgStaffPayDeptMapper.insertSelective(record);
	}

	@Override
	public OrgStaffPayDept selectByPrimaryKey(Long id) {

		return orgStaffPayDeptMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffPayDept record) {

		return orgStaffPayDeptMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffPayDept record) {

		return orgStaffPayDeptMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrgStaffPayDept initOrgStaffPayDept() {

		OrgStaffPayDept record = new OrgStaffPayDept();
		record.setOrderId(0L);
		record.setStaffId(0L);
		record.setOrderNo("");
		record.setMobile("");
		record.setPayType((short) 0L);
		record.setOrderMoney(new BigDecimal(0));
		record.setOrderStatus((short) 1L);
		record.setPayAccount("");
		record.setTradeId("");
		record.setTradeStatus("");
		record.setAddTime(TimeStampUtil.getNowSecond());

		return record;
	}

	@Override
	public OrgStaffPayDept selectByOrderNo(String orderNo) {
		
		return orgStaffPayDeptMapper.selectByOrderNo(orderNo);
	}

	@Override
	public List<OrgStaffPayDept> selectByListPage(
			OrgStaffFinanceSearchVo searchVo, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<OrgStaffPayDept> list = orgStaffPayDeptMapper.selectVoByListPage(searchVo);
		return list;
	}
	
	@Override
	public List<OrgStaffPayDept> selectBySearchVo(OrderSearchVo searchVo) {
		return orgStaffPayDeptMapper.selectBySearchVo(searchVo);
	}
	
	@Override
	public BigDecimal totalBySearchVo(OrderSearchVo searchVo) {
		return orgStaffPayDeptMapper.totalBySearchVo(searchVo);
	}


}
