package com.jhj.service.impl.bs;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.bs.OrgStaffCashMapper;
import com.jhj.po.dao.bs.OrgStaffsMapper;
import com.jhj.po.model.bs.OrgStaffCash;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.service.bs.OrgStaffCashService;
import com.jhj.vo.staff.OrgStaffCashSearchVo;
import com.jhj.vo.staff.OrgStaffCashVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.MathBigDecimalUtil;
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
	
	@Autowired
	private OrgStaffsMapper orgStaffsMapper;

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
	public List<OrgStaffCash> selectBySearchVo(OrgStaffCashSearchVo searchVo) {
		return orgStaffCashMapper.selectBySearchVo(searchVo);
	}

	@Override
	public PageInfo selectByListPage(OrgStaffCashSearchVo searchVo,
			int pageNo, int pageSize) {

		PageHelper.startPage(pageNo, pageSize);
		List<OrgStaffCash> list = orgStaffCashMapper.selectByListPage(searchVo);
		PageInfo result = new PageInfo(list);	
		return result;
	}

	@Override
	public BigDecimal getTotalCashMoney(OrgStaffCashSearchVo searchVo) {

		BigDecimal totalCashMoney = orgStaffCashMapper.getTotalCashMoney(searchVo);

		if (totalCashMoney == null) {
			BigDecimal a = new BigDecimal(0);
			return a;
		}
		totalCashMoney = MathBigDecimalUtil.round(totalCashMoney, 2);
		return totalCashMoney;
	}

	@Override
	public OrgStaffCashVo transVo(OrgStaffCash orgStaffCash) {
		OrgStaffCashVo vo=new OrgStaffCashVo();
		BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffCash,vo);
		Long staffId = orgStaffCash.getStaffId();
		OrgStaffs staffs = orgStaffsMapper.selectByPrimaryKey(staffId);
		if(staffs!=null){
			vo.setStaffName(staffs.getName());
		}
		return vo;
	}

	
}
