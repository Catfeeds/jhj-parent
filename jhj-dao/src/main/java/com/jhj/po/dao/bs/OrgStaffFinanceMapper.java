package com.jhj.po.dao.bs;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;

public interface OrgStaffFinanceMapper {
    int deleteByPrimaryKey(Long id);

    Long insert(OrgStaffFinance record);

    Long insertSelective(OrgStaffFinance record);

    OrgStaffFinance selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffFinance record);

    int updateByPrimaryKey(OrgStaffFinance record);

	OrgStaffFinance selectByStaffId(Long staffId);

	List<OrgStaffFinance> selectByListPage(OrgStaffFinanceSearchVo searchVo);
	
	List<OrgStaffFinance> selectBySearchVo(OrgStaffFinanceSearchVo searchVo);
	
	//统计服务人员欠款
	Map<String,Object> totalMoney(OrgStaffFinanceSearchVo searchVo);
}